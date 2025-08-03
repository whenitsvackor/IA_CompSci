package ib.ia_programme;

import ib.ia_programme.controllers.MainPageController;
import ib.ia_programme.others.MotivationalMessagesCreator;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class TrackerApplication extends Application{


    @Override
    public void start(Stage stage){
        try{
            FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("/ib/ia_programme/main-page-view.fxml"));
            Scene mainScene = new Scene(mainSceneLoader.load(), 720, 400);
            String css = getClass().getResource("/ib/ia_programme/tracker.css").toExternalForm();
            mainScene.getStylesheets().add(css);
            MainPageController controller = mainSceneLoader.getController();
            controller.setStage(stage);
            stage.setTitle("TRACKER - MAIN PAGE");
            stage.setScene(mainScene);
            stage.show();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        launch();
    }
}
