package ModeloDAOGECA;

import ConfigGECA.clsConexionGECA;
import InterfacesGECA.CRUDReclamoGECA;
import ModeloGECA.clsReclamoGECA;
import ModeloGECA.clsnombreGECA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class clsReclamoDAOImplGECA implements CRUDReclamoGECA {

    private static final String SQL_INSERTAR = "INSERT INTO RECLAMOS_GECA(id_usuario_geca, id_categoria_geca, titulo_reclamo_geca, descripcion_reclamo_geca, estado_geca, prioridad_geca) VALUES(?,?,?,?,?,?)";
    private static final String SQL_LISTAR_USUARIO = "SELECT r.*, c.nombre_categoria_geca FROM RECLAMOS_GECA r LEFT JOIN CATEGORIAS_GECA c ON r.id_categoria_geca = c.id_categoria_geca WHERE r.id_usuario_geca = ? ORDER BY r.fecha_creacion_geca DESC";
    private static final String SQL_LISTAR_ESTADO = "SELECT r.*, c.nombre_categoria_geca FROM RECLAMOS_GECA r LEFT JOIN CATEGORIAS_GECA c ON r.id_categoria_geca = c.id_categoria_geca WHERE r.estado_geca = ? ORDER BY r.fecha_creacion_geca";
    private static final String SQL_LISTAR_TODOS = "SELECT r.*, c.nombre_categoria_geca FROM RECLAMOS_GECA r LEFT JOIN CATEGORIAS_GECA c ON r.id_categoria_geca = c.id_categoria_geca ORDER BY r.fecha_creacion_geca DESC";
    private static final String SQL_OBTENER_ID = "SELECT r.*, c.nombre_categoria_geca FROM RECLAMOS_GECA r LEFT JOIN CATEGORIAS_GECA c ON r.id_categoria_geca = c.id_categoria_geca WHERE r.id_reclamo_geca = ?";
    private static final String SQL_ACTUALIZAR_ESTADO = "UPDATE RECLAMOS_GECA SET estado_geca=?, fecha_actualizacion_geca=CURRENT_TIMESTAMP WHERE id_reclamo_geca=?";
    private static final String SQL_RESUMEN_ESTADO = "SELECT estado_geca AS etiqueta, COUNT(*) AS total FROM RECLAMOS_GECA GROUP BY estado_geca";
    private static final String SQL_RESUMEN_CATEGORIA = "SELECT COALESCE(c.nombre_categoria_geca, 'Sin categoría') AS etiqueta, COUNT(*) AS total FROM RECLAMOS_GECA r LEFT JOIN CATEGORIAS_GECA c ON r.id_categoria_geca = c.id_categoria_geca GROUP BY etiqueta";

    private final clsSeguimientoDAOImplGECA seguimientoDAO = new clsSeguimientoDAOImplGECA();

    @Override
    public boolean registrarReclamoGECA(clsReclamoGECA reclamo) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, reclamo.getIdUsuarioGeca());
            if (reclamo.getIdCategoriaGeca() != null) {
                ps.setInt(2, reclamo.getIdCategoriaGeca());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, reclamo.getTituloReclamoGeca());
            ps.setString(4, reclamo.getDescripcionReclamoGeca());
            ps.setString(5, reclamo.getEstadoGeca());
            ps.setString(6, reclamo.getPrioridadGeca());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar reclamo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<clsReclamoGECA> listarReclamosPorUsuarioGECA(int idUsuario) {
        List<clsReclamoGECA> reclamos = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_USUARIO)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamoGECA(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por usuario: " + e.getMessage());
        }
        return reclamos;
    }

    @Override
    public List<clsReclamoGECA> listarReclamosPorEstadoGECA(String estado) {
        List<clsReclamoGECA> reclamos = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_ESTADO)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reclamos.add(mapearReclamoGECA(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos por estado: " + e.getMessage());
        }
        return reclamos;
    }

    @Override
    public List<clsReclamoGECA> listarTodosGECA() {
        List<clsReclamoGECA> reclamos = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reclamos.add(mapearReclamoGECA(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reclamos: " + e.getMessage());
        }
        return reclamos;
    }

    @Override
    public Optional<clsReclamoGECA> obtenerReclamoPorIdGECA(int idReclamo) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_OBTENER_ID)) {
            ps.setInt(1, idReclamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearReclamoGECA(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reclamo: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean actualizarEstadoGECA(int idReclamo, String nuevoEstado, int idUsuario, String observaciones) {
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR_ESTADO)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReclamo);
            if (ps.executeUpdate() > 0) {
                seguimientoDAO.registrarCambioEstadoGECA(idReclamo, idUsuario, nuevoEstado, observaciones);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<clsnombreGECA> obtenerResumenPorEstadoGECA() {
        return obtenerResumen(SQL_RESUMEN_ESTADO);
    }

    @Override
    public List<clsnombreGECA> obtenerResumenPorCategoriaGECA() {
        return obtenerResumen(SQL_RESUMEN_CATEGORIA);
    }

    private List<clsnombreGECA> obtenerResumen(String sql) {
        List<clsnombreGECA> resumen = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsnombreGECA dato = new clsnombreGECA();
                dato.setEtiquetaGeca(rs.getString("etiqueta"));
                dato.setTotalGeca(rs.getLong("total"));
                resumen.add(dato);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener resumen: " + e.getMessage());
        }
        return resumen;
    }

    private clsReclamoGECA mapearReclamoGECA(ResultSet rs) throws SQLException {
        clsReclamoGECA reclamo = new clsReclamoGECA();
        reclamo.setIdReclamoGeca(rs.getInt("id_reclamo_geca"));
        reclamo.setIdUsuarioGeca(rs.getInt("id_usuario_geca"));
        int categoria = rs.getInt("id_categoria_geca");
        reclamo.setIdCategoriaGeca(rs.wasNull() ? null : categoria);
        reclamo.setTituloReclamoGeca(rs.getString("titulo_reclamo_geca"));
        reclamo.setDescripcionReclamoGeca(rs.getString("descripcion_reclamo_geca"));
        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion_geca");
        if (fechaCreacion != null) {
            reclamo.setFechaCreacionGeca(fechaCreacion.toLocalDateTime());
        }
        Timestamp fechaActualizacion = rs.getTimestamp("fecha_actualizacion_geca");
        if (fechaActualizacion != null) {
            reclamo.setFechaActualizacionGeca(fechaActualizacion.toLocalDateTime());
        }
        reclamo.setEstadoGeca(rs.getString("estado_geca"));
        reclamo.setPrioridadGeca(rs.getString("prioridad_geca"));
        reclamo.setNombreCategoriaGeca(rs.getString("nombre_categoria_geca"));
        return reclamo;
    }
}
