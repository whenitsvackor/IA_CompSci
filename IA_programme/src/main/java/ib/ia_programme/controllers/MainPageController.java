package ib.ia_programme.controllers;

import ib.ia_programme.others.MotivationalMessagesCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    private Stage stage;
    @FXML private Label motivationalMessageLabel;
    @FXML private Button regenerateButton;
    @FXML private Button dailyScalesButton;
    @FXML private Button dailyThoughtsButton;
    @FXML private Button personalImprovementButton;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void switchScene(String fxmlPath, Button sourceButton) throws IOException {
        Stage currentStage = (Stage) sourceButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(root, 720, 400);
        
        // Apply CSS styling
        String cssPath = getClass().getResource("/ib/ia_programme/tracker.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        
        currentStage.setScene(scene);
    }

    @FXML 
    public void initialize() {
        // Load initial motivational message
        refreshMotivationalMessage();
    }

    @FXML 
    public void onDailyScalesButtonClick() throws IOException {
        switchScene("/ib/ia_programme/daily-scales-view.fxml", dailyScalesButton);
        stage.setTitle("TRACKER - DAILY SCALES");
    }

    @FXML 
    public void onDailyThoughtsButtonClick() throws IOException {
        switchScene("/ib/ia_programme/daily-thoughts-view.fxml", dailyThoughtsButton);
        stage.setTitle("TRACKER - DAILY THOUGHTS");
    }

    @FXML 
    public void onPersonalImprovementButtonClick() throws IOException {
        switchScene("/ib/ia_programme/personal-improvement-view.fxml", personalImprovementButton);
        stage.setTitle("TRACKER - PERSONAL IMPROVEMENT");
    }

    @FXML 
    public void onRegenerateButtonClick() {
        refreshMotivationalMessage();
    }

    private void refreshMotivationalMessage() {
        String message = MotivationalMessagesCreator.messageCreator();
        adjustFontSizeForMessage(message);
    }

    private void adjustFontSizeForMessage(String message) {
        double available = motivationalMessageLabel.getWidth();
        if (available <= 0) {
            available = motivationalMessageLabel.getPrefWidth();
        }
        // small margin to avoid clipping borders
        double maxWidth = Math.max(0, available - 8);

        String fontFamily = motivationalMessageLabel.getFont().getFamily();
        double fontSize = 36.0;
        double minFontSize = 4.0;

        Text measurer = new Text(message);
        measurer.setBoundsType(javafx.scene.text.TextBoundsType.VISUAL);

        while (fontSize > minFontSize) {
            measurer.setFont(Font.font(fontFamily, fontSize));
            double textWidth = measurer.getLayoutBounds().getWidth();
            if (textWidth <= maxWidth) {
                break;
            }
            fontSize -= 1.0;
        }

        // Use CSS so it wins over stylesheet defaults
        motivationalMessageLabel.setStyle("-fx-font-size: " + fontSize + "px;");
        motivationalMessageLabel.setText(message);
    }
}
