package ControladorGECA;

import InterfacesGECA.CRUDUsuarioGECA;
import ModeloDAOGECA.clsUsuarioDAOImplGECA;
import ModeloGECA.clsUsuarioGeca;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorUsuarioServletGECA", urlPatterns = {"/usuarioGECA"})
public class ControladorUsuarioServletGECA extends HttpServlet {

    private static final String GESTION_JSP = "/vistaGECA/gestionUsuariosGECA.jsp";
    private final CRUDUsuarioGECA usuarioDAO = new clsUsuarioDAOImplGECA();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!esAdministradorGECA(request, response)) {
            return;
        }
        String accion = request.getParameter("accion");
        if ("editar".equals(accion)) {
            cargarUsuarioGECA(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminarUsuarioGECA(request, response);
        } else {
            listarUsuariosGECA(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!esAdministradorGECA(request, response)) {
            return;
        }
        String accion = request.getParameter("accion");
        if ("guardar".equals(accion)) {
            registrarUsuarioGECA(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarUsuarioGECA(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarioGECA");
        }
    }

    private void listarUsuariosGECA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("usuariosGECA", usuarioDAO.listarUsuariosGECA());
        request.getRequestDispatcher(GESTION_JSP).forward(request, response);
    }

    private void cargarUsuarioGECA(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("idUsuarioGeca");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Optional<clsUsuarioGeca> usuario = usuarioDAO.obtenerUsuarioPorIdGECA(id);
            usuario.ifPresent(u -> request.setAttribute("usuarioEditarGECA", u));
        }
        listarUsuariosGECA(request, response);
    }

    private void registrarUsuarioGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        clsUsuarioGeca usuario = obtenerUsuarioDesdeFormularioGECA(request, false);
        if (usuarioDAO.registrarUsuarioGECA(usuario)) {
            request.getSession().setAttribute("mensajeExitoGECA", "Usuario registrado correctamente.");
        } else {
            request.getSession().setAttribute("mensajeErrorGECA", "No se pudo registrar el usuario.");
        }
        response.sendRedirect(request.getContextPath() + "/usuarioGECA");
    }

    private void actualizarUsuarioGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        clsUsuarioGeca usuario = obtenerUsuarioDesdeFormularioGECA(request, true);
        if (usuarioDAO.actualizarUsuarioGECA(usuario)) {
            request.getSession().setAttribute("mensajeExitoGECA", "Usuario actualizado correctamente.");
        } else {
            request.getSession().setAttribute("mensajeErrorGECA", "No se pudo actualizar el usuario.");
        }
        response.sendRedirect(request.getContextPath() + "/usuarioGECA");
    }

    private void eliminarUsuarioGECA(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("idUsuarioGeca");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            if (usuarioDAO.eliminarUsuarioGECA(id)) {
                request.getSession().setAttribute("mensajeExitoGECA", "Usuario eliminado correctamente.");
            } else {
                request.getSession().setAttribute("mensajeErrorGECA", "No se pudo eliminar el usuario.");
            }
        }
        response.sendRedirect(request.getContextPath() + "/usuarioGECA");
    }

    private clsUsuarioGeca obtenerUsuarioDesdeFormularioGECA(HttpServletRequest request, boolean incluirId) {
        clsUsuarioGeca usuario = new clsUsuarioGeca();
        if (incluirId) {
            usuario.setIdUsuarioGeca(Integer.parseInt(request.getParameter("idUsuarioGeca")));
        }
        usuario.setNombreGeca(request.getParameter("nombreGeca"));
        usuario.setEmailGeca(request.getParameter("emailGeca"));
        usuario.setPasswordGeca(request.getParameter("passwordGeca"));
        usuario.setRolGeca(request.getParameter("rolGeca"));
        usuario.setIpPermitidaGeca(request.getParameter("ipPermitidaGeca"));
        String estado = request.getParameter("estadoGeca");
        usuario.setEstadoGeca(estado != null ? estado : "Activo");
        return usuario;
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
