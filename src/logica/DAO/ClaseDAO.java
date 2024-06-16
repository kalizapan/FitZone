package logica.DAO;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logica.dominio.Clase;
import dataaccess.BaseDatosFitZone;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ClaseDAO {

    public boolean existeClase(String nombre, String fechaClase) throws SQLException {
        boolean existe = false;

        try (Connection conexion = BaseDatosFitZone.getConnection(); 
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                "SELECT COUNT(*) AS cantidad FROM Clase WHERE nombre = ? AND fechaClase = ?")) {
            sentenciaPreparada.setString(1, nombre);
            sentenciaPreparada.setString(2, fechaClase);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    int cantidad = resultSet.getInt("cantidad");
                    existe = cantidad > 0;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return existe;
    }
    
    public List<Clase> listarClasesActivas(){
        List<Clase> clasesActivas = new LinkedList<>();

        try (Connection conexion = BaseDatosFitZone.getConnection(); 
             PreparedStatement sentenciaPreparada = conexion.prepareStatement("SELECT * FROM Clase WHERE estado = ?")) {
            sentenciaPreparada.setInt(1, 1); 

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                while (resultSet.next()) {
                    Clase clase = new Clase(
                            resultSet.getInt("idClase"),
                            resultSet.getString("nombre"),
                            resultSet.getString("tipo"),
                            resultSet.getInt("capacidad"),
                            resultSet.getString("fechaClase"),
                            resultSet.getString("horaInicio"),
                            resultSet.getString("horaFin"),
                            resultSet.getInt("precio"),
                            resultSet.getInt("estado"),
                            resultSet.getInt("idEntrenador")
                    );
                    clasesActivas.add(clase);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clasesActivas;
    }

    public int registrarClase(Clase clase) throws SQLException, IllegalArgumentException {
        if (validarClase(clase)) {
            if (!existeClase(clase.getNombre(), clase.getFechaClase())) {
                int filasAfectadas = 0;
                try (Connection conexion = BaseDatosFitZone.getConnection(); 
                     PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                        "INSERT INTO Clase (nombre, tipo, capacidad, fechaClase, horaInicio, horaFin, precio, estado, idEntrenador)"
                                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                    sentenciaPreparada.setString(1, clase.getNombre());
                    sentenciaPreparada.setString(2, clase.getTipo());
                    sentenciaPreparada.setInt(3, clase.getCapacidad());
                    sentenciaPreparada.setString(4, clase.getFechaClase());
                    sentenciaPreparada.setString(5, clase.getHoraInicio());
                    sentenciaPreparada.setString(6, clase.getHoraFin());
                    sentenciaPreparada.setInt(7, clase.getPrecio());
                    sentenciaPreparada.setInt(8, clase.getEstado());
                    sentenciaPreparada.setInt(9, clase.getIdEntrenador());

                    filasAfectadas = sentenciaPreparada.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
                return filasAfectadas;
            } else {
                return 0;
            }
        } else {
            throw new IllegalArgumentException("Datos de clase no válidos.");
        }
    }

    public boolean validarClase(Clase clase) {
        return clase.getNombre() != null && clase.getCapacidad() > 0 && clase.getPrecio() > 0
                && clase.getIdEntrenador() > 0 && clase.getTipo() != null
                && clase.getFechaClase() != null && clase.getHoraInicio() != null && clase.getHoraFin() != null;
    }

    public int actualizarClase(Clase clase) throws SQLException { 
        if (validarClase(clase)) {
            try (Connection conexion = BaseDatosFitZone.getConnection();
                 PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                         "UPDATE clase SET nombre = ?, tipo = ?, capacidad = ?, horaInicio = ?, "
                                 + "horaFin = ?, precio = ?, fechaClase = ?, idEntrenador = ? WHERE idClase = ?")) {

                sentenciaPreparada.setString(1, clase.getNombre());
                sentenciaPreparada.setString(2, clase.getTipo());
                sentenciaPreparada.setInt(3, clase.getCapacidad());
                sentenciaPreparada.setString(4, clase.getHoraInicio());
                sentenciaPreparada.setString(5, clase.getHoraFin());
                sentenciaPreparada.setInt(6, clase.getPrecio());
                sentenciaPreparada.setString(7, clase.getFechaClase());
                sentenciaPreparada.setInt(8, clase.getIdEntrenador());
                sentenciaPreparada.setInt(9, clase.getIdClase());

                // Retorna el número de filas afectadas
                return sentenciaPreparada.executeUpdate();
            }
        }
        return 0; // Si la validación falla o no se actualiza nada, retorna 0
    }

    public String obtenerNombreClasePorId(int idClase) throws SQLException {
        if (idClase <= 0) {
            throw new IllegalArgumentException("ID de la clase no válido.");
        }

        String nombre = null;

        try (Connection conexion = BaseDatosFitZone.getConnection(); 
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                "SELECT nombre FROM Clase WHERE idClase = ? AND estado = 1")) {
            sentenciaPreparada.setInt(1, idClase);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    nombre = resultSet.getString("nombre");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return nombre;
    }

    public int cancelarClase(Clase clase) throws SQLException {
        int filasAfectadas = 0;

        try (Connection conexion = BaseDatosFitZone.getConnection(); 
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                "UPDATE Clase SET estado=? WHERE idClase=?")) {
            sentenciaPreparada.setInt(1, 0);
            sentenciaPreparada.setInt(2, clase.getIdClase());
            filasAfectadas = sentenciaPreparada.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return filasAfectadas;
    }

    public int obtenerIdClasePorNombre(String nombre) throws SQLException {
        int idClase = -1;

        try (Connection conexion = BaseDatosFitZone.getConnection(); 
             PreparedStatement sentenciaPreparada = conexion.prepareStatement(
                "SELECT idClase FROM Clase WHERE nombre = ?")) {
            sentenciaPreparada.setString(1, nombre);

            try (ResultSet resultSet = sentenciaPreparada.executeQuery()) {
                if (resultSet.next()) {
                    idClase = resultSet.getInt("idClase");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        return idClase;
    }

    public boolean esFechaPasada(String fechaClase) {
        try {
            LocalDate fecha = LocalDate.parse(fechaClase, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return fecha.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido.");
        }
    }
}