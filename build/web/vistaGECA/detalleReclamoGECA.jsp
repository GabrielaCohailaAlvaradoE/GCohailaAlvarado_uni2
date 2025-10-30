<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Detalle del reclamo - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body>
        <c:set var="rolActualGECA" value="${sessionScope.rolGECA}" />
        <c:choose>
            <c:when test="${rolActualGECA eq 'Administrador'}">
                <jsp:include page="/vistaGECA/segmentos/menuAdminGECA.jsp" />
            </c:when>
            <c:otherwise>
                <jsp:include page="/vistaGECA/segmentos/menuUsuarioGECA.jsp" />
            </c:otherwise>
        </c:choose>
        <div class="container py-4">
            <div class="row">
                <div class="col-lg-8">
                    <div class="card mb-4 shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">${reclamoDetalleGECA.tituloReclamoGeca}</h5>
                        </div>
                        <div class="card-body">
                            <dl class="row">
                                <dt class="col-sm-4">Categoría</dt>
                                <dd class="col-sm-8"><c:out value="${reclamoDetalleGECA.nombreCategoriaGeca}" default="Sin categoría"/></dd>
                                <dt class="col-sm-4">Estado</dt>
                                <dd class="col-sm-8">
                                    <span class="badge bg-${reclamoDetalleGECA.estadoGeca eq 'Resuelto' ? 'success' : (reclamoDetalleGECA.estadoGeca eq 'En atención' ? 'warning text-dark' : 'secondary')}">
                                        ${reclamoDetalleGECA.estadoGeca}
                                    </span>
                                </dd>
                                <dt class="col-sm-4">Prioridad</dt>
                                <dd class="col-sm-8">${reclamoDetalleGECA.prioridadGeca}</dd>
                                <dt class="col-sm-4">Fecha creación</dt>
                                <dd class="col-sm-8">${reclamoDetalleGECA.fechaCreacionGeca}</dd>
                                <dt class="col-sm-4">Última actualización</dt>
                                <dd class="col-sm-8">${reclamoDetalleGECA.fechaActualizacionGeca}</dd>
                            </dl>
                            <hr>
                            <h6>Descripción</h6>
                            <p>${reclamoDetalleGECA.descripcionReclamoGeca}</p>
                        </div>
                    </div>
                    <div class="card shadow-sm">
                        <div class="card-header bg-secondary text-white">
                            <h6 class="mb-0">Historial de seguimiento</h6>
                        </div>
                        <div class="card-body">
                            <c:if test="${empty seguimientosGECA}">
                                <p class="text-muted">No existen seguimientos registrados.</p>
                            </c:if>
                            <c:forEach items="${seguimientosGECA}" var="seguimiento">
                                <div class="border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between">
                                        <strong>${seguimiento.accionGeca}</strong>
                                        <span class="text-muted small">${seguimiento.fechaSeguimientoGeca}</span>
                                    </div>
                                    <p class="mb-1">${seguimiento.observacionesGeca}</p>
                                    <small class="text-muted">Estado: ${seguimiento.nuevoEstadoGeca} | Responsable: ${seguimiento.nombreUsuarioGeca}</small>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <c:if test="${rolActualGECA eq 'Administrador'}">
                    <div class="col-lg-4">
                        <div class="card shadow-sm mb-4">
                            <div class="card-header bg-dark text-white">
                                <h6 class="mb-0">Actualizar estado</h6>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/adminGECA">
                                    <input type="hidden" name="accion" value="actualizarEstado">
                                    <input type="hidden" name="idReclamoGeca" value="${reclamoDetalleGECA.idReclamoGeca}">
                                    <div class="mb-3">
                                        <label class="form-label" for="nuevoEstadoGeca">Nuevo estado</label>
                                        <select class="form-select" id="nuevoEstadoGeca" name="nuevoEstadoGeca" required>
                                            <option value="Pendiente" ${reclamoDetalleGECA.estadoGeca eq 'Pendiente' ? 'selected' : ''}>Pendiente</option>
                                            <option value="En atención" ${reclamoDetalleGECA.estadoGeca eq 'En atención' ? 'selected' : ''}>En atención</option>
                                            <option value="Resuelto" ${reclamoDetalleGECA.estadoGeca eq 'Resuelto' ? 'selected' : ''}>Resuelto</option>
                                        </select>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label" for="observacionesEstado">Observaciones</label>
                                        <textarea id="observacionesEstado" name="observacionesGeca" class="form-control" rows="3" placeholder="Detalle la gestión realizada"></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary w-100">Actualizar</button>
                                </form>
                            </div>
                        </div>
                        <div class="card shadow-sm">
                            <div class="card-header bg-info text-white">
                                <h6 class="mb-0">Agregar seguimiento</h6>
                            </div>
                            <div class="card-body">
                                <form method="post" action="${pageContext.request.contextPath}/adminGECA">
                                    <input type="hidden" name="accion" value="registrarSeguimiento">
                                    <input type="hidden" name="idReclamoGeca" value="${reclamoDetalleGECA.idReclamoGeca}">
                                    <div class="mb-3">
                                        <label class="form-label" for="accionReclamoGeca">Acción realizada</label>
                                        <input type="text" class="form-control" id="accionReclamoGeca" name="accionReclamoGeca" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label" for="observacionesSeguimiento">Observaciones</label>
                                        <textarea class="form-control" id="observacionesSeguimiento" name="observacionesGeca" rows="3" required></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label" for="estadoSeguimientoGeca">Estado asociado</label>
                                        <select class="form-select" id="estadoSeguimientoGeca" name="estadoSeguimientoGeca">
                                            <option value="Pendiente">Pendiente</option>
                                            <option value="En atención">En atención</option>
                                            <option value="Resuelto">Resuelto</option>
                                        </select>
                                    </div>
                                    <button type="submit" class="btn btn-success w-100">Registrar seguimiento</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </body>
</html>