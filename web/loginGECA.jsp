<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Ingreso - Sistema de Reclamos GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body class="bg-light">
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white text-center">
                            <h4 class="mb-0">Sistema de Reclamos GECA</h4>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty mensajeErrorGECA}">
                                <div class="alert alert-danger" role="alert">
                                    ${mensajeErrorGECA}
                                </div>
                            </c:if>
                            <form method="post" action="${pageContext.request.contextPath}/loginGECA">
                                <div class="mb-3">
                                    <label for="emailGeca" class="form-label">Correo electrónico</label>
                                    <input type="email" class="form-control" id="emailGeca" name="emailGeca" required>
                                </div>
                                <div class="mb-3">
                                    <label for="passwordGeca" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="passwordGeca" name="passwordGeca" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Captcha de verificación</label>
                                    <div class="input-group">
                                        <span class="input-group-text">${captchaPreguntaGECA}</span>
                                        <input type="number" class="form-control" name="captchaGeca" required placeholder="Resultado">
                                    </div>
                                    <div class="form-text">Ingrese el resultado de la operación para continuar.</div>
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Ingresar</button>
                            </form>
                        </div>
                        <div class="card-footer text-center text-muted">
                            <small>Su dirección IP actual: ${pageContext.request.remoteAddr}</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
