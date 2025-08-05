package ib.ia_programme;

import ib.ia_programme.controllers.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TrackerApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ib/ia_programme/main-page-view.fxml"));
            Scene scene = new Scene(loader.load(), 720, 400);
            
            // Load CSS styling
            String cssPath = getClass().getResource("/ib/ia_programme/tracker.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
            
            // Configure the controller
            MainPageController controller = loader.getController();
            controller.setStage(primaryStage);
            
            primaryStage.setTitle("TRACKER - MAIN PAGE");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to load application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
