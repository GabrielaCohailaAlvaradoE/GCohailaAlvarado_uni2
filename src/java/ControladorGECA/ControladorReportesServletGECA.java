package ControladorGECA;

import InterfacesGECA.CRUDReclamoGECA;
import ModeloDAOGECA.clsReclamoDAOImplGECA;
import ModeloGECA.clsnombreGECA;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorReportesServletGECA", urlPatterns = {"/reportesGECA"})
public class ControladorReportesServletGECA extends HttpServlet {

    private static final String REPORTES_JSP = "/vistaGECA/reportesGECA.jsp";
    private final CRUDReclamoGECA reclamoDAO = new clsReclamoDAOImplGECA();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!esAdministradorGECA(request, response)) {
            return;
        }
        List<clsnombreGECA> resumenEstado = reclamoDAO.obtenerResumenPorEstadoGECA();
        List<clsnombreGECA> resumenCategoria = reclamoDAO.obtenerResumenPorCategoriaGECA();
        request.setAttribute("resumenEstadoGECA", resumenEstado);
        request.setAttribute("resumenCategoriaGECA", resumenCategoria);
        request.getRequestDispatcher(REPORTES_JSP).forward(request, response);
    }

    private boolean esAdministradorGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/loginGECA");
            return false;
        }
        String rol = (String) session.getAttribute("rolGECA");
        if (rol == null || !"Administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/loginGECA");
            return false;
        }
        return true;
    }
}