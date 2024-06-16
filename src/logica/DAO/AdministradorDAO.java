package logica.DAO;

import com.mysql.cj.xdevapi.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import dataaccess.BaseDatosFitZone;
import java.util.ArrayList;
import logica.dominio.Administrador;

public class AdministradorDAO {
    
    public List<Administrador> listarAdministradoresActivos() {
    List<Administrador> administradoresActivos = new ArrayList<>();

    try (Connection conexion = BaseDatosFitZone.getConnection();
         PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Administrador WHERE estado = ?")) {
        
        statement.setInt(1, 1);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Administrador administrador = new Administrador(
                        resultSet.getInt("idAdministrador"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellidos"),
                        resultSet.getString("area"),
                        resultSet.getString("correo"),
                        resultSet.getString("contrasenia"),
                        resultSet.getBoolean("estado")
                       
                );
                administradoresActivos.add(administrador);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return administradoresActivos;
}
    
    public boolean existeAdministrador(String correo) throws SQLException {
        boolean existe = false;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT COUNT(*) AS cantidad FROM Administrador WHERE correo = ?")) {
            sentenciaPreparada.setString(1, correo);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    existe = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return existe;
    }
    
    public boolean validarCredenciales(String correo, String contrasenia) {
        boolean credencialesValidas = false;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = BaseDatosFitZone.getConnection();
            PreparedStatement consulta = conexion.prepareStatement("SELECT contrasenia FROM Administrador WHERE correo = ?");
            consulta.setString(1, correo);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                String contraseniaAlmacenada = resultado.getString("contrasenia");
                credencialesValidas = contrasenia.equals(contraseniaAlmacenada);
            }
        } catch (SQLException e) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            BaseDatosFitZone.cerrarConexion(conexion);
        }

        return credencialesValidas;
    }
    
    public boolean verificarEstadoAdministrador(String correo) {
        boolean administradorActivo = false;
        ResultSet resultado = null;
        Connection conexion = null;

        try {
            conexion = BaseDatosFitZone.getConnection();
            PreparedStatement consulta = conexion.prepareStatement("SELECT estado FROM Administrador WHERE correo = ?");
            consulta.setString(1, correo);
            resultado = consulta.executeQuery();

            if (resultado.next()) {
                int estadoAdministrador = resultado.getInt("estado");
                administradorActivo = (estadoAdministrador == 1); 
            }
        } catch (SQLException e) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            BaseDatosFitZone.cerrarConexion(conexion);
        }

        return administradorActivo;
    }
    
    public int registrarAdministrador(Administrador administrador) throws SQLException, IllegalArgumentException {
    if (validarAdministrador(administrador)) {
        if (!existeAdministrador(administrador.getCorreo())) {
            int filasAfectadas = 0;
            try (Connection conexion = BaseDatosFitZone.getConnection();
                 PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                         "INSERT INTO Administrador (nombre, apellidos, area, correo, contrasenia, estado) VALUES (?, ?, ?, ?, ?, ?)")) {

                sentenciaPreparada.setString(1, administrador.getNombre());
                sentenciaPreparada.setString(2, administrador.getApellidos());
                sentenciaPreparada.setString(3, administrador.getArea());
                sentenciaPreparada.setString(4, administrador.getCorreo());
                sentenciaPreparada.setString(5, administrador.getContrasenia());
                sentenciaPreparada.setInt(6, 1);

                filasAfectadas = sentenciaPreparada.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return filasAfectadas;
        } else {
            return 0;
        }
    } else {
        throw new IllegalArgumentException("Datos del administrador no válidos.");
    }
}

    
    public boolean validarAdministrador(Administrador administrador) {
        return !(administrador.getNombre() == null || administrador.getApellidos() == null
                || administrador.getArea() == null || administrador.getCorreo() == null 
                || administrador.getContrasenia() == null);
    }
    
    public int actualizarAdministrador(Administrador administrador) throws SQLException {
        if (validarAdministrador(administrador)) {
            int filasAfectadas = 0;
            try (Connection conexion = BaseDatosFitZone.getConnection();
                 PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                         "UPDATE Cliente SET nombre=?, apellidos=?, area=?, correo=?, contrasenia=? WHERE idCliente=?")) {
                sentenciaPreparada.setString(1, administrador.getNombre());
                sentenciaPreparada.setString(2, administrador.getApellidos());
                sentenciaPreparada.setString(3, administrador.getArea());
                sentenciaPreparada.setString(4, administrador.getCorreo());
                sentenciaPreparada.setString(5, administrador.getContrasenia());
                sentenciaPreparada.setBoolean(6, administrador.getEstado());
                
                sentenciaPreparada.setInt(7, administrador.getIdAdministrador());

                filasAfectadas = sentenciaPreparada.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            return filasAfectadas;
        } else {
            throw new IllegalArgumentException("Datos del administrador no válidos.");
        }
    }
    
    public int desactivarAdministrador(int idAdministrador) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "UPDATE Administrador SET estado=0 WHERE idAdministrador=?")) {
            sentenciaPreparada.setInt(1, idAdministrador);
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filasAfectadas;
    }
    
     public Administrador obtenerAdministradorPorId(int idAdministrador) throws SQLException {
        Administrador administrador = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT * FROM Administrador WHERE idAdministrador = ?")) {
            sentenciaPreparada.setInt(1, idAdministrador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    administrador = new Administrador(
                            resultSet.getInt("idAdministrador"),
                            resultSet.getString("nombre"),
                            resultSet.getString("apellidos"),
                            resultSet.getString("area"),
                            resultSet.getString("correo"),
                            resultSet.getString("contrasenia"),
                            resultSet.getBoolean("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdministradorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return administrador;
    }
     
     public int obtenerIdAdministrador(String correo) throws SQLException {
        int idAdministrador = -1;

        String consulta = "SELECT idAdministrador FROM Administrador WHERE correo = ?";

        try (Connection conexion = BaseDatosFitZone.getConnection(); PreparedStatement sentencia = conexion.prepareStatement(consulta)) {

            sentencia.setString(1, correo);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    idAdministrador = resultado.getInt("idAdministrador");
                }
            }
        }

        return idAdministrador;
    }
    
}
