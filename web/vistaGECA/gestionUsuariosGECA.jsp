<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de usuarios - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body>
        <jsp:include page="/vistaGECA/segmentos/menuAdminGECA.jsp" />
        <div class="container py-4">
            <div class="row g-4">
                <div class="col-lg-4">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><c:choose><c:when test="${not empty usuarioEditarGECA}">Editar usuario</c:when><c:otherwise>Registrar usuario</c:otherwise></c:choose></h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="${pageContext.request.contextPath}/usuarioGECA">
                                <input type="hidden" name="accion" value="${not empty usuarioEditarGECA ? 'actualizar' : 'guardar'}">
                                <c:if test="${not empty usuarioEditarGECA}">
                                    <input type="hidden" name="idUsuarioGeca" value="${usuarioEditarGECA.idUsuarioGeca}">
                                </c:if>
                                <div class="mb-3">
                                    <label class="form-label" for="nombreGeca">Nombre completo</label>
                                    <input type="text" id="nombreGeca" name="nombreGeca" class="form-control" required value="${usuarioEditarGECA.nombreGeca}">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="emailGeca">Correo electrónico</label>
                                    <input type="email" id="emailGeca" name="emailGeca" class="form-control" required value="${usuarioEditarGECA.emailGeca}">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="passwordGeca">Contraseña</label>
                                    <input type="text" id="passwordGeca" name="passwordGeca" class="form-control" required value="${usuarioEditarGECA.passwordGeca}">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="rolGeca">Rol</label>
                                    <select id="rolGeca" name="rolGeca" class="form-select" required>
                                        <option value="Usuario" ${usuarioEditarGECA.rolGeca eq 'Usuario' ? 'selected' : ''}>Usuario</option>
                                        <option value="Administrador" ${usuarioEditarGECA.rolGeca eq 'Administrador' ? 'selected' : ''}>Administrador</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="ipPermitidaGeca">IP permitida</label>
                                    <input type="text" id="ipPermitidaGeca" name="ipPermitidaGeca" class="form-control" placeholder="Ej: 192.168.1.100" value="${usuarioEditarGECA.ipPermitidaGeca}">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label" for="estadoGeca">Estado</label>
                                    <select id="estadoGeca" name="estadoGeca" class="form-select">
                                        <option value="Activo" ${usuarioEditarGECA.estadoGeca eq 'Activo' ? 'selected' : ''}>Activo</option>
                                        <option value="Inactivo" ${usuarioEditarGECA.estadoGeca eq 'Inactivo' ? 'selected' : ''}>Inactivo</option>
                                    </select>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success">${not empty usuarioEditarGECA ? 'Actualizar' : 'Registrar'}</button>
                                    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/usuarioGECA">Limpiar</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-8">
                    <c:if test="${not empty sessionScope.mensajeExitoGECA}">
                        <div class="alert alert-success">${sessionScope.mensajeExitoGECA}</div>
                        <c:remove var="mensajeExitoGECA" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.mensajeErrorGECA}">
                        <div class="alert alert-danger">${sessionScope.mensajeErrorGECA}</div>
                        <c:remove var="mensajeErrorGECA" scope="session" />
                    </c:if>
                    <div class="card shadow-sm">
                        <div class="card-header bg-secondary text-white">
                            <h5 class="mb-0">Usuarios registrados</h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-striped mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>#</th>
                                            <th>Nombre</th>
                                            <th>Correo</th>
                                            <th>Rol</th>
                                            <th>IP</th>
                                            <th>Estado</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${usuariosGECA}" var="usuario" varStatus="status">
                                            <tr>
                                                <td>${status.index + 1}</td>
                                                <td>${usuario.nombreGeca}</td>
                                                <td>${usuario.emailGeca}</td>
                                                <td>${usuario.rolGeca}</td>
                                                <td>${usuario.ipPermitidaGeca}</td>
                                                <td>${usuario.estadoGeca}</td>
                                                <td class="text-end">
                                                    <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/usuarioGECA?accion=editar&idUsuarioGeca=${usuario.idUsuarioGeca}">Editar</a>
                                                    <a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/usuarioGECA?accion=eliminar&idUsuarioGeca=${usuario.idUsuarioGeca}" onclick="return confirm('¿Eliminar usuario seleccionado?');">Eliminar</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty usuariosGECA}">
                                            <tr>
                                                <td colspan="7" class="text-center py-4">No existen usuarios registrados.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
