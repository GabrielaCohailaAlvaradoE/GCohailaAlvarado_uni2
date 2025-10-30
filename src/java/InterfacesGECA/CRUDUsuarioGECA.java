package InterfacesGECA;

import ModeloGECA.clsUsuarioGeca;
import java.util.List;
import java.util.Optional;

public interface CRUDUsuarioGECA {

    Optional<clsUsuarioGeca> validarAccesoGECA(String emailGeca, String passwordGeca);

    List<clsUsuarioGeca> listarUsuariosGECA();

    Optional<clsUsuarioGeca> obtenerUsuarioPorIdGECA(int idUsuario);

    boolean registrarUsuarioGECA(clsUsuarioGeca usuario);

    boolean actualizarUsuarioGECA(clsUsuarioGeca usuario);

    boolean eliminarUsuarioGECA(int idUsuario);
}
