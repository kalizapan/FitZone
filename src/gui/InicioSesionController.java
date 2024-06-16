package gui;

import logica.DAO.AdministradorDAO;
import logica.DAO.ClienteDAO;
import logica.dominio.SesionAdministrador;
import logica.dominio.SesionCliente;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InicioSesionController {

    @FXML
    private TextField correoTextField;

    @FXML
    private PasswordField contraseñaTextField; // Corrección del tipo de campo

    @FXML
    private Button ingresarButton;

    @FXML
    private Button crearCuentaButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        ingresarButton.setOnAction(event -> {
            try {
                handleIniciarSesion();
            } catch (SQLException ex) {
                Logger.getLogger(InicioSesionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        crearCuentaButton.setOnAction(event -> handleRegistrarse());
    }

    @FXML
    private void handleIniciarSesion() throws SQLException {
        String correo = correoTextField.getText().trim();
        String contrasenia = contraseñaTextField.getText().trim();

        ClienteDAO clienteDAO = new ClienteDAO();
        AdministradorDAO administradorDAO = new AdministradorDAO();

        if (administradorDAO.validarCredenciales(correo, contrasenia)) {
            handleIniciarSesionAdministrativo(correo, contrasenia);
        } else {
            handleIniciarSesionCliente(correo, contrasenia);
        }
    }

    private void handleIniciarSesionCliente(String correo, String contrasenia) throws SQLException {
    ClienteDAO clienteDAO = new ClienteDAO();

    if (clienteDAO.validarCredenciales(correo, contrasenia)) {
        if (clienteDAO.verificarEstadoCliente(correo)) {
            int idCliente = clienteDAO.obtenerIdCliente(correo);
            SesionCliente.getInstancia().setIdCliente(idCliente);
            abrirVentanaCliente();
            cerrarVentanaActual();
        } else {
            mostrarAlerta("Tu cuenta ha sido dada de baja.");
        }
    } else {
        mostrarAlerta("Correo o contraseña incorrectos");
    }
}

private void handleIniciarSesionAdministrativo(String correo, String contrasenia) throws SQLException {
    AdministradorDAO administradorDAO = new AdministradorDAO();

    if (administradorDAO.validarCredenciales(correo, contrasenia)) {
        if (administradorDAO.verificarEstadoAdministrador(correo)) {
            int idAdministrador = administradorDAO.obtenerIdAdministrador(correo);
            SesionAdministrador.getInstancia().setIdAdministrador(idAdministrador);
            abrirVentanaAdministrador();
            cerrarVentanaActual();
        } else {
            mostrarAlerta("La cuenta está inactiva. Contacta al administrador.");
        }
    } else {
        mostrarAlerta("Correo o contraseña incorrectos");
    }
}

private void cerrarVentanaActual() {
    if (stage != null) {
        stage.close();
    }
}


    private void abrirVentanaCliente() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Cliente.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));        
        newStage.show();
        if (stage != null) {
            stage.close();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void abrirVentanaAdministrador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Administrador.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleRegistrarse() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SeleccionRegistro.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
