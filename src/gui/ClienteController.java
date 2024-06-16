package gui;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.DAO.ClaseDAO;
import logica.DAO.EntrenadorDAO;
import logica.dominio.Clase;
import logica.dominio.Entrenador;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class ClienteController implements Initializable {

    @FXML
    private TableView<Clase> tableClases;

    @FXML
    private TableColumn<Clase, String> colNombre;

    @FXML
    private TableColumn<Clase, String> colTipo;

    @FXML
    private TableColumn<Clase, String> colHoraInicio;

    @FXML
    private TableColumn<Clase, String> colHoraFin;

    @FXML
    private TableColumn<Clase, String> colFechaClase;

    @FXML
    private TableColumn<Clase, String> colEntrenador;
    
    @FXML
    private StackPane stackPaneContent;

    @FXML
    private Label labelTitulo;

    @FXML
    private Button btnClases;

    @FXML
    private Button btnPerfil;

    @FXML
    private Button buttonCerrarSesion;

    private final ClaseDAO claseDAO = new ClaseDAO();
    private final EntrenadorDAO entrenadorDAO = new EntrenadorDAO();
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        colHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        colFechaClase.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaClase().toString()));
        colEntrenador.setCellValueFactory(cellData -> {
            int idEntrenador = cellData.getValue().getIdEntrenador();
            Entrenador entrenador = null;
            try {
                entrenador = entrenadorDAO.obtenerEntrenadorPorId(idEntrenador);
            } catch (SQLException ex) {
                Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (entrenador != null) {
                return new SimpleStringProperty(entrenador.getNombre());
            } else {
                return new SimpleStringProperty("");
            }
        });
        loadClases();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadClases() {
        List<Clase> clasesActivas = claseDAO.listarClasesActivas();
        ObservableList<Clase> data = FXCollections.observableArrayList(clasesActivas);
        tableClases.setItems(data);
    }

    @FXML
    void handleClases(ActionEvent event) {
        loadClases();
        tableClases.setVisible(true);
        labelTitulo.setText("¡Diviértete con nuestras clases!");
    }

    @FXML
    void handlePerfil(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/PerfilCliente.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Perfil del Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
private void cerrarSesion(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InicioSesion.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        
        // Obtener el controlador del inicio de sesión
        InicioSesionController controller = loader.getController();
        
        // Configurar el escenario para el inicio de sesión
        controller.setStage(stage);
        
        // Mostrar la ventana de inicio de sesión
        stage.show();
        
        // Cerrar la ventana actual (la ventana de cliente)
        Stage currentStage = (Stage) buttonCerrarSesion.getScene().getWindow();
        currentStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
