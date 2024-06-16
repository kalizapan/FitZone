package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logica.DAO.ClaseDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Entrenador;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ConsultaClaseController implements Initializable {
    @FXML
    private TextField txtFieldNombreClase;
    @FXML
    private ComboBox<String> cBoxTipo;
    @FXML
    private TextField txtFieldCapacidad;
    @FXML
    private TextField txtFieldHoraInicio;
    @FXML
    private TextField txtFieldHoraFin;
    @FXML
    private TextField txtFieldPrecio;
    @FXML
    private DatePicker dtpickerFechaDeInicio;
    @FXML
    private ComboBox<String> cBoxEntrenador;

    @FXML
    private Label lblNombreClase;
    @FXML
    private Label lblTipo;
    @FXML
    private Label lblCapacidad;
    @FXML
    private Label lblHoraInicio;
    @FXML
    private Label lblHoraFin;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblFechaInicio;
    @FXML
    private Label lblEntrenador;

    @FXML
    private Rectangle fondoRectangle1;
    @FXML
    private Rectangle fondoRectangle2;
    @FXML
    private Rectangle fondoRectangle3;
    @FXML
    private Rectangle fondoRectangle4;
    @FXML
    private Rectangle fondoRectangle5;
    @FXML
    private Rectangle fondoRectangle6;
    @FXML
    private Rectangle fondoRectangle7;
    @FXML
    private Rectangle fondoRectangle8;
    
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private Clase clase;
    private ClaseDAO claseDAO;
    private EntrenadorDAO entrenadorDAO;
    private AdministradorController administradorController;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        claseDAO = new ClaseDAO();
        entrenadorDAO = new EntrenadorDAO();
        cBoxTipo.getItems().addAll("Pilates", "Cardio", "Spinning", "Yoga");
        cargarEntrenador();
    }

    private void cargarEntrenador() {
        List<Entrenador> entrenadorActivo = entrenadorDAO.listarEntrenadoresActivos();
        ObservableList<String> nombresEntrenadores = FXCollections.observableArrayList();
        for (Entrenador entrenador : entrenadorActivo) {
            nombresEntrenadores.add(entrenador.getNombre());
        }
        cBoxEntrenador.setItems(nombresEntrenadores);
    }

    public void initData(Clase clase, AdministradorController adminController) {
        this.clase = clase;
        this.administradorController = adminController;
        mostrarDetallesClase();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void actualizarClase() {
        toggleEditMode(true);
    }

    private boolean validarDatosClase() {
        String nombre = txtFieldNombreClase.getText().trim();
        String tipo = cBoxTipo.getValue();
        String capacidad = txtFieldCapacidad.getText().trim();
        String horaInicio = txtFieldHoraInicio.getText().trim();
        String horaFin = txtFieldHoraFin.getText().trim();
        String precio = txtFieldPrecio.getText().trim();
        LocalDate fechaInicio = dtpickerFechaDeInicio.getValue();
        String nombreEntrenador = cBoxEntrenador.getValue();

        if (nombre.isEmpty() || tipo == null || capacidad.isEmpty() || horaInicio.isEmpty() ||
            horaFin.isEmpty() || precio.isEmpty() || fechaInicio == null || nombreEntrenador == null) {
            mostrarAlertaError("Por favor, completa todos los campos.");
            return false;
        }

        if (nombre.length() > 70) {
            mostrarAlertaError("El nombre debe tener menos de 70 caracteres.");
            return false;
        }
        if (!nombre.matches("[a-zA-ZÀ-ÿ\u00f1\u00d1 ]+")) {
            mostrarAlertaError("El nombre no puede contener caracteres especiales.");
            return false;
        }

        if (capacidad.length() > 2) {
            mostrarAlertaError("No pueden ser más de 30 personas en una clase.");
            return false;
        }
        if (!capacidad.matches("^(?:[1-9]|[12][0-9]|30)$")) {
            mostrarAlertaError("La capacidad debe estar en el rango de 1 a 30 personas.");
            return false;
        }
        try {
            Integer.valueOf(capacidad);
        } catch (NumberFormatException e) {
            mostrarAlertaError("La capacidad debe ser un número entero.");
            return false;
        }

        if (horaInicio.length() != 5 || !horaInicio.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            mostrarAlertaError("El formato de la hora de inicio debe ser hh:mm.");
            return false;
        }

        if (horaFin.length() != 5 || !horaFin.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            mostrarAlertaError("El formato de la hora de fin debe ser hh:mm.");
            return false;
        }
        if (precio.length() > 4) {
            mostrarAlertaError("No pueden ser más de 4 dígitos.");
            return false;
        }
        if (!precio.matches("[0-9]+")) {
            mostrarAlertaError("El precio debe estar en el rango de $1 a $9999.");
            return false;
        }
        try {
            Integer.valueOf(precio);
        } catch (NumberFormatException e) {
            mostrarAlertaError("El precio debe ser un número entero.");
            return false;
        }

        return true;
    }

   @FXML
    private void aceptarActualizacion() {
        if (clase == null) {
            mostrarAlertaError("Error: Clase es nula.");
            return;
        }

        if (!validarDatosClase()) {
            return; // Si la validación falla, se detiene el proceso
        }

        try {
            clase.setNombre(txtFieldNombreClase.getText().trim());
            clase.setTipo(cBoxTipo.getValue());
            clase.setCapacidad(Integer.parseInt(txtFieldCapacidad.getText().trim()));
            clase.setHoraInicio(txtFieldHoraInicio.getText().trim());
            clase.setHoraFin(txtFieldHoraFin.getText().trim());
            clase.setPrecio(Integer.parseInt(txtFieldPrecio.getText().trim())); 
            clase.setFechaClase(dtpickerFechaDeInicio.getValue().format(dateFormatter));
            String nombreEntrenadorSeleccionado = cBoxEntrenador.getValue();
            clase.setIdEntrenador(entrenadorDAO.obtenerIdEntrenadorPorNombre(nombreEntrenadorSeleccionado));

            int filasAfectadas = claseDAO.actualizarClase(clase);
            System.out.println("Filas afectadas: " + filasAfectadas); // Mensaje de depuración

            if (filasAfectadas > 0) {
                mostrarAlertaInformacion("Clase actualizada exitosamente.");
                mostrarDetallesClase();
                administradorController.loadClases(); // Recargar la tabla de clases en el AdministradorController

                // Asegúrate de que la tabla se actualice visualmente
                Platform.runLater(() -> {
                    administradorController.getTableClases().refresh();
                });
            } else {
                mostrarAlertaError("Error al actualizar clase.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaError("Error al actualizar clase: " + e.getMessage());
        } finally {
            toggleEditMode(false);
        }
    }


    @FXML
    private void cancelarClase() {
        dialogStage.close();
    }

    private void mostrarDetallesClase() {
        if (clase != null) {
            lblNombreClase.setText(clase.getNombre());
            lblTipo.setText(clase.getTipo());
            lblCapacidad.setText(String.valueOf(clase.getCapacidad()));
            lblHoraInicio.setText(clase.getHoraInicio());
            lblHoraFin.setText(clase.getHoraFin());
            lblPrecio.setText(String.valueOf(clase.getPrecio()));
            lblFechaInicio.setText(clase.getFechaClase());
            try {
                lblEntrenador.setText(entrenadorDAO.obtenerNombreEntrenadorPorId(clase.getIdEntrenador()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            txtFieldNombreClase.setText(clase.getNombre());
            cBoxTipo.setValue(clase.getTipo());
            txtFieldCapacidad.setText(String.valueOf(clase.getCapacidad()));
            txtFieldHoraInicio.setText(clase.getHoraInicio());
            txtFieldHoraFin.setText(clase.getHoraFin());
            txtFieldPrecio.setText(String.valueOf(clase.getPrecio()));
            dtpickerFechaDeInicio.setValue(LocalDate.parse(clase.getFechaClase(), dateFormatter));
            try {
                cBoxEntrenador.setValue(entrenadorDAO.obtenerNombreEntrenadorPorId(clase.getIdEntrenador()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: Clase es nula en mostrarDetallesClase.");
        }
    }

    private void toggleEditMode(boolean editable) {
        lblNombreClase.setVisible(!editable);
        lblTipo.setVisible(!editable);
        lblCapacidad.setVisible(!editable);
        lblHoraInicio.setVisible(!editable);
        lblHoraFin.setVisible(!editable);
        lblPrecio.setVisible(!editable);
        lblFechaInicio.setVisible(!editable);
        lblEntrenador.setVisible(!editable);

        txtFieldNombreClase.setVisible(editable);
        cBoxTipo.setVisible(editable);
        txtFieldCapacidad.setVisible(editable);
        txtFieldHoraInicio.setVisible(editable);
        txtFieldHoraFin.setVisible(editable);
        txtFieldPrecio.setVisible(editable);
        dtpickerFechaDeInicio.setVisible(editable);
        cBoxEntrenador.setVisible(editable);

        fondoRectangle1.setVisible(!editable);
        fondoRectangle2.setVisible(!editable);
        fondoRectangle3.setVisible(!editable);
        fondoRectangle4.setVisible(!editable);
        fondoRectangle5.setVisible(!editable);
        fondoRectangle6.setVisible(!editable);
        fondoRectangle7.setVisible(!editable);
        fondoRectangle8.setVisible(!editable);

        btnActualizar.setVisible(!editable);
        btnAceptar.setVisible(editable);
    }

    private void mostrarAlertaInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}