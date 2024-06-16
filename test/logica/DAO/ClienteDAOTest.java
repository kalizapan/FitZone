package logica.DAO;

import java.util.List;
import logica.DAO.ClienteDAO;
import logica.dominio.Cliente;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

/**
 *
 * @author Vero
 */
public class ClienteDAOTest {
    
    public ClienteDAOTest() {
    }
    
    private ClienteDAO clienteDAO;

    @Before
    public void setUp() {
        clienteDAO = new ClienteDAO();
    }

    @Test
    public void testListarClientesActivos() throws SQLException {
        List<Cliente> clientesActivos = clienteDAO.listarClientesActivos();
        assertNotNull(clientesActivos);
    }

    @Test
    public void testExisteClienteExitoso() throws SQLException {
        String correo = "correo@cliente.com";
        String numeroCelular = "123456789";

        boolean resultado = clienteDAO.existeCliente(correo, numeroCelular);
        assertTrue(resultado);
    }

    @Test
    public void testExisteClienteFallido() throws SQLException {
        String correo = "correo@noexistente.com";
        String numeroCelular = "987654321";

        boolean resultado = clienteDAO.existeCliente(correo, numeroCelular);
        assertFalse(resultado);
    }

    @Test
    public void testValidarCredencialesExitoso() {
        String correo = "correo@cliente.com";
        String contrasenia = "contraseña";

        boolean resultado = clienteDAO.validarCredenciales(correo, contrasenia);
        assertTrue(resultado);
    }

    @Test
    public void testValidarCredencialesFallido() {
        String correo = "correo@noexistente.com";
        String contrasenia = "contraseña";

        boolean resultado = clienteDAO.validarCredenciales(correo, contrasenia);
        assertFalse(resultado);
    }

    @Test
    public void testVerificarEstadoClienteActivo() {
        String correo = "correo@cliente.com";

        boolean resultado = clienteDAO.verificarEstadoCliente(correo);
        assertTrue(resultado);
    }

    @Test
    public void testVerificarEstadoClienteInactivo() {
        String correo = "correo@cliente-inactivo.com";

        boolean resultado = clienteDAO.verificarEstadoCliente(correo);
        assertFalse(resultado);
    }

    @Test //ya
    public void testRegistrarClienteExitoso() throws SQLException, IllegalArgumentException {
        Cliente cliente = new Cliente("Nombreaaa", "ApellidoPaaaaaterno", "ApellidoMaternoooo", "Direccion", 
                "correo@clienteeeee.com", "contrasenAA!ia987yhj", "123356789", 1);

        int filasAfectadas = clienteDAO.registrarCliente(cliente);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarClienteDatosInvalidos() throws SQLException, IllegalArgumentException {
        Cliente cliente = new Cliente(0, null, null, null, null, null, null, null, -1, 0);
        clienteDAO.registrarCliente(cliente);
    }

    @Test
    public void testActualizarClienteExitoso() throws SQLException {
        Cliente cliente = new Cliente(0, "Nombre", "ApellidoPaterno", "ApellidoMaterno", "Direccion", 
                "correo@cliente.com", "contrasenia987yhj", "123456789", 1, 1);

        int filasAfectadas = clienteDAO.actualizarCliente(cliente);
        assertTrue(filasAfectadas > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActualizarClienteDatosInvalidos() throws SQLException {
        Cliente cliente = new Cliente(1, null, null, null, null, null, null, null, -1, 0);
        clienteDAO.actualizarCliente(cliente);
    }

    @Test
    public void testDesactivarClienteExitoso() throws SQLException {
        int idCliente = 1;
        int filasAfectadas = clienteDAO.desactivarCliente(idCliente);
        assertTrue(filasAfectadas > 0);
    }

    @Test
    public void testObtenerClientePorIdExitoso() throws SQLException {
        int idCliente = 1;
        Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
        assertNotNull(cliente);
    }
    
}
