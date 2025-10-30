package InterfacesGECA;

import ModeloGECA.clsReclamoGECA;
import ModeloGECA.clsnombreGECA;
import java.util.List;
import java.util.Optional;

public interface CRUDReclamoGECA {

    boolean registrarReclamoGECA(clsReclamoGECA reclamo);

    List<clsReclamoGECA> listarReclamosPorUsuarioGECA(int idUsuario);

    List<clsReclamoGECA> listarReclamosPorEstadoGECA(String estado);

    List<clsReclamoGECA> listarTodosGECA();

    Optional<clsReclamoGECA> obtenerReclamoPorIdGECA(int idReclamo);

    boolean actualizarEstadoGECA(int idReclamo, String nuevoEstado, int idUsuario, String observaciones);

    List<clsnombreGECA> obtenerResumenPorEstadoGECA();

    List<clsnombreGECA> obtenerResumenPorCategoriaGECA();
}