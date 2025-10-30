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

@WebServlet(name = "ControladorReclamoServletGECA", urlPatterns = {"/reclamoGECA"})
public class ControladorReclamoServletGECA extends HttpServlet {

    private static final String FORM_JSP = "/vistaGECA/registroReclamoGECA.jsp";
    private static final String LISTA_JSP = "/vistaGECA/seguimientoReclamosGECA.jsp";
    private static final String DETALLE_JSP = "/vistaGECA/detalleReclamoGECA.jsp";

    private final CRUDReclamoGECA reclamoDAO = new clsReclamoDAOImplGECA();
    private final CRUDCategoriaGECA categoriaDAO = new clsCategoriaDAOImplGECA();
    private final CRUDSeguimientoGECA seguimientoDAO = new clsSeguimientoDAOImplGECA();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        clsUsuarioGeca usuario = obtenerUsuarioSesionGECA(request, response);
        if (usuario == null) {
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) {
            mostrarFormularioGECA(request, response, usuario);
            return;
        }
        switch (accion) {
            case "listar":
                listarReclamosGECA(request, response, usuario);
                break;
            case "detalle":
                mostrarDetalleGECA(request, response, usuario);
                break;
            case "nuevo":
            default:
                mostrarFormularioGECA(request, response, usuario);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        clsUsuarioGeca usuario = obtenerUsuarioSesionGECA(request, response);
        if (usuario == null) {
            return;
        }
        String titulo = request.getParameter("tituloReclamoGeca");
        String descripcion = request.getParameter("descripcionReclamoGeca");
        String idCategoria = request.getParameter("idCategoriaGeca");
        String prioridad = request.getParameter("prioridadGeca");

        clsReclamoGECA reclamo = new clsReclamoGECA();
        reclamo.setIdUsuarioGeca(usuario.getIdUsuarioGeca());
        reclamo.setTituloReclamoGeca(titulo);
        reclamo.setDescripcionReclamoGeca(descripcion);
        reclamo.setPrioridadGeca(prioridad != null ? prioridad : "Media");
        reclamo.setEstadoGeca("Pendiente");
        if (idCategoria != null && !idCategoria.isEmpty()) {
            reclamo.setIdCategoriaGeca(Integer.parseInt(idCategoria));
        }

        boolean registrado = reclamoDAO.registrarReclamoGECA(reclamo);
        if (registrado) {
            request.getSession().setAttribute("mensajeExitoGECA", "Reclamo registrado correctamente.");
            response.sendRedirect(request.getContextPath() + "/reclamoGECA?accion=listar");
        } else {
            request.setAttribute("mensajeErrorGECA", "No se pudo registrar el reclamo. Intente nuevamente.");
            mostrarFormularioGECA(request, response, usuario);
        }
    }

    private clsUsuarioGeca obtenerUsuarioSesionGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioGECA") == null) {
            response.sendRedirect(request.getContextPath() + "/loginGECA");
            return null;
        }
        return (clsUsuarioGeca) session.getAttribute("usuarioGECA");
    }

    private void mostrarFormularioGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca usuario)
            throws ServletException, IOException {
        request.setAttribute("categoriasGECA", categoriaDAO.listarCategoriasGECA());
        request.getRequestDispatcher(FORM_JSP).forward(request, response);
    }

    private void listarReclamosGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca usuario)
            throws ServletException, IOException {
        request.setAttribute("reclamosGECA", reclamoDAO.listarReclamosPorUsuarioGECA(usuario.getIdUsuarioGeca()));
        request.setAttribute("mensajeExitoGECA", request.getSession().getAttribute("mensajeExitoGECA"));
        request.getSession().removeAttribute("mensajeExitoGECA");
        request.getRequestDispatcher(LISTA_JSP).forward(request, response);
    }

    private void mostrarDetalleGECA(HttpServletRequest request, HttpServletResponse response, clsUsuarioGeca usuario)
            throws ServletException, IOException {
        String idParam = request.getParameter("idReclamoGeca");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/reclamoGECA?accion=listar");
            return;
        }
        int idReclamo = Integer.parseInt(idParam);
        Optional<clsReclamoGECA> reclamoOpt = reclamoDAO.obtenerReclamoPorIdGECA(idReclamo);
        if (!reclamoOpt.isPresent() || reclamoOpt.get().getIdUsuarioGeca() != usuario.getIdUsuarioGeca()) {
            response.sendRedirect(request.getContextPath() + "/reclamoGECA?accion=listar");
            return;
        }
        List<clsSeguimientoGECA> seguimientos = seguimientoDAO.listarPorReclamoGECA(idReclamo);
        request.setAttribute("reclamoDetalleGECA", reclamoOpt.get());
        request.setAttribute("seguimientosGECA", seguimientos);
        request.getRequestDispatcher(DETALLE_JSP).forward(request, response);
    }
}