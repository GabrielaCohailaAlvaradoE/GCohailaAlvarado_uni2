
package ConfigGECA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class clsConexionGECA {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/nombre";
    private static final String USER = "root"; // Cambiar por tu usuario
    private static final String PASSWORD = ""; // Cambiar por tu contraseña
    
    public static Connection getConnectionGECA() {
        Connection connectionGECA = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connectionGECA = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en la conexión: " + e.getMessage());
        }
        return connectionGECA;
    }
    
    public static void closeConnectionGECA(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    public static boolean probarConexionGECA() {
        try (Connection testCon = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return testCon != null && !testCon.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar la conexión: " + e.getMessage());
            return false;
        }
    }
}
