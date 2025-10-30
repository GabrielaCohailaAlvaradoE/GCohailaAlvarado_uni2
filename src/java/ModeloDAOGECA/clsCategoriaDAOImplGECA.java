package ModeloDAOGECA;

import ConfigGECA.clsConexionGECA;
import InterfacesGECA.CRUDCategoriaGECA;
import ModeloGECA.clsCategoriaGECA;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clsCategoriaDAOImplGECA implements CRUDCategoriaGECA {

    private static final String SQL_LISTAR = "SELECT id_categoria_geca, nombre_categoria_geca, descripcion_categoria_geca FROM CATEGORIAS_GECA ORDER BY nombre_categoria_geca";

    @Override
    public List<clsCategoriaGECA> listarCategoriasGECA() {
        List<clsCategoriaGECA> categorias = new ArrayList<>();
        try (Connection con = clsConexionGECA.getConnectionGECA();
                PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clsCategoriaGECA categoria = new clsCategoriaGECA();
                categoria.setIdCategoriaGeca(rs.getInt("id_categoria_geca"));
                categoria.setNombreCategoriaGeca(rs.getString("nombre_categoria_geca"));
                categoria.setDescripcionCategoriaGeca(rs.getString("descripcion_categoria_geca"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }
}
