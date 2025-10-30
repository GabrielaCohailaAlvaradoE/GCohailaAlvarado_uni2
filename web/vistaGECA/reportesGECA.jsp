<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Reportes - GECA</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    </head>
    <body>
        <jsp:include page="/vistaGECA/segmentos/menuAdminGECA.jsp" />
        <div class="container py-4">
            <h4 class="mb-4">Resumen general de reclamos</h4>
            <div class="row g-4">
                <div class="col-lg-6">
                    <div class="card shadow-sm h-100">
                        <div class="card-header bg-primary text-white">
                            <h6 class="mb-0">Reclamos por estado</h6>
                        </div>
                        <div class="card-body">
                            <table class="table table-sm table-striped">
                                <thead>
                                    <tr>
                                        <th>Estado</th>
                                        <th class="text-end">Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${resumenEstadoGECA}" var="estado">
                                        <tr>
                                            <td>${estado.etiquetaGeca}</td>
                                            <td class="text-end">${estado.totalGeca}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty resumenEstadoGECA}">
                                        <tr>
                                            <td colspan="2" class="text-center">Sin datos disponibles</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card shadow-sm h-100">
                        <div class="card-header bg-secondary text-white">
                            <h6 class="mb-0">Reclamos por categoría</h6>
                        </div>
                        <div class="card-body">
                            <table class="table table-sm table-striped">
                                <thead>
                                    <tr>
                                        <th>Categoría</th>
                                        <th class="text-end">Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${resumenCategoriaGECA}" var="categoria">
                                        <tr>
                                            <td>${categoria.etiquetaGeca}</td>
                                            <td class="text-end">${categoria.totalGeca}</td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty resumenCategoriaGECA}">
                                        <tr>
                                            <td colspan="2" class="text-center">Sin datos disponibles</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card shadow-sm mt-4">
                <div class="card-header bg-info text-white">
                    <h6 class="mb-0">Gráfico de distribución por estado</h6>
                </div>
                <div class="card-body">
                    <canvas id="graficoEstadoGECA" height="120"></canvas>
                </div>
            </div>
        </div>
        <script>
            const datosEstadoGECA = [
                <c:forEach items="${resumenEstadoGECA}" var="estado" varStatus="status">
                {label: '${estado.etiquetaGeca}', total: ${estado.totalGeca}}${status.last ? '' : ','}
                </c:forEach>
            ];
            if (datosEstadoGECA.length > 0) {
                const ctx = document.getElementById('graficoEstadoGECA');
                const etiquetas = datosEstadoGECA.map(item => item.label);
                const datos = datosEstadoGECA.map(item => item.total);
                new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: etiquetas,
                        datasets: [{
                                label: 'Reclamos',
                                data: datos,
                                backgroundColor: ['#0d6efd', '#ffc107', '#198754', '#0dcaf0', '#6f42c1']
                            }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            }
        </script>
    </body>
</html>