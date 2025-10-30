package ModeloDAOGECA;

import ConfigGECA.clsConexionGECA;
import InterfacesGECA.CRUDSeguimientoGECA;
import ModeloGECA.clsSeguimientoGECA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class clsSeguimientoDAOImplGECA implements CRUDSeguimientoGECA {

    private static final String SQL_LISTAR = "SELECT s.*, u.nombre_geca FROM SEGUIMIENTO_GECA s INNER JOIN USUARIOS_GECA u ON s.id_usuario_geca = u.id_usuario_geca WHERE s.id_reclamo_geca = ? ORDER BY s.fecha_seguimiento_geca";
    private static final String SQL_INSERTAR = "INSERT INTO SEGUIMIENTO_GECA(id_reclamo_geca, id_usuario_geca, accion_geca, observaciones_geca, nuevo_estado_geca) VALUES(?,?,?,?,?)";

    @Override
    public List<clsSeguimientoGECA> listarPorReclamoGECA(int idReclamo) {
        List<clsSeguimientoGECA> seguimientos = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA(); PreparedStatement ps = con.prepareStatement(SQL_LISTAR)) {
            ps.setInt(1, idReclamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clsSeguimientoGECA seguimiento = mapearSeguimientoGECA(rs);
                    seguimiento.setNombreUsuarioGeca(rs.getString("nombre_geca"));
                    seguimientos.add(seguimiento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar seguimiento: " + e.getMessage());
        }
        return seguimientos;
    }

    @Override
    public boolean registrarSeguimientoGECA(clsSeguimientoGECA seguimiento) {
        try (Connection con = clsConexionGECA.getConnectionGECA()) {
            if (con == null) {
                return false;
            }
            return registrarSeguimiento(con, seguimiento);
        } catch (SQLException e) {
            System.err.println("Error al registrar seguimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarCambioEstadoGECA(Connection con, int idReclamo, int idUsuario, String nuevoEstado, String observaciones) throws SQLException {
        clsSeguimientoGECA seguimiento = construirCambioEstado(idReclamo, idUsuario, nuevoEstado, observaciones);
        return registrarSeguimiento(con, seguimiento);
    }

    public boolean registrarCambioEstadoGECA(int idReclamo, int idUsuario, String nuevoEstado, String observaciones) {
        try (Connection con = clsConexionGECA.getConnectionGECA()) {
            if (con == null) {
                return false;
            }
            return registrarCambioEstadoGECA(con, idReclamo, idUsuario, nuevoEstado, observaciones);
        } catch (SQLException e) {
            System.err.println("Error al registrar cambio de estado: " + e.getMessage());
            return false;
        }
    }

    private boolean registrarSeguimiento(Connection con, clsSeguimientoGECA seguimiento) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, seguimiento.getIdReclamoGeca());
            ps.setInt(2, seguimiento.getIdUsuarioGeca());
            ps.setString(3, seguimiento.getAccionGeca());
            ps.setString(4, seguimiento.getObservacionesGeca());
            ps.setString(5, seguimiento.getNuevoEstadoGeca());
            return ps.executeUpdate() > 0;
        }
    }

    private clsSeguimientoGECA construirCambioEstado(int idReclamo, int idUsuario, String nuevoEstado, String observaciones) {
        clsSeguimientoGECA seguimiento = new clsSeguimientoGECA();
        seguimiento.setIdReclamoGeca(idReclamo);
        seguimiento.setIdUsuarioGeca(idUsuario);
        seguimiento.setAccionGeca("Cambio de estado");
        seguimiento.setObservacionesGeca(limpiarObservacionesCambio(nuevoEstado, observaciones));
        seguimiento.setNuevoEstadoGeca(nuevoEstado);
        return seguimiento;
    }

    private String limpiarObservacionesCambio(String nuevoEstado, String observaciones) {
        if (observaciones != null) {
            String texto = observaciones.trim();
            if (!texto.isEmpty()) {
                return texto;
            }
        }
        if (nuevoEstado != null) {
            return "Estado actualizado a: " + nuevoEstado;
        }
        return null;
    }

    private clsSeguimientoGECA mapearSeguimientoGECA(ResultSet rs) throws SQLException {
        clsSeguimientoGECA seguimiento = new clsSeguimientoGECA();
        seguimiento.setIdSeguimientoGeca(rs.getInt("id_seguimiento_geca"));
        seguimiento.setIdReclamoGeca(rs.getInt("id_reclamo_geca"));
        seguimiento.setIdUsuarioGeca(rs.getInt("id_usuario_geca"));
        seguimiento.setAccionGeca(rs.getString("accion_geca"));
        seguimiento.setObservacionesGeca(rs.getString("observaciones_geca"));
        Timestamp fechaSeguimiento = rs.getTimestamp("fecha_seguimiento_geca");
        if (fechaSeguimiento != null) {
            seguimiento.setFechaSeguimientoGeca(fechaSeguimiento.toLocalDateTime());
        }
        seguimiento.setNuevoEstadoGeca(rs.getString("nuevo_estado_geca"));
        return seguimiento;
    }
}
