package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logica.dominio.Cliente;
import logica.dominio.SesionCliente;
import logica.DAO.ClienteDAO;
import javafx.stage.Window;
import java.sql.SQLException;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PerfilClienteController {

    private Stage stage;
    private ClienteDAO clienteDAO;

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoPaternoField;
    @FXML
    private TextField apellidoMaternoField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField membresiaField;

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
    private Label telefonoLabel;
    @FXML
    private Label membresiaLabel;

    @FXML
    private Button buttonModificar;
    @FXML
    private Button buttonAceptar;
    @FXML
    private Button buttonEliminarCuenta;
    @FXML
    private Button buttonRegresar;
    @FXML
    private Button buttonVerPagos;
    @FXML
    private Button buttonVerClases;

    private Cliente cliente;
    private boolean modificando = false;

    public void initialize() {
        try {
            int idCliente = SesionCliente.getInstancia().getIdCliente();
            clienteDAO = new ClienteDAO();
            cliente = clienteDAO.obtenerClientePorId(idCliente);

            if (cliente != null) {
                mostrarInformacionCliente();
            } else {
                mostrarAlerta("Error", "No se pudo cargar la información del cliente.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo obtener la información del cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarInformacionCliente() {
        nombreLabel.setText(cliente.getNombre());
        apellidoPaternoLabel.setText(cliente.getApellidoPaterno());
        apellidoMaternoLabel.setText(cliente.getApellidoMaterno());
        correoLabel.setText(cliente.getCorreo());
        direccionLabel.setText(cliente.getDireccion());
        telefonoLabel.setText(cliente.getNumeroCelular());
        membresiaLabel.setText(cliente.getMembresia());
    }

    @FXML
    private void handleRegresar() {
        if (modificando) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Estás seguro? Tu información no se guardará.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                cargarVentanaCliente();
            }
        } else {
            cargarVentanaCliente();
        }
    }

    private void cargarVentanaCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cliente.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Cliente");
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de cliente.");
        }
    }

    @FXML
    private void handleModificarInformacion() {
        if (cliente == null) {
            mostrarAlerta("Error", "No se puede modificar la información porque el cliente no está cargado.");
            return;
        }

        mostrarCamposEdicion(true);
        buttonModificar.setVisible(false);
        buttonVerPagos.setVisible(false);
        buttonVerClases.setVisible(false);
        buttonAceptar.setVisible(true);
        modificando = true;

        nombreField.setText(cliente.getNombre());
        apellidoPaternoField.setText(cliente.getApellidoPaterno());
        apellidoMaternoField.setText(cliente.getApellidoMaterno());
        correoField.setText(cliente.getCorreo());
        direccionField.setText(cliente.getDireccion());
        telefonoField.setText(cliente.getNumeroCelular());
        membresiaField.setText(cliente.getMembresia());
    }

    @FXML
    private void handleAceptarModificacion() {
        if (cliente == null) {
            mostrarAlerta("Error", "No se puede aceptar la modificación porque el cliente no está cargado.");
            return;
        }

        cliente.setNombre(nombreField.getText());
        cliente.setApellidoPaterno(apellidoPaternoField.getText());
        cliente.setApellidoMaterno(apellidoMaternoField.getText());
        cliente.setCorreo(correoField.getText());
        cliente.setDireccion(direccionField.getText());
        cliente.setNumeroCelular(telefonoField.getText());
        cliente.setMembresia(membresiaField.getText());

        try {
            clienteDAO.actualizarCliente(cliente);
            mostrarInformacionCliente();
            mostrarCamposEdicion(false);
            buttonModificar.setVisible(true);
            buttonVerPagos.setVisible(true);
            buttonVerClases.setVisible(true);
            buttonAceptar.setVisible(false);
            modificando = false;
            mostrarAlerta("Información actualizada", "Tu información ha sido actualizada.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo actualizar la información.");
            e.printStackTrace();
        }
    }

    private void mostrarCamposEdicion(boolean visible) {
        nombreField.setVisible(visible);
        apellidoPaternoField.setVisible(visible);
        apellidoMaternoField.setVisible(visible);
        correoField.setVisible(visible);
        direccionField.setVisible(visible);
        telefonoField.setVisible(visible);
        membresiaField.setVisible(visible);

        nombreLabel.setVisible(!visible);
        apellidoPaternoLabel.setVisible(!visible);
        apellidoMaternoLabel.setVisible(!visible);
        correoLabel.setVisible(!visible);
        direccionLabel.setVisible(!visible);
        telefonoLabel.setVisible(!visible);
        membresiaLabel.setVisible(!visible);
    }

@FXML
    private void handleEliminarCuenta() {
        if (cliente == null) {
            mostrarAlerta("Error", "No se puede eliminar la cuenta porque el cliente no está cargado.");
            return;
        }

        cliente.setEstado(0);
        try {
            clienteDAO.actualizarCliente(cliente);
            mostrarAlerta("Cuenta desactivada", "Su cuenta ha sido desactivada.");

            // Cerrar todas las ventanas abiertas
            cerrarTodasLasVentanas();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo desactivar la cuenta.");
            e.printStackTrace();
        }
    }

    private void cerrarTodasLasVentanas() {
        // Recopilar todas las ventanas abiertas en una lista
        List<Stage> stages = new ArrayList<>();
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                stages.add((Stage) window);
            }
        }

        // Cerrar todas las ventanas recopiladas
        for (Stage stage : stages) {
            stage.close();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
	
}