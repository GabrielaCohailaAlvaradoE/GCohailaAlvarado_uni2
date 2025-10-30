<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Mis reclamos - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body>
        <jsp:include page="/vistaGECA/segmentos/menuUsuarioGECA.jsp" />
        <div class="container py-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4>Mis reclamos registrados</h4>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/reclamoGECA?accion=nuevo">Registrar nuevo</a>
            </div>
            <c:if test="${not empty mensajeExitoGECA}">
                <div class="alert alert-success">${mensajeExitoGECA}</div>
            </c:if>
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Título</th>
                                    <th>Categoría</th>
                                    <th>Estado</th>
                                    <th>Prioridad</th>
                                    <th>Fecha</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${reclamosGECA}" var="reclamo" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${reclamo.tituloReclamoGeca}</td>
                                        <td><c:out value="${reclamo.nombreCategoriaGeca}" default="Sin categoría"/></td>
                                        <td>
                                            <span class="badge bg-${reclamo.estadoGeca eq 'Resuelto' ? 'success' : (reclamo.estadoGeca eq 'En atención' ? 'warning text-dark' : 'secondary')}">
                                                ${reclamo.estadoGeca}
                                            </span>
                                        </td>
                                        <td>${reclamo.prioridadGeca}</td>
                                        <td>
                                            <c:if test="${not empty reclamo.fechaCreacionGeca}">
                                                ${reclamo.fechaCreacionGeca}
                                            </c:if>
                                        </td>
                                        <td class="text-end">
                                            <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/reclamoGECA?accion=detalle&idReclamoGeca=${reclamo.idReclamoGeca}">Detalle</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty reclamosGECA}">
                                    <tr>
                                        <td colspan="7" class="text-center py-4">No tiene reclamos registrados.</td>
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
