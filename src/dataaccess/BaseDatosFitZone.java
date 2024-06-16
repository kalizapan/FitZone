package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BaseDatosFitZone {
    
    public static Connection getConnection() throws SQLException {
        String hostname = "localhost";
        String port = "3306";
        String database = "gymfitzone";
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";
        
        String user = "root";
        String password = "LOONA!ahgase2014";
        
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión OK");
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatosFitZone.class.getName()).log(Level.SEVERE, null, ex);
            // Si hay una excepción, cerramos la conexión antes de lanzarla
            if (conexion != null) {
                conexion.close();
            }
            throw ex;
        }
        return conexion;
    }
    
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(BaseDatosFitZone.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
