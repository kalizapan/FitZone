package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import logica.dominio.Cliente;
import logica.DAO.ClienteDAO;

public class RegistroClienteController implements Initializable {

    @FXML
    private Button buttonRegistrarse;

    @FXML
    private Button buttonRegresar;

    @FXML
    private TextField txtfieldNombre;

    @FXML
    private TextField txtfieldApellidoPaterno;

    @FXML
    private TextField txtfieldApellidoMaterno;

    @FXML
    private TextField txtfieldCorreo;

    @FXML
    private TextField txtfieldContrasena;

    @FXML
    private TextField txtfieldDireccion;

    @FXML
    private TextField txtfieldCelular;

    @FXML 
    private ComboBox<String> cBoxMembresia;

    private ClienteDAO clienteDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO = new ClienteDAO();
        cBoxMembresia.getItems().addAll("Mensual", "Anual", "Semestral");
    }

    @FXML
    private void handleRegistrarse(ActionEvent event) {
        try {
            String nombre = txtfieldNombre.getText();
            String apellidoPaterno = txtfieldApellidoPaterno.getText();
            String apellidoMaterno = txtfieldApellidoMaterno.getText();
            String correo = txtfieldCorreo.getText();
            String contrasena = txtfieldContrasena.getText();
            String direccion = txtfieldDireccion.getText();
            String numeroCelular = txtfieldCelular.getText();
            String membresia = cBoxMembresia.getValue();
            int estado = 1;

            // Validaciones
            if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || correo.isEmpty() ||
                contrasena.isEmpty() || direccion.isEmpty() || numeroCelular.isEmpty() || membresia == null) {
                mostrarAlertaError("Por favor, completa todos los campos.");
                return;
            }

            if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,30}$")) {
                mostrarAlertaError("El nombre no puede contener números ni caracteres especiales, "
                        + "y debe tener un máximo de 30 caracteres.");
                return;
            }

            if (!apellidoPaterno.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,30}$")) {
                mostrarAlertaError("El apellido paterno no puede contener números ni caracteres especiales, "
                        + "y debe tener un máximo de 30 caracteres.");
                return;
            }

            if (!apellidoMaterno.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,30}$")) {
                mostrarAlertaError("El apellido materno no puede contener números ni caracteres especiales, "
                        + "y debe tener un máximo de 30 caracteres.");
                return;
            }

            if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                mostrarAlertaError("El formato del correo no es válido.");
                return;
            }

            if (!contrasena.matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$")) {
                mostrarAlertaError("La contraseña debe tener al entre 8 y 16 caracteres, al "
                                + "menos un dígito, al menos una minúscula y al menos una mayúscula.");
                return;
            }

            if (!direccion.matches("^[-a-zA-Z0-9áéíóúÁÉÍÓÚñÑ/()#,. ]{1,100}$")) {
                mostrarAlertaError("La dirección no puede contener caracteres especiales, "
                        + "y debe tener un máximo de 100 caracteres.");
                return;
            }

            if (!numeroCelular.matches("^[0-9]{10}$")) {
                mostrarAlertaError("El número de celular debe tener exactamente 10 dígitos "
                        + "y solo contener números.");
                return;
            }

            // Confirmación antes de registrar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de Registro");
            confirmacion.setHeaderText("Confirmación");
            confirmacion.setContentText("¿Estás seguro tu información? Se guardarán tus datos.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Creación del cliente y registro en la base de datos
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setNombre(nombre);
                nuevoCliente.setApellidoPaterno(apellidoPaterno);
                nuevoCliente.setApellidoMaterno(apellidoMaterno);
                nuevoCliente.setCorreo(correo);
                nuevoCliente.setContrasena(contrasena);
                nuevoCliente.setDireccion(direccion);
                nuevoCliente.setNumeroCelular(numeroCelular);
                nuevoCliente.setEstado(estado);
                nuevoCliente.setMembresia(membresia);

                int filasAfectadas = clienteDAO.registrarCliente(nuevoCliente);
                if (filasAfectadas > 0) {
                    mostrarAlerta("Tu cuenta se ha registrado exitosamente.");
                    vaciarCampos();
                    // Cerrar la ventana actual y regresar a la ventana de inicio de sesión
                    Stage stageActual = (Stage) buttonRegistrarse.getScene().getWindow();
                    stageActual.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
                    AnchorPane root = loader.load();

                    InicioSesionController controller = loader.getController();
                    controller.setStage(stageActual);

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Inicio de Sesión");
                    stage.show();
                } else {
                    mostrarAlertaError("Error al registrar cuenta, correo repetido.");
                }
            }
        } catch (IllegalArgumentException ex) {
            mostrarAlertaError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarAlertaError("Error al registrar cliente: " + ex.getMessage());
        } catch (Exception e) {
            mostrarAlertaError("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
        if (camposLlenos()) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación");
            confirmacion.setHeaderText("Confirmación");
            confirmacion.setContentText("¿Estás seguro de querer regresar? Se borrará tu información.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                cerrarVentanaYMostrarInicioSesion();
            }
        } else {
            cerrarVentanaYMostrarInicioSesion();
        }
    }

    private boolean camposLlenos() {
        return !txtfieldNombre.getText().isEmpty() || !txtfieldApellidoPaterno.getText().isEmpty() ||
                !txtfieldApellidoMaterno.getText().isEmpty() || !txtfieldCorreo.getText().isEmpty() ||
                !txtfieldContrasena.getText().isEmpty() || !txtfieldDireccion.getText().isEmpty() ||
                !txtfieldCelular.getText().isEmpty();
    }

    private void cerrarVentanaYMostrarInicioSesion() {
        try {
            Stage stageActual = (Stage) buttonRegresar.getScene().getWindow();
            stageActual.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
            AnchorPane root = loader.load();

            InicioSesionController controller = loader.getController();
            controller.setStage(stageActual);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (Exception e) {
            mostrarAlertaError("Error al cargar la ventana de inicio de sesión: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText("Información");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void vaciarCampos() {
        txtfieldNombre.clear();
        txtfieldApellidoPaterno.clear();
        txtfieldApellidoMaterno.clear();
        txtfieldCorreo.clear();
        txtfieldContrasena.clear();
        txtfieldDireccion.clear();
        txtfieldCelular.clear();
        cBoxMembresia.setValue(null);
    }
}