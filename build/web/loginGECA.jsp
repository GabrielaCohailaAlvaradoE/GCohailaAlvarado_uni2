<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty requestScope.captchaPreguntaGECA and empty sessionScope.captchaPreguntaGECA}">
    <jsp:forward page="/loginGECA" />
</c:if>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ingreso - Sistema de Reclamos GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <style>
            body {
                min-height: 100vh;
                background: linear-gradient(135deg, #1e88e5, #43a047);
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 2rem 1rem;
            }
            .login-card {
                border: none;
                border-radius: 1rem;
                overflow: hidden;
            }
            .login-card .card-header {
                background: rgba(30, 136, 229, 0.9);
            }
            .login-card .form-control:focus {
                box-shadow: none;
                border-color: #1e88e5;
            }
            .brand-badge {
                font-size: 3rem;
                line-height: 1;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-5 col-md-7">
                    <div class="card shadow-lg login-card">
                        <div class="card-header text-white text-center py-4">
                            <div class="brand-badge mb-2">GECA</div>
                            <h4 class="mb-0">Sistema de Reclamos</h4>
                            <small class="d-block mt-1">Ingrese con sus credenciales corporativas</small>
                        </div>
                        <div class="card-body p-4 p-md-5">
                            <c:if test="${not empty mensajeErrorGECA}">
                                <div class="alert alert-danger" role="alert">
                                    ${mensajeErrorGECA}
                                </div>
                            </c:if>
                            <form method="post" action="${pageContext.request.contextPath}/loginGECA" class="needs-validation" novalidate>
                                <div class="mb-3">
                                    <label for="emailGeca" class="form-label">Correo electrónico</label>
                                    <input type="email" class="form-control" id="emailGeca" name="emailGeca" value="${empty emailIngresadoGECA ? '' : emailIngresadoGECA}" required>
                                    <div class="invalid-feedback">Ingrese un correo electrónico válido.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="passwordGeca" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="passwordGeca" name="passwordGeca" required>
                                    <div class="invalid-feedback">Ingrese su contraseña.</div>
                                </div>
                                <div class="mb-4">
                                    <label class="form-label">Captcha de verificación</label>
                                    <div class="input-group">
                                        <c:set var="captchaTextoGECA" value="${empty captchaPreguntaGECA ? sessionScope.captchaPreguntaGECA : captchaPreguntaGECA}" />
                                        <span class="input-group-text">
                                            <c:out value="${captchaTextoGECA}" default="?" />
                                        </span>
                                        <input type="number" class="form-control" name="captchaGeca" required placeholder="Resultado">
                                        <div class="invalid-feedback">Resuelva el captcha para continuar.</div>
                                    </div>
                                    <div class="form-text">Ingrese el resultado de la operación para validar que es una persona.</div>
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Ingresar al sistema</button>
                            </form>
                        </div>
                        <div class="card-footer text-center bg-light py-3">
                            <small class="text-muted d-block">Su dirección IP actual: ${pageContext.request.remoteAddr}</small>
                            <div class="text-muted small mt-2">
                                ¿Necesita ayuda? Contacte al administrador del sistema.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            (() => {
                const forms = document.querySelectorAll('.needs-validation');
                Array.prototype.slice.call(forms).forEach((form) => {
                    form.addEventListener('submit', (event) => {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                });
            })();
        </script>
    </body>
</html>
