package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import logica.dominio.Administrador;
import logica.DAO.AdministradorDAO;

public class RegistrarAdministradorController implements Initializable {

    @FXML
    private Button buttonRegistrarse;

    @FXML
    private Button buttonRegresar;

    @FXML
    private TextField txtfieldNombre;

    @FXML
    private TextField txtfieldApellidos;

    @FXML
    private TextField txtfieldCorreo;

    @FXML
    private PasswordField txtfieldContrasena;
    
    @FXML
    private TextField txtfieldCodigoSeguridad;

    @FXML
    private ComboBox<String> cBoxArea;

    private AdministradorDAO administradorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        administradorDAO = new AdministradorDAO();
        cBoxArea.getItems().addAll("recepción", "administrativa", "recursos humanos");
    }
    
    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleRegistrarse(ActionEvent event) {
        try {
            String nombre = txtfieldNombre.getText();
            String apellidos = txtfieldApellidos.getText();
            String correo = txtfieldCorreo.getText();
            String contrasena = txtfieldContrasena.getText();
            String area = cBoxArea.getValue();
            String codigoSeguridad = txtfieldCodigoSeguridad.getText(); // Obtener el código de seguridad

            if (area.isEmpty() || area.length() > 30 ||
            nombre.isEmpty() || nombre.length() > 60 ||
            apellidos.isEmpty() || apellidos.length() > 70 ||
            correo.isEmpty() || correo.length() > 100 ||
            contrasena.isEmpty() || contrasena.length() > 40 ||
            codigoSeguridad.isEmpty()) {
                mostrarMensaje("Los campos no pueden estar vacíos o tienen demasiados caracteres.");
        }else{
            if (!area.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+") ||
                !nombre.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+") ||
                !apellidos.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+")) {
                    mostrarMensaje("Los campos no pueden contener caracteres especiales.");
            }else{
                    if (!codigoSeguridad.equals("1a2b3c")){
                        mostrarMensaje("El codigo de seguridad no es valido");
                    }else{
                        if(!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                            mostrarMensaje("El formato del correo debe ser abcd@correo.com");
                        }else{
                        if(!contrasena.matches("^(?=\\w*\\d)(?=\\w*[A-Z])(?=\\w*[a-z])\\S{8,16}$")){
                            mostrarMensaje("La contraseña debe tener al entre 8 y 16 caracteres, al "
                                + "menos un dígito, al menos una minúscula y al menos una mayúscula.");
                        }
                        else{

            // Resto del código para registrar al administrador...

            Administrador nuevoAdministrador = new Administrador();
            nuevoAdministrador.setNombre(nombre);
            nuevoAdministrador.setApellidos(apellidos);
            nuevoAdministrador.setCorreo(correo);
            nuevoAdministrador.setContrasenia(contrasena);
            nuevoAdministrador.setArea(area);
            nuevoAdministrador.setEstado(true);


            int filasAfectadas = administradorDAO.registrarAdministrador(nuevoAdministrador);
            if (filasAfectadas > 0) {
                mostrarAlerta("Administrador registrado exitosamente.");
                vaciarCampos();
            } else {
                mostrarAlertaError("Error al registrar administrador.");
            }
                    }
            }
            }
            }
            }
        } catch (IllegalArgumentException ex) {
            mostrarAlertaError(ex.getMessage());
        } catch (SQLException ex) {
            mostrarAlertaError("Error al registrar administrador: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRegresar(ActionEvent event) {
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
        txtfieldApellidos.clear();
        txtfieldCorreo.clear();
        txtfieldContrasena.clear();
        txtfieldCodigoSeguridad.clear(); // Limpiar el campo de código de seguridad
        cBoxArea.setValue(null);
    }
}