package logica.DAO;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import logica.DAO.ClaseDAO;
import logica.dominio.Clase;

public class ClaseDAOTest {

    private ClaseDAO claseDAO;

    @Before
    public void setUp() {
        claseDAO = new ClaseDAO();
    }

    @Test
    public void listarClasesActivas() throws SQLException {
        List<Clase> clasesActivas = claseDAO.listarClasesActivas();
        assertNotNull(clasesActivas);
        assertFalse(clasesActivas.isEmpty());
    }

    @Test
    public void existeClase_ConClaseExistente() throws SQLException {
        String nombre = "Yoga";
        String fechaClase = "2024-05-25";
        boolean existe = claseDAO.existeClase(nombre, fechaClase);
        assertTrue(existe);
    }

    @Test
    public void existeClase_ConClaseNoExistente() throws SQLException {
        String nombre = "Pilates";
        String fechaClase = "2024-06-01";
        boolean existe = claseDAO.existeClase(nombre, fechaClase);
        assertFalse(existe);
    }

    @Test
    public void registrarClase() throws SQLException, IllegalArgumentException {
        Clase clase = new Clase(1, "Natación", "Acuática", 20, "2024-06-15", "09:00", "10:00", 10.5f, 1, 1);
        int filasAfectadas = claseDAO.registrarClase(clase);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registrarClase_ConDatosInvalidos() throws SQLException, IllegalArgumentException {
        Clase clase = new Clase(0, null, null, 0, null, null, null, 0, 0, 0);
        claseDAO.registrarClase(clase);
    }

    @Test
    public void actualizarClase_ConDatosValidos() throws SQLException {
        Clase clase = new Clase(1, "Yoga Avanzado", "Yoga", 15, "2024-05-25", "10:00", "12:00", 15.0f, 1, 2);
        int filasAfectadas = claseDAO.actualizarClase(clase);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void actualizarClase_ConDatosInvalidos() throws SQLException {
        Clase clase = new Clase(1, null, null, 0, null, null, null, 0, 0, 0);
        claseDAO.actualizarClase(clase);
    }

    @Test
    public void cancelarClase() throws SQLException {
        Clase clase = new Clase();
        clase.setIdClase(1);
        int filasAfectadas = claseDAO.cancelarClase(clase);
        assertTrue(filasAfectadas > 0);
    }

    @Test
    public void obtenerNombreClasePorId() throws SQLException {
        int idClase = 1;
        String nombre = claseDAO.obtenerNombreClasePorId(idClase);
        assertNotNull(nombre);
    }

    @Test
    public void obtenerIdClasePorNombre() throws SQLException {
        String nombre = "Yoga";
        int idClase = claseDAO.obtenerIdClasePorNombre(nombre);
        assertTrue(idClase > 0);
    }
}
