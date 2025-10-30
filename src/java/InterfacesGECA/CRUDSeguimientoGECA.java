package InterfacesGECA;

import ModeloGECA.clsSeguimientoGECA;
import java.util.List;

public interface CRUDSeguimientoGECA {

    List<clsSeguimientoGECA> listarPorReclamoGECA(int idReclamo);

    boolean registrarSeguimientoGECA(clsSeguimientoGECA seguimiento);
}
