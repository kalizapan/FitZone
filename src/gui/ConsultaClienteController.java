package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logica.DAO.ClienteDAO;
import logica.dominio.Cliente;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ConsultaClienteController implements Initializable {
    private AdministradorController administradorController;

    @FXML
    private Label nombreLabel;
    @FXML
    private Label apellidoPaternoLabel;
    @FXML
    private Label apellidoMaternoLabel;
    @FXML
    private Label correoLabel;
    @FXML
    private Label direccionLabel;
    @FXML
    private Label celularLabel;
    @FXML
    private Label membresiaLabel;
    @FXML
    private Button buttonDarBaja;
    @FXML
    private Button buttonVerPagos;
    @FXML
    private Button buttonVerClases;

    private Stage dialogStage;
    private Cliente cliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void initData(Cliente cliente) {
        this.cliente = cliente;
        mostrarDetallesCliente();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAdministradorController(AdministradorController administradorController) {
        this.administradorController = administradorController;
    }

    @FXML
    private void mostrarDetallesCliente() {
        if (cliente != null) {
            nombreLabel.setText(cliente.getNombre());
            apellidoPaternoLabel.setText(cliente.getApellidoPaterno());
            apellidoMaternoLabel.setText(cliente.getApellidoMaterno());
            correoLabel.setText(cliente.getCorreo());
            direccionLabel.setText(cliente.getDireccion());
            celularLabel.setText(cliente.getNumeroCelular());
            membresiaLabel.setText(cliente.getMembresia());
        } else {
            System.out.println("Error: El cliente es nulo en mostrarDetallesCliente.");
        }
    }

    @FXML
    private void darBajaCliente() throws SQLException {
        if (cliente == null) {
            System.out.println("Error: Cliente es nulo en darBajaCliente.");
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Seguro que desea dar de baja a este cliente?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ClienteDAO clienteDAO = new ClienteDAO();
            int idCliente = cliente.getIdCliente();
            int filasAfectadas = clienteDAO.desactivarCliente(idCliente);
            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Cliente dado de baja exitosamente.");
                administradorController.loadClientes();
                dialogStage.close();
            } else {
                mostrarAlertaError("Error al dar de baja cliente.");
            }
        }
    }

    private void mostrarAlertaInformacion(String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}