package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class gymFitzone extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
        Parent root = loader.load();
        
        InicioSesionController controller = loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        
        primaryStage.setTitle("Administrador - Gym Fit Zone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
