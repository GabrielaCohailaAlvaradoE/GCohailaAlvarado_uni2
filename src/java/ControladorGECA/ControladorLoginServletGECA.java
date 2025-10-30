package ControladorGECA;

import InterfacesGECA.CRUDUsuarioGECA;
import ModeloDAOGECA.clsUsuarioDAOImplGECA;
import ModeloGECA.clsUsuarioGeca;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ControladorLoginServletGECA", urlPatterns = {"/loginGECA"})
public class ControladorLoginServletGECA extends HttpServlet {

    private static final String LOGIN_JSP = "/loginGECA.jsp";
    private final CRUDUsuarioGECA usuarioDAO = new clsUsuarioDAOImplGECA();
    private final Random random = new Random();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if ("logout".equals(request.getParameter("accion"))) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + LOGIN_JSP);
            return;
        }
        generarCaptchaGECA(session, request);
        request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = request.getParameter("emailGeca");
        String password = request.getParameter("passwordGeca");
        String captchaIngresado = request.getParameter("captchaGeca");
        String captchaEsperado = String.valueOf(session.getAttribute("captchaResultadoGECA"));

        if (captchaIngresado == null || !captchaIngresado.equals(captchaEsperado)) {
            request.setAttribute("mensajeErrorGECA", "Captcha inválido. Intente nuevamente.");
            generarCaptchaGECA(session, request);
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        Optional<clsUsuarioGeca> usuarioOpt = usuarioDAO.validarAccesoGECA(email, password);
        if (!usuarioOpt.isPresent()) {
            request.setAttribute("mensajeErrorGECA", "Credenciales inválidas o usuario inactivo.");
            generarCaptchaGECA(session, request);
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        clsUsuarioGeca usuario = usuarioOpt.get();
        String ipRemota = obtenerIPClienteGECA(request.getRemoteAddr());
        String ipPermitida = usuario.getIpPermitidaGeca();
        if (ipPermitida != null && !ipPermitida.trim().isEmpty() && !ipPermitida.equals(ipRemota)) {
            request.setAttribute("mensajeErrorGECA", "Acceso denegado desde esta dirección IP: " + ipRemota);
            generarCaptchaGECA(session, request);
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        session.setAttribute("usuarioGECA", usuario);
        session.setAttribute("rolGECA", usuario.getRolGeca());

        if ("Administrador".equalsIgnoreCase(usuario.getRolGeca())) {
            response.sendRedirect(request.getContextPath() + "/adminGECA");
        } else {
            response.sendRedirect(request.getContextPath() + "/reclamoGECA");
        }
    }

    private void generarCaptchaGECA(HttpSession session, HttpServletRequest request) {
        int numeroUno = random.nextInt(9) + 1;
        int numeroDos = random.nextInt(9) + 1;
        session.setAttribute("captchaResultadoGECA", numeroUno + numeroDos);
        request.setAttribute("captchaPreguntaGECA", numeroUno + " + " + numeroDos);
    }

    private String obtenerIPClienteGECA(String remoteAddr) {
        if (remoteAddr == null) {
            return "";
        }
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return "127.0.0.1";
        }
        return remoteAddr;
    }
}