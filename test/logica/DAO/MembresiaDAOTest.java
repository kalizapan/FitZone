package logica.DAO;

import java.util.List;
import logica.dominio.Membresia;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

/**
 *
 * @author Vero
 */
public class MembresiaDAOTest {

    private MembresiaDAO membresiaDAO;

    @Before
    public void setUp() {
        membresiaDAO = new MembresiaDAO();
    }

    @Test
    public void testListarMembresiasActivas() {
        List<Membresia> membresiasActivas = membresiaDAO.listarMembresiasActivas();
        assertNotNull(membresiasActivas);
        assertFalse(membresiasActivas.isEmpty());
    }

    @Test
    public void testExisteMembresiaExitoso() throws SQLException {
        String nombre = "Membresia Activa";

        boolean resultado = membresiaDAO.existeMembresia(nombre);
        assertTrue(resultado);
    }

    @Test
    public void testExisteMembresiaFallido() throws SQLException {
        String nombre = "Membresia Inexistente";

        boolean resultado = membresiaDAO.existeMembresia(nombre);
        assertFalse(resultado);
    }

    @Test //ya
    public void testRegistrarMembresiaExitoso() throws SQLException, IllegalArgumentException {
        Membresia membresia = new Membresia("Membresia", "1 añooo", 1);

        int filasAfectadas = membresiaDAO.registrarMembresia(membresia);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarMembresiaDatosInvalidos() throws SQLException, IllegalArgumentException {
        Membresia membresia = new Membresia(0, null, null, -1);
        membresiaDAO.registrarMembresia(membresia);
    }

    @Test
    public void testActualizarMembresiaExitoso() throws SQLException {
        Membresia membresia = new Membresia(1, "Membresia Actualizada", "2 años", 1);

        int filasAfectadas = membresiaDAO.actualizarMembresia(membresia);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActualizarMembresiaDatosInvalidos() throws SQLException {
        Membresia membresia = new Membresia(1, null, null, -1);
        membresiaDAO.actualizarMembresia(membresia);
    }

    @Test
    public void testDesactivarMembresiaExitoso() throws SQLException {
        int idMembresia = 1; 

        int filasAfectadas = membresiaDAO.desactivarMembresia(idMembresia);
        assertTrue(filasAfectadas > 0);
    }

    @Test
    public void testObtenerMembresiaPorIdExitoso() throws SQLException {
        int idMembresia = 1;

        Membresia membresia = membresiaDAO.obtenerMembresiaPorId(idMembresia);
        assertNotNull(membresia);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObtenerMembresiaPorIdInvalido() throws SQLException {
        int idMembresia = -1;
        membresiaDAO.obtenerMembresiaPorId(idMembresia);
    }
}