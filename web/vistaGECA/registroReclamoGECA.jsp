<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registrar Reclamo - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    </head>
    <body>
        <jsp:include page="/vistaGECA/segmentos/menuUsuarioGECA.jsp" />
        <div class="container py-4">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">Registrar nuevo reclamo</h5>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty mensajeErrorGECA}">
                                <div class="alert alert-danger">${mensajeErrorGECA}</div>
                            </c:if>
                            <form method="post" action="${pageContext.request.contextPath}/reclamoGECA">
                                <div class="mb-3">
                                    <label for="tituloReclamoGeca" class="form-label">Título del reclamo</label>
                                    <input type="text" id="tituloReclamoGeca" name="tituloReclamoGeca" class="form-control" required maxlength="200">
                                </div>
                                <div class="mb-3">
                                    <label for="idCategoriaGeca" class="form-label">Categoría</label>
                                    <select id="idCategoriaGeca" name="idCategoriaGeca" class="form-select" required>
                                        <option value="">Seleccione...</option>
                                        <c:forEach items="${categoriasGECA}" var="categoria">
                                            <option value="${categoria.idCategoriaGeca}">${categoria.nombreCategoriaGeca}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="prioridadGeca" class="form-label">Prioridad</label>
                                    <select id="prioridadGeca" name="prioridadGeca" class="form-select">
                                        <option value="Baja">Baja</option>
                                        <option value="Media" selected>Media</option>
                                        <option value="Alta">Alta</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="descripcionReclamoGeca" class="form-label">Descripción detallada</label>
                                    <textarea id="descripcionReclamoGeca" name="descripcionReclamoGeca" class="form-control" rows="5" required></textarea>
                                </div>
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success">Registrar reclamo</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
