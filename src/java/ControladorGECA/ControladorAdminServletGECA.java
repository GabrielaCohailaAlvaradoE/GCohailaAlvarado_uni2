package ControladorGECA;

import InterfacesGECA.CRUDCategoriaGECA;
import InterfacesGECA.CRUDReclamoGECA;
import InterfacesGECA.CRUDSeguimientoGECA;
import ModeloDAOGECA.clsCategoriaDAOImplGECA;
import ModeloDAOGECA.clsReclamoDAOImplGECA;
import ModeloDAOGECA.clsSeguimientoDAOImplGECA;
import ModeloGECA.clsReclamoGECA;
import ModeloGECA.clsSeguimientoGECA;
import ModeloGECA.clsUsuarioGeca;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorAdminServletGECA", urlPatterns = {"/adminGECA"})
public class ControladorAdminServletGECA extends HttpServlet {

    private static final String PANEL_JSP = "/vistaGECA/panelAdminGECA.jsp";
    private static final String DETALLE_JSP = "/vistaGECA/detalleReclamoGECA.jsp";
    private static final String[] ESTADOS_VALIDOS = {"Pendiente", "En atención", "Resuelto"};
    private static final Map<String, String> ESTADOS_VALIDOS_NORMALIZADOS;

    static {
        Map<String, String> estados = new HashMap<>();
        for (String estado : ESTADOS_VALIDOS) {
            String clavePrincipal = normalizarClaveEstadoGECA(estado);
            if (!clavePrincipal.isEmpty()) {
                estados.put(clavePrincipal, estado);
                String claveSinEspacios = clavePrincipal.replace(" ", "");
                if (!claveSinEspacios.equals(clavePrincipal)) {
                    estados.put(claveSinEspacios, estado);
                }
            }
        }
        ESTADOS_VALIDOS_NORMALIZADOS = Collections.unmodifiableMap(estados);
    }

    private final CRUDReclamoGECA reclamoDAO = new clsReclamoDAOImplGECA();
    private final CRUDSeguimientoGECA seguimientoDAO = new clsSeguimientoDAOImplGECA();
    private final CRUDCategoriaGECA categoriaDAO = new clsCategoriaDAOImplGECA();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        clsUsuarioGeca administrador = obtenerAdministradorGECA(request, response);
        if (administrador == null) {
            return;
        }
        String accion = request.getParameter("accion");
        if ("detalle".equals(accion)) {
            mostrarDetalleReclamoGECA(request, response);
        } else {
            mostrarPanelGECA(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        clsUsuarioGeca administrador = obtenerAdministradorGECA(request, response);
        if (administrador == null) {
            return;
        }
        String accion = request.getParameter("accion");
        if ("actualizarEstado".equals(accion)) {
            actualizarEstadoGECA(request, response, administrador);
        } else if ("registrarSeguimiento".equals(accion)) {
            registrarSeguimientoGECA(request, response, administrador);
        } else {
            response.sendRedirect(request.getContextPath() + "/adminGECA");
        }
    }

    private void mostrarPanelGECA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String estadoFiltro = request.getParameter("estadoFiltroGeca");
        List<clsReclamoGECA> reclamos;
        if (estadoFiltro == null || estadoFiltro.isEmpty() || "Todos".equalsIgnoreCase(estadoFiltro)) {
            reclamos = reclamoDAO.listarTodosGECA();
        } else {
            reclamos = reclamoDAO.listarReclamosPorEstadoGECA(estadoFiltro);
        }
        request.setAttribute("reclamosGECA", reclamos);
        request.setAttribute("categoriasGECA", categoriaDAO.listarCategoriasGECA());
        request.getRequestDispatcher(PANEL_JSP).forward(request, response);
    }

    private void mostrarDetalleReclamoGECA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idReclamoGeca");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/adminGECA");
            return;
        }
        int idReclamo = Integer.parseInt(idParam);
        Optional<clsReclamoGECA> reclamoOpt = reclamoDAO.obtenerReclamoPorIdGECA(idReclamo);
        if (!reclamoOpt.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/adminGECA");
            return;
        }
        List<clsSeguimientoGECA> seguimientos = seguimientoDAO.listarPorReclamoGECA(idReclamo);
        request.setAttribute("reclamoDetalleGECA", reclamoOpt.get());
        request.setAttribute("seguimientosGECA", seguimientos);
        request.getRequestDispatcher(DETALLE_JSP).forward(request, response);
    }

    private void actualizarEstadoGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca administrador)
            throws IOException {
        String idParam = request.getParameter("idReclamoGeca");
        if (idParam == null) {
            request.getSession().setAttribute("mensajeErrorGECA", "No se indicó el reclamo a actualizar.");
            response.sendRedirect(request.getContextPath() + "/adminGECA");
            return;
        }
        final int idReclamo;
        try {
            idReclamo = Integer.parseInt(idParam);
        } catch (NumberFormatException ex) {
            request.getSession().setAttribute("mensajeErrorGECA", "El identificador del reclamo no es válido.");
            response.sendRedirect(request.getContextPath() + "/adminGECA");
            return;
        }
        Optional<clsReclamoGECA> reclamoOpt = reclamoDAO.obtenerReclamoPorIdGECA(idReclamo);
        if (!reclamoOpt.isPresent()) {
            request.getSession().setAttribute("mensajeErrorGECA", "El reclamo seleccionado no existe.");
            response.sendRedirect(request.getContextPath() + "/adminGECA");
            return;
        }
        String nuevoEstado = recortarEspaciosGECA(request.getParameter("nuevoEstadoGeca"));
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            request.getSession().setAttribute("mensajeErrorGECA", "Debe seleccionar un estado válido para el reclamo.");
            response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
            return;
        }
        String estadoCanonico = normalizarEstadoGECA(nuevoEstado);
        if (estadoCanonico == null) {
            request.getSession().setAttribute("mensajeErrorGECA", "El estado seleccionado no es válido.");
            response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
            return;
        }
        String observaciones = recortarEspaciosGECA(request.getParameter("observacionesGeca"));
        String observacionesLimpias = observaciones;
        boolean hayObservaciones = observacionesLimpias != null && !observacionesLimpias.isEmpty();
        if (!hayObservaciones) {
            observacionesLimpias = null;
        }

        clsReclamoGECA reclamo = reclamoOpt.get();
        String estadoActual = reclamo.getEstadoGeca();
        boolean cambioEstado = !sonEstadosEquivalentesGECA(estadoActual, estadoCanonico);

        if (!cambioEstado) {
            if (hayObservaciones) {
                clsSeguimientoGECA seguimiento = new clsSeguimientoGECA();
                seguimiento.setIdReclamoGeca(idReclamo);
                seguimiento.setIdUsuarioGeca(administrador.getIdUsuarioGeca());
                seguimiento.setAccionGeca("Observaciones registradas");
                seguimiento.setObservacionesGeca(observacionesLimpias);
                seguimiento.setNuevoEstadoGeca(estadoCanonico);
                if (seguimientoDAO.registrarSeguimientoGECA(seguimiento)) {
                    request.getSession().setAttribute("mensajeExitoGECA", "Las observaciones fueron registradas para el reclamo.");
                } else {
                    request.getSession().setAttribute("mensajeErrorGECA", "No se pudieron registrar las observaciones del reclamo.");
                }
            } else {
                request.getSession().setAttribute("mensajeExitoGECA", "El reclamo ya se encontraba en el estado \"" + estadoCanonico + "\".");
            }
            response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
            return;
        }

        boolean actualizado = reclamoDAO.actualizarEstadoGECA(idReclamo, estadoCanonico, administrador.getIdUsuarioGeca(), observacionesLimpias);
        if (actualizado) {
            String mensaje = hayObservaciones
                    ? "Estado del reclamo actualizado a \"" + estadoCanonico + "\" y observaciones registradas correctamente."
                    : "Estado del reclamo actualizado a \"" + estadoCanonico + "\" correctamente.";
            request.getSession().setAttribute("mensajeExitoGECA", mensaje);
        } else {
            request.getSession().setAttribute("mensajeErrorGECA", "No se pudo actualizar el estado del reclamo.");
        }
        response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
    }

    private void registrarSeguimientoGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca administrador)
            throws IOException {
        int idReclamo = Integer.parseInt(request.getParameter("idReclamoGeca"));
        String accion = recortarEspaciosGECA(request.getParameter("accionReclamoGeca"));
        if (accion != null && accion.isEmpty()) {
            accion = null;
        }
        String observaciones = recortarEspaciosGECA(request.getParameter("observacionesGeca"));
        if (observaciones != null && observaciones.isEmpty()) {
            observaciones = null;
        }
        String estado = request.getParameter("estadoSeguimientoGeca");
        String estadoCanonico = normalizarEstadoGECA(estado);
        String estadoRecortado = recortarEspaciosGECA(estado);
        if (estadoCanonico == null && estadoRecortado != null && !estadoRecortado.isEmpty()) {
            request.getSession().setAttribute("mensajeErrorGECA", "El estado seleccionado no es válido.");
            response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
            return;
        }

        clsSeguimientoGECA seguimiento = new clsSeguimientoGECA();
        seguimiento.setIdReclamoGeca(idReclamo);
        seguimiento.setIdUsuarioGeca(administrador.getIdUsuarioGeca());
        seguimiento.setAccionGeca(accion);
        seguimiento.setObservacionesGeca(observaciones);
        seguimiento.setNuevoEstadoGeca(estadoCanonico);

        if (seguimientoDAO.registrarSeguimientoGECA(seguimiento)) {
            request.getSession().setAttribute("mensajeExitoGECA", "Seguimiento registrado correctamente.");
        } else {
            request.getSession().setAttribute("mensajeErrorGECA", "No se pudo registrar el seguimiento.");
        }
        response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
    }

    private clsUsuarioGeca obtenerAdministradorGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/loginGECA");
            return null;
        }
        clsUsuarioGeca usuario = (clsUsuarioGeca) session.getAttribute("usuarioGECA");
        String rol = (String) session.getAttribute("rolGECA");
        if (usuario == null || rol == null || !"Administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/loginGECA");
            return null;
        }
        return usuario;
    }

    private String normalizarEstadoGECA(String estado) {
        if (estado == null) {
            return null;
        }
        String candidato = recortarEspaciosGECA(estado);
        if (candidato == null || candidato.isEmpty()) {
            return null;
        }
        String clave = normalizarClaveEstadoGECA(candidato);
        if (clave.isEmpty()) {
            return null;
        }
        String estadoCanonico = ESTADOS_VALIDOS_NORMALIZADOS.get(clave);
        if (estadoCanonico == null && clave.indexOf(' ') >= 0) {
            estadoCanonico = ESTADOS_VALIDOS_NORMALIZADOS.get(clave.replace(" ", ""));
        }
        return estadoCanonico;
    }

    private static boolean sonEstadosEquivalentesGECA(String estadoA, String estadoB) {
        String normalizadoA = normalizarClaveEstadoGECA(estadoA);
        String normalizadoB = normalizarClaveEstadoGECA(estadoB);
        if (normalizadoA.isEmpty() && normalizadoB.isEmpty()) {
            return true;
        }
        if (normalizadoA.equals(normalizadoB)) {
            return true;
        }
        return normalizadoA.replace(" ", "").equals(normalizadoB.replace(" ", ""));
    }

    private static String normalizarClaveEstadoGECA(String texto) {
        String recortado = recortarEspaciosGECA(texto);
        if (recortado == null || recortado.isEmpty()) {
            return "";
        }
        String textoNormalizado = Normalizer.normalize(recortado, Normalizer.Form.NFD);
        StringBuilder resultado = new StringBuilder(textoNormalizado.length());
        boolean hayEspacioPendiente = false;
        for (int i = 0; i < textoNormalizado.length(); i++) {
            char c = textoNormalizado.charAt(i);
            if (Character.getType(c) == Character.NON_SPACING_MARK) {
                continue;
            }
            if (Character.isWhitespace(c)) {
                hayEspacioPendiente = resultado.length() > 0;
                continue;
            }
            if (Character.isLetterOrDigit(c)) {
                if (hayEspacioPendiente) {
                    resultado.append(' ');
                    hayEspacioPendiente = false;
                }
                resultado.append(Character.toLowerCase(c));
            }
        }
        int longitud = resultado.length();
        while (longitud > 0 && resultado.charAt(longitud - 1) == ' ') {
            resultado.setLength(--longitud);
        }
        return resultado.toString();
    }

    private static String recortarEspaciosGECA(String texto) {
        if (texto == null) {
            return null;
        }
        int inicio = 0;
        int fin = texto.length() - 1;
        while (inicio <= fin && Character.isWhitespace(texto.charAt(inicio))) {
            inicio++;
        }
        while (fin >= inicio && Character.isWhitespace(texto.charAt(fin))) {
            fin--;
        }
        if (inicio > fin) {
            return "";
        }
        return texto.substring(inicio, fin + 1);
    }
}
