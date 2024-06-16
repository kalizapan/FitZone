package logica.DAO;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import logica.DAO.EntrenadorDAO;
import logica.dominio.Entrenador;

public class EntrenadorDAOTest {

    private EntrenadorDAO entrenadorDAO;

    @Before
    public void setUp() {
        entrenadorDAO = new EntrenadorDAO();
    }

    @Test
    public void listarEntrenadoresActivo() {
        List<Entrenador> entrenadoresActivos = entrenadorDAO.listarEntrenadoresActivos();
        assertNotNull(entrenadoresActivos);
        assertFalse(entrenadoresActivos.isEmpty());
       
    }

    @Test
    public void existeEntrenador() throws SQLException {
        // Arrange
        String nombre = "Andy";
        String apellidoPaterno = "Diaz";
        String apellidoMaterno = "Regules";
        boolean existe = entrenadorDAO.existeEntrenador(nombre, apellidoPaterno, apellidoMaterno);
        assertTrue(existe);
    }

    @Test
    public void existeEntrenador_ConEntrenadorNoExistente() throws SQLException {
        String nombre = "Pedro";
        String apellidoPaterno = "García";
        String apellidoMaterno = "Martínez";
        boolean existe = entrenadorDAO.existeEntrenador(nombre, apellidoPaterno, apellidoMaterno);
        assertFalse(existe);
    }

    @Test
    public void registrarEntrenadorExitoso() throws SQLException, IllegalArgumentException {
        Entrenador entrenador = new Entrenador("Naomi", "Condado", "Viveros", "Indoor cycling");
        int filasAfectadas = entrenadorDAO.registrarEntrenador(entrenador);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registrarEntrenador_ConDatosInvalidos() throws SQLException, IllegalArgumentException {
        Entrenador entrenador = new Entrenador(null, null, null, null);
        entrenadorDAO.registrarEntrenador(entrenador);
    }

    @Test
    public void actualizarEntrenador_ConDatosValidos() throws SQLException {
        Entrenador entrenador = new Entrenador("Juan", "Pérez", "Gómez", "Yoga");
        int filasAfectadas = entrenadorDAO.actualizarEntrenador(entrenador);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void actualizarEntrenador_ConDatosInvalidos() throws SQLException {
        Entrenador entrenador = new Entrenador(null, null, null, null);
        entrenadorDAO.actualizarEntrenador(entrenador);
    }

    @Test
    public void desactivarEntrenador() throws SQLException {
        int idEntrenador = 1;
        int filasAfectadas = entrenadorDAO.desactivarEntrenador(idEntrenador);
        assertTrue(filasAfectadas > 0);
    }

    @Test
    public void obtenerEntrenadorPorId() throws SQLException {
        int idEntrenador = 1;
        Entrenador entrenador = entrenadorDAO.obtenerEntrenadorPorId(idEntrenador);

        assertNotNull(entrenador);
    }
}
