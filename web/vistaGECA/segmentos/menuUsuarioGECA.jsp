<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/reclamoGECA?accion=nuevo">GECA - Reclamos</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarUsuarioGeca" aria-controls="navbarUsuarioGeca" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarUsuarioGeca">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/reclamoGECA?accion=nuevo">Registrar</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/reclamoGECA?accion=listar">Mis reclamos</a>
                </li>
            </ul>
            <span class="navbar-text me-3 text-white-50">${sessionScope.usuarioGECA.nombreGeca}</span>
            <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/loginGECA?accion=logout">Salir</a>
        </div>
    </div>
</nav>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>