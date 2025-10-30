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
import java.util.List;
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
        int idReclamo = Integer.parseInt(request.getParameter("idReclamoGeca"));
        String estadoActual = request.getParameter("estadoActualGeca");
        String nuevoEstado = request.getParameter("nuevoEstadoGeca");
        if (nuevoEstado != null) {
            nuevoEstado = nuevoEstado.trim();
        }
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
        String observaciones = request.getParameter("observacionesGeca");
        String observacionesLimpias = observaciones != null ? observaciones.trim() : null;
        boolean hayObservaciones = observacionesLimpias != null && !observacionesLimpias.isEmpty();
        if (!hayObservaciones) {
            observacionesLimpias = null;
        }

        boolean cambioEstado = estadoActual != null && !estadoActual.equalsIgnoreCase(estadoCanonico);
        boolean actualizado = reclamoDAO.actualizarEstadoGECA(idReclamo, estadoCanonico, administrador.getIdUsuarioGeca(), observacionesLimpias);
        if (actualizado) {
            String mensaje;
            if (cambioEstado) {
                mensaje = "Estado del reclamo actualizado a \"" + estadoCanonico + "\" correctamente.";
            } else if (hayObservaciones) {
                mensaje = "Las observaciones fueron registradas para el reclamo.";
            } else {
                mensaje = "El reclamo ya se encontraba en el estado \"" + estadoCanonico + "\".";
            }
            request.getSession().setAttribute("mensajeExitoGECA", mensaje);
        } else {
            request.getSession().setAttribute("mensajeErrorGECA", "No se pudo actualizar el estado del reclamo.");
        }
        response.sendRedirect(request.getContextPath() + "/adminGECA?accion=detalle&idReclamoGeca=" + idReclamo);
    }

    private void registrarSeguimientoGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca administrador)
            throws IOException {
        int idReclamo = Integer.parseInt(request.getParameter("idReclamoGeca"));
        String accion = request.getParameter("accionReclamoGeca");
        String observaciones = request.getParameter("observacionesGeca");
        String estado = request.getParameter("estadoSeguimientoGeca");

        clsSeguimientoGECA seguimiento = new clsSeguimientoGECA();
        seguimiento.setIdReclamoGeca(idReclamo);
        seguimiento.setIdUsuarioGeca(administrador.getIdUsuarioGeca());
        seguimiento.setAccionGeca(accion);
        seguimiento.setObservacionesGeca(observaciones);
        seguimiento.setNuevoEstadoGeca(estado);

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
        String candidato = estado.trim();
        if (candidato.isEmpty()) {
            return null;
        }
        for (String valido : ESTADOS_VALIDOS) {
            if (valido.equalsIgnoreCase(candidato)) {
                return valido;
            }
        }
        return null;
    }
}
