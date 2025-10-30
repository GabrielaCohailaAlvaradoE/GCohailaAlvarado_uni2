package ConfigGECA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class clsConexionGECA {
    // Corregido: nombre de la base de datos según el script SQL
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_reclamos_geca";
    private static final String USER = "root"; // Cambiar por tu usuario
    private static final String PASSWORD = ""; // Cambiar por tu contraseña
    
    // Bloque estático para registrar el driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL registrado correctamente GECA");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al registrar el driver MySQL GECA: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el driver de la base de datos GECA", e);
        }
    }
    
    public static Connection getConnectionGECA() {
        Connection connectionGECA = null;
        try {
            connectionGECA = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos GECA");
        } catch (SQLException e) {
            System.err.println("Error en la conexión GECA: " + e.getMessage());
            e.printStackTrace();
        }
        return connectionGECA;
    }
    
    public static void closeConnectionGECA(Connection connectionGECA) {
        if (connectionGECA != null) {
            try {
                connectionGECA.close();
                System.out.println("Conexión cerrada GECA");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión GECA: " + e.getMessage());
            }
        }
    }
    
    public static boolean probarConexionGECA() {
        Connection testCon = null;
        try {
            testCon = getConnectionGECA();
            return testCon != null && !testCon.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar la conexión GECA: " + e.getMessage());
            return false;
        } finally {
            closeConnectionGECA(testCon);
        }
    }
    
    // Método adicional para obtener información de la conexión
    public static String getInfoConexionGECA() {
        return "Base de datos: sistema_reclamos_geca | URL: " + URL + " | Usuario: " + USER;
    }
}