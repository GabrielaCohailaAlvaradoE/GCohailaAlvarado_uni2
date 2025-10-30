package ConfigGECA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class clsConexionGECA {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/sistema_reclamos_geca?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el driver de MySQL", e);
        }
    }

    private clsConexionGECA() {
    }

    public static Connection getConnectionGECA() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean probarConexionGECA() {
        try (Connection testCon = getConnectionGECA()) {
            return testCon != null && !testCon.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }
}
