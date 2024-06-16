package logica.DAO;

import dataaccess.BaseDatosFitZone;
import logica.dominio.Entrenador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntrenadorDAO {

    public List<Entrenador> listarEntrenadoresActivos() {
        List<Entrenador> entrenadoresActivos = new ArrayList<>();

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement statement = conexion.prepareStatement("SELECT * FROM Entrenador WHERE estado = ?")) {

            statement.setBoolean(1, true);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Entrenador entrenador = new Entrenador(
                            resultSet.getInt("idEntrenador"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidoPaterno"),
                            resultSet.getString("apellidoMaterno"),
                            resultSet.getString("especialidad"),
                            resultSet.getBoolean("estado")
                    );
                    entrenadoresActivos.add(entrenador);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return entrenadoresActivos;
    }


    public boolean existeEntrenador(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException {
        boolean existe = false;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT COUNT(*) AS cantidad FROM Entrenador "
                     + "WHERE nombres = ? AND apellidoPaterno = ? AND apellidoMaterno = ?")) {
            sentenciaPreparada.setString(1, nombre);
            sentenciaPreparada.setString(2, apellidoPaterno);
            sentenciaPreparada.setString(3, apellidoMaterno);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    existe = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return existe;
    }


    public int registrarEntrenador(Entrenador entrenador) throws SQLException, IllegalArgumentException {
        if (validarEntrenador(entrenador)) {
            if (!existeEntrenador(entrenador.getNombre(), entrenador.getApellidoPaterno(),
                    entrenador.getApellidoMaterno())) {
                int filasAfectadas = 0;
                try (Connection conexion = BaseDatosFitZone.getConnection();
                     PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                             "INSERT INTO Entrenador (nombres, apellidoPaterno, apellidoMaterno, especialidad, estado) "
                                     + "VALUES (?, ?, ?, ?, ?)")) {

                    sentenciaPreparada.setString(1, entrenador.getNombre());
                    sentenciaPreparada.setString(2, entrenador.getApellidoPaterno());
                    sentenciaPreparada.setString(3, entrenador.getApellidoMaterno());
                    sentenciaPreparada.setString(4, entrenador.getEspecialidad());
                    sentenciaPreparada.setBoolean(5, true);

                    filasAfectadas = sentenciaPreparada.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                return filasAfectadas;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("Datos del entrenador no válidos.");
        }
    }



    public boolean validarEntrenador(Entrenador entrenador) {
        return !(entrenador.getNombre() == null || entrenador.getApellidoPaterno() == null
                || entrenador.getApellidoMaterno() == null || entrenador.getEspecialidad() == null);
    }

    public int actualizarEntrenador(Entrenador entrenador) throws SQLException {
        if (validarEntrenador(entrenador)) {
            int filasAfectadas = 0;
            try (Connection conexion = BaseDatosFitZone.getConnection();
                 PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                         "UPDATE Entrenador "
                         + "SET nombres=?, apellidoPaterno=?, apellidoMaterno=?, especialidad=?, estado=? "
                         + "WHERE idEntrenador=?")) {
                sentenciaPreparada.setString(1, entrenador.getNombre());
                sentenciaPreparada.setString(2, entrenador.getApellidoPaterno());
                sentenciaPreparada.setString(3, entrenador.getApellidoMaterno());
                sentenciaPreparada.setString(4, entrenador.getEspecialidad());
                sentenciaPreparada.setBoolean(5, entrenador.getEstado());
                sentenciaPreparada.setInt(6, entrenador.getIdEntrenador());

                filasAfectadas = sentenciaPreparada.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            return filasAfectadas;
        } else {
            throw new IllegalArgumentException("Datos del entrenador no válidos.");
        }
    }


    public int desactivarEntrenador(int idEntrenador) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "UPDATE Entrenador SET estado=0 WHERE idEntrenador=?")) {
            sentenciaPreparada.setInt(1, idEntrenador);
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return filasAfectadas;
    }


   public Entrenador obtenerEntrenadorPorId(int idEntrenador) throws SQLException {
        Entrenador entrenador = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT * FROM Entrenador WHERE idEntrenador = ?")) {
            sentenciaPreparada.setInt(1, idEntrenador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    entrenador = new Entrenador(
                            resultSet.getInt("idEntrenador"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidoPaterno"),
                            resultSet.getString("apellidoMaterno"),
                            resultSet.getString("especialidad"),
                            resultSet.getBoolean("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return entrenador;
    }

    
    public Entrenador obtenerEntrenadorPorNombre(String nombreEntrenador) throws SQLException {
        Entrenador entrenador = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT * FROM Entrenador WHERE nombres = ?")) {
            sentenciaPreparada.setString(1, nombreEntrenador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    entrenador = new Entrenador(
                            resultSet.getInt("idEntrenador"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidoPaterno"),
                            resultSet.getString("apellidoMaterno"),
                            resultSet.getString("especialidad"),
                            resultSet.getBoolean("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return entrenador;
    }

    public int obtenerIdEntrenadorPorNombre(String nombreEntrenador) throws SQLException {
        int idEntrenador = -1;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT idEntrenador FROM Entrenador WHERE nombres = ?")) {
            sentenciaPreparada.setString(1, nombreEntrenador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    idEntrenador = resultSet.getInt("idEntrenador");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return idEntrenador;
    }

    public String obtenerNombreEntrenadorPorId(int idEntrenador) throws SQLException {
        String nombreEntrenador = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                    "SELECT nombres FROM Entrenador WHERE idEntrenador = ?")) {
            sentenciaPreparada.setInt(1, idEntrenador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    nombreEntrenador = resultSet.getString("nombres");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return nombreEntrenador;
    }
    
    public String obtenerFechaIngresoPorId(int idEntrenador) throws SQLException {
        String fechaIngreso = null;

        try (Connection conexion = BaseDatosFitZone.getConnection();
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                     "SELECT fechaIngreso FROM Entrenador WHERE idEntrenador = ?")) {
            sentenciaPreparada.setInt(1, idEntrenador);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    fechaIngreso = resultSet.getString("fechaIngreso");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EntrenadorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return fechaIngreso;
    }


}