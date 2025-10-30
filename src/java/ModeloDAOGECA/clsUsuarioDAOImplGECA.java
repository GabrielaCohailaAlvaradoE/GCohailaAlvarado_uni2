package ModeloDAOGECA;

import ConfigGECA.clsConexionGECA;
import InterfacesGECA.CRUDUsuarioGECA;
import ModeloGECA.clsUsuarioGeca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class clsUsuarioDAOImplGECA implements CRUDUsuarioGECA {

    private static final String SQL_VALIDAR = "SELECT * FROM USUARIOS_GECA WHERE email_geca = ? AND password_geca = ? AND estado_geca = 'Activo'";
    private static final String SQL_LISTAR = "SELECT * FROM USUARIOS_GECA ORDER BY nombre_geca";
    private static final String SQL_OBTENER_ID = "SELECT * FROM USUARIOS_GECA WHERE id_usuario_geca = ?";
    private static final String SQL_INSERTAR = "INSERT INTO USUARIOS_GECA(nombre_geca, email_geca, password_geca, rol_geca, ip_permitida_geca, estado_geca) VALUES(?,?,?,?,?,?)";
    private static final String SQL_ACTUALIZAR = "UPDATE USUARIOS_GECA SET nombre_geca=?, email_geca=?, password_geca=?, rol_geca=?, ip_permitida_geca=?, estado_geca=? WHERE id_usuario_geca=?";
    private static final String SQL_ELIMINAR = "DELETE FROM USUARIOS_GECA WHERE id_usuario_geca=?";

    @Override
    public Optional<clsUsuarioGeca> validarAccesoGECA(String emailGeca, String passwordGeca) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_VALIDAR)) {
            ps.setString(1, emailGeca);
            ps.setString(2, passwordGeca);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuarioGECA(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<clsUsuarioGeca> listarUsuariosGECA() {
        List<clsUsuarioGeca> usuarios = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapearUsuarioGECA(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public Optional<clsUsuarioGeca> obtenerUsuarioPorIdGECA(int idUsuario) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_OBTENER_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuarioGECA(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean registrarUsuarioGECA(clsUsuarioGeca usuario) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, usuario.getNombreGeca());
            ps.setString(2, usuario.getEmailGeca());
            ps.setString(3, usuario.getPasswordGeca());
            ps.setString(4, usuario.getRolGeca());
            ps.setString(5, usuario.getIpPermitidaGeca());
            ps.setString(6, usuario.getEstadoGeca());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizarUsuarioGECA(clsUsuarioGeca usuario) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, usuario.getNombreGeca());
            ps.setString(2, usuario.getEmailGeca());
            ps.setString(3, usuario.getPasswordGeca());
            ps.setString(4, usuario.getRolGeca());
            ps.setString(5, usuario.getIpPermitidaGeca());
            ps.setString(6, usuario.getEstadoGeca());
            ps.setInt(7, usuario.getIdUsuarioGeca());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarUsuarioGECA(int idUsuario) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    private clsUsuarioGeca mapearUsuarioGECA(ResultSet rs) throws SQLException {
        clsUsuarioGeca usuario = new clsUsuarioGeca();
        usuario.setIdUsuarioGeca(rs.getInt("id_usuario_geca"));
        usuario.setNombreGeca(rs.getString("nombre_geca"));
        usuario.setEmailGeca(rs.getString("email_geca"));
        usuario.setPasswordGeca(rs.getString("password_geca"));
        usuario.setRolGeca(rs.getString("rol_geca"));
        usuario.setIpPermitidaGeca(rs.getString("ip_permitida_geca"));
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro_geca");
        usuario.setFechaRegistroGeca(fechaRegistro != null ? fechaRegistro.toLocalDateTime() : LocalDateTime.now());
        usuario.setEstadoGeca(rs.getString("estado_geca"));
        return usuario;
    }
}
