package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logica.dominio.Cliente;

import java.io.IOException;

public class ModificarPerfilController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoPaternoField;
    @FXML
    private TextField apellidoMaternoField;
    @FXML
    private TextField correoField;
    @FXML
    private TextField contrasenaField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField membresiaField;

    private Cliente cliente;
    private Stage stage;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        cargarDatosCliente();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void cargarDatosCliente() {
        if (cliente != null) {
            nombreField.setText(cliente.getNombre());
            apellidoPaternoField.setText(cliente.getApellidoPaterno());
            apellidoMaternoField.setText(cliente.getApellidoMaterno());
            correoField.setText(cliente.getCorreo());
            contrasenaField.setText(cliente.getContrasena());
            direccionField.setText(cliente.getDireccion());
            telefonoField.setText(cliente.getNumeroCelular());
            membresiaField.setText(cliente.getMembresia()); 
        }
    }

    @FXML
    private void handleRegresar() {
    }

    @FXML
    private void handleActualizar() {
        cliente.setNombre(nombreField.getText());
        cliente.setApellidoPaterno(apellidoPaternoField.getText());
        cliente.setApellidoMaterno(apellidoMaternoField.getText());
        cliente.setCorreo(correoField.getText());
        cliente.setContrasena(contrasenaField.getText());
        cliente.setDireccion(direccionField.getText());
        cliente.setNumeroCelular(telefonoField.getText());
        cliente.setMembresia(membresiaField.getText()); 

        mostrarAlerta("Perfil actualizado", "Sus datos han sido actualizados.");
        
        handleRegresar();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}