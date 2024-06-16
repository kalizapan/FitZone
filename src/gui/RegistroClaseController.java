package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logica.DAO.ClaseDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Entrenador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;

public class RegistroClaseController implements Initializable {

    @FXML
    private Button buttonRegistrar;

    @FXML
    private Button buttonRegresar;

    @FXML
    private TextField txtfieldNombre;

    @FXML
    private ChoiceBox<String> chboxTipo;

    @FXML
    private DatePicker dtpickerFechaDeInicio;

    @FXML
    private TextField txtfieldCapacidad;

    @FXML
    private TextField txtfieldHoraInicio;

    @FXML
    private TextField txtfieldHoraFin;

    @FXML
    private TextField txtfieldPrecio;

    @FXML
    private ChoiceBox<String> chboxEntrenador;

    private ClaseDAO claseDAO;
    private EntrenadorDAO entrenadorDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claseDAO = new ClaseDAO();
        entrenadorDAO = new EntrenadorDAO();
        chboxTipo.getItems().addAll("Pilates", "Cardio", "Spinning", "Yoga");

        cargarNombresEntrenadores();
    }

    private void cargarNombresEntrenadores() {
        chboxEntrenador.getItems().clear(); 
        for (Entrenador entrenador : entrenadorDAO.listarEntrenadoresActivos()) {
            chboxEntrenador.getItems().add(entrenador.getNombre());
        }
    }
    
    private void mostrarMensaje(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void handleRegistrarClase(ActionEvent event) throws SQLException {
        String nombre = txtfieldNombre.getText();
        String capacidad = txtfieldCapacidad.getText();
        String horaInicio = txtfieldHoraInicio.getText();
        String horaFin = txtfieldHoraFin.getText();
        String precio = txtfieldPrecio.getText();
        String nombreEntrenador = chboxEntrenador.getValue();
        LocalDate fechaInicio = dtpickerFechaDeInicio.getValue();
        String tipo = chboxTipo.getValue();

        if (capacidad.isEmpty() || capacidad.length() > 2 ||
            nombre.isEmpty() || nombre.length() > 70 ||
            precio.isEmpty() || precio.length() > 4 ||
            nombreEntrenador.isEmpty() || nombreEntrenador.length() > 100 ||
            tipo.isEmpty() || tipo.length() > 40 ||
            fechaInicio == null ) {
                mostrarMensaje("Los campos no pueden estar vacíos o tienen demasiados caracteres.");
        }else{
            if (!nombre.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+") ||
                !precio.matches("[0-9]+") ||
                !nombreEntrenador.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+") ||
                !tipo.matches("[a-zA-ZÀ-ÿ0-9\u00f1\u00d1 ]+")) {
                    mostrarMensaje("Los campos no pueden contener caracteres especiales.");
            }else{
                if (horaInicio.isEmpty() || horaInicio.length() != 5 || !horaInicio.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") ||
                    horaFin.isEmpty() || horaFin.length() != 5 || !horaFin.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$") ) {
                    
                    mostrarMensaje("El formato de la hora debe ser horas y minutos (hh:mm)");
                }else{
                    if (!capacidad.matches("^(?:[1-9]|[12][0-9]|30)$")){
                        mostrarMensaje("El maximo de capacidad es de 30 personas por clase");
                    }else{
                    // Validación de tipos de datos
        int capacidadInt;
        int precioInt;
        try {
            capacidadInt = Integer.parseInt(capacidad);
            precioInt = Integer.parseInt(precio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Capacidad y precio deben ser números enteros.");
        }

        Entrenador entrenador = null;
        try {
            entrenador = entrenadorDAO.obtenerEntrenadorPorNombre(nombreEntrenador);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroClaseController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Error al obtener entrenador.", ex);
        }

        if (entrenador == null) {
            throw new IllegalArgumentException("No se encontró un entrenador con ese nombre.");
        }

        int idEntrenador = entrenador.getIdEntrenador();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaInicioString = fechaInicio.format(formato);

        Clase nuevaClase = new Clase(nombre, tipo, capacidadInt, fechaInicioString, horaInicio, horaFin, precioInt, 1, idEntrenador);
        int filasAfectadas = 0;
        try {
            filasAfectadas = claseDAO.registrarClase(nuevaClase);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroClaseController.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Error al registrar clase.", ex);
        }

        if (filasAfectadas > 0) {
            mostrarAlerta("Clase registrada exitosamente.");
            vaciarCampos();
        }
                }
            }
        }
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
        txtfieldCapacidad.clear();
        txtfieldHoraInicio.clear();
        txtfieldHoraFin.clear();
        txtfieldPrecio.clear();
        chboxEntrenador.getSelectionModel().clearSelection();
        chboxTipo.getSelectionModel().clearSelection();
        dtpickerFechaDeInicio.getEditor().clear();
    }
}