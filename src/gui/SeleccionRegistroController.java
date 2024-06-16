package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SeleccionRegistroController {

    @FXML
    private void handleRegistrarseComoCliente(javafx.event.ActionEvent event) {
        abrirVentanaRegistro(event, "/gui/RegistroCliente.fxml");
    }

    @FXML
    private void handleRegistrarseComoAdministrador(javafx.event.ActionEvent event) {
        abrirVentanaRegistro(event, "/gui/RegistrarAdministrador.fxml");
    }

    private void abrirVentanaRegistro(javafx.event.ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
