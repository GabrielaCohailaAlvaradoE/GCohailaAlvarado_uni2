<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Panel administrativo - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body>
        <jsp:include page="/vistaGECA/segmentos/menuAdminGECA.jsp" />
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4>Gestión de reclamos</h4>
                <form class="d-flex" method="get" action="${pageContext.request.contextPath}/adminGECA">
                    <select class="form-select me-2" name="estadoFiltroGeca">
                        <option value="Todos">Todos</option>
                        <option value="Pendiente" ${param.estadoFiltroGeca eq 'Pendiente' ? 'selected' : ''}>Pendiente</option>
                        <option value="En atención" ${param.estadoFiltroGeca eq 'En atención' ? 'selected' : ''}>En atención</option>
                        <option value="Resuelto" ${param.estadoFiltroGeca eq 'Resuelto' ? 'selected' : ''}>Resuelto</option>
                    </select>
                    <button class="btn btn-outline-secondary" type="submit">Filtrar</button>
                </form>
            </div>
            <c:if test="${not empty sessionScope.mensajeExitoGECA}">
                <div class="alert alert-success">${sessionScope.mensajeExitoGECA}</div>
                <c:remove var="mensajeExitoGECA" scope="session" />
            </c:if>
            <c:if test="${not empty sessionScope.mensajeErrorGECA}">
                <div class="alert alert-danger">${sessionScope.mensajeErrorGECA}</div>
                <c:remove var="mensajeErrorGECA" scope="session" />
            </c:if>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Título</th>
                                    <th>Usuario</th>
                                    <th>Categoría</th>
                                    <th>Estado</th>
                                    <th>Prioridad</th>
                                    <th>Creado</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${reclamosGECA}" var="reclamo" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${reclamo.tituloReclamoGeca}</td>
                                        <td>${reclamo.idUsuarioGeca}</td>
                                        <td><c:out value="${reclamo.nombreCategoriaGeca}" default="Sin categoría"/></td>
                                        <td>${reclamo.estadoGeca}</td>
                                        <td>${reclamo.prioridadGeca}</td>
                                        <td>${reclamo.fechaCreacionGeca}</td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/adminGECA?accion=detalle&idReclamoGeca=${reclamo.idReclamoGeca}">Gestionar</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty reclamosGECA}">
                                    <tr>
                                        <td colspan="8" class="text-center py-4">No se encontraron reclamos con el filtro aplicado.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
