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

        if ("logout".equalsIgnoreCase(request.getParameter("accion"))) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + LOGIN_JSP);
            return;
        }

        clsUsuarioGeca usuarioEnSesion = (clsUsuarioGeca) session.getAttribute("usuarioGECA");
        if (usuarioEnSesion != null) {
            redirigirSegunRol(response, request.getContextPath(), usuarioEnSesion.getRolGeca());
            return;
        }

        generarCaptchaGECA(session, request);
        request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = obtenerParametroLimpio(request, "emailGeca");
        String password = obtenerParametroLimpio(request, "passwordGeca");
        String captchaIngresado = obtenerParametroLimpio(request, "captchaGeca");

        if (email.isEmpty() || password.isEmpty()) {
            prepararRespuestaConError(request, session, email, "Debe ingresar su correo y contraseña.");
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        if (!validarCaptcha(session, captchaIngresado)) {
            prepararRespuestaConError(request, session, email, "Captcha inválido. Intente nuevamente.");
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        Optional<clsUsuarioGeca> usuarioOpt = usuarioDAO.validarAccesoGECA(email, password);
        if (!usuarioOpt.isPresent()) {
            prepararRespuestaConError(request, session, email, "Credenciales inválidas o usuario inactivo.");
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        clsUsuarioGeca usuario = usuarioOpt.get();
        String ipRemota = obtenerIPClienteGECA(request);
        String ipPermitida = usuario.getIpPermitidaGeca();
        if (!esIPRemotaPermitida(ipPermitida, ipRemota)) {
            prepararRespuestaConError(request, session, email, "Acceso denegado desde esta dirección IP: " + ipRemota);
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        session.setAttribute("usuarioGECA", usuario);
        session.setAttribute("rolGECA", usuario.getRolGeca());
        session.removeAttribute("captchaResultadoGECA");

        redirigirSegunRol(response, request.getContextPath(), usuario.getRolGeca());
    }

    private String obtenerParametroLimpio(HttpServletRequest request, String nombreParametro) {
        String valor = request.getParameter(nombreParametro);
        return valor != null ? valor.trim() : "";
    }

    private void prepararRespuestaConError(HttpServletRequest request, HttpSession session, String email, String mensajeError) {
        request.setAttribute("emailIngresadoGECA", email);
        request.setAttribute("mensajeErrorGECA", mensajeError);
        generarCaptchaGECA(session, request);
    }

    private void generarCaptchaGECA(HttpSession session, HttpServletRequest request) {
        int numeroUno = random.nextInt(9) + 1;
        int numeroDos = random.nextInt(9) + 1;
        session.setAttribute("captchaResultadoGECA", numeroUno + numeroDos);
        request.setAttribute("captchaPreguntaGECA", numeroUno + " + " + numeroDos);
    }

    private boolean validarCaptcha(HttpSession session, String captchaIngresado) {
        Object captchaSesion = session.getAttribute("captchaResultadoGECA");
        if (captchaSesion == null || captchaIngresado.isEmpty()) {
            return false;
        }
        try {
            int esperado = Integer.parseInt(captchaSesion.toString());
            int ingresado = Integer.parseInt(captchaIngresado);
            return ingresado == esperado;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private String obtenerIPClienteGECA(HttpServletRequest request) {
        String ipDesdeCabecera = obtenerIPDesdeCabecerasProxys(request);
        if (!ipDesdeCabecera.isEmpty()) {
            return ipDesdeCabecera;
        }
        String remoteAddr = Optional.ofNullable(request.getRemoteAddr()).orElse("");
        if (remoteAddr.isEmpty()) {
            return "";
        }
        return normalizarLoopback(remoteAddr);
    }

    private boolean esIPRemotaPermitida(String ipPermitida, String ipRemota) {
        if (ipPermitida == null || ipPermitida.trim().isEmpty()) {
            return true;
        }

        String ipNormalizadaRemota = normalizarIP(ipRemota);
        if (ipNormalizadaRemota.isEmpty()) {
            return false;
        }

        String[] ipPermitidas = ipPermitida.split("[,;\\s]+");
        for (String ip : ipPermitidas) {
            String ipNormalizada = normalizarIP(ip);
            if ("*".equals(ipNormalizada)) {
                return true;
            }
            if (!ipNormalizada.isEmpty() && ipNormalizada.equals(ipNormalizadaRemota)) {
                return true;
            }
        }
        return false;
    }

    private String normalizarIP(String ip) {
        if (ip == null) {
            return "";
        }
        String limpia = ip.trim();
        if (limpia.isEmpty()) {
            return "";
        }
        limpia = normalizarLoopback(limpia);
        return limpia.toLowerCase();
    }

    private String obtenerIPDesdeCabecerasProxys(HttpServletRequest request) {
        String[] cabeceras = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String cabecera : cabeceras) {
            String valor = request.getHeader(cabecera);
            if (valor != null && !valor.trim().isEmpty()) {
                String ip = valor.split(",")[0].trim();
                if (!ip.isEmpty()) {
                    return normalizarLoopback(ip);
                }
            }
        }
        return "";
    }

    private String normalizarLoopback(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip) || "localhost".equalsIgnoreCase(ip)) {
            return "127.0.0.1";
        }
        return ip;
    }

    private void redirigirSegunRol(HttpServletResponse response, String contextPath, String rol) throws IOException {
        if ("Administrador".equalsIgnoreCase(rol)) {
            response.sendRedirect(contextPath + "/adminGECA");
        } else {
            response.sendRedirect(contextPath + "/reclamoGECA");
        }
    }
}
