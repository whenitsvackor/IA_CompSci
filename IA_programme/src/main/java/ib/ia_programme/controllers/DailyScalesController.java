package ib.ia_programme.controllers;

import ib.ia_programme.util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class DailyScalesController{

    private Stage stage;
    @FXML private Button backButton;
    @FXML private Slider rateMyDaySlider;
    @FXML private Button rateMyDaySaverButton;
    @FXML private Slider anxietyLogSlider;
    @FXML private Button anxietyLogSaverButton;
    @FXML private Slider physicalWellbeingLogSlider;
    @FXML private Button physicalWellbeingLogSaverButton;

    private static final String SQL = "SELECT mood_scale, anxiety_scale, physical_scale FROM daily_entries WHERE entry_date = ?";

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void switchScene(String fxmlFilePath, Button sourceButton) throws IOException{
        this.stage = (Stage) sourceButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        Scene newScene = new Scene(root, 720, 400);
        String css = getClass().getResource("/ib/ia_programme/tracker.css").toExternalForm();
        newScene.getStylesheets().add(css);
        stage.setScene(newScene);
    }

    @FXML public void initialize(){
        LocalDate today = LocalDate.now();
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SQL)){
            statement.setDate(1, java.sql.Date.valueOf(today));
            var resultSet = statement.executeQuery();
            if (resultSet.next()){
                int mood = resultSet.getInt("mood_scale");
                if (!resultSet.wasNull()){
                    rateMyDaySlider.setValue(mood);
                }
                int anxiety = resultSet.getInt("anxiety_scale");
                if (!resultSet.wasNull()){
                    anxietyLogSlider.setValue(anxiety);
                }
                int physical = resultSet.getInt("physical_scale");
                if (!resultSet.wasNull()){
                    physicalWellbeingLogSlider.setValue(physical);
                }
            }
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    public void successPopup(String message){
        Alert success = new Alert(AlertType.INFORMATION);
        success.setTitle("Success");
        success.setHeaderText(null);
        success.setContentText(message);
        success.showAndWait();
    }

    @FXML public void onBackButtonClick() throws IOException{
        switchScene("/ib/ia_programme/main-page-view.fxml", backButton);
        stage.setTitle("TRACKER - MAIN PAGE");
    }

    @FXML public void onRateMyDaySaverButtonClick(){
        LocalDate dateRateMyDay = LocalDate.now();
        int moodValue = (int) rateMyDaySlider.getValue();
        String sql = "INSERT INTO daily_entries (entry_date, mood_scale) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE mood_scale = VALUES(mood_scale)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateRateMyDay));
            statement.setInt(2, moodValue);
            statement.executeUpdate();
            successPopup("Daily rating saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    @FXML public void onAnxietyLogSaverButtonClick(){
        LocalDate dateAnxietyLog = LocalDate.now();
        int anxietyValue = (int) anxietyLogSlider.getValue();
        String sql = "INSERT INTO daily_entries (entry_date, anxiety_scale) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE anxiety_scale = VALUES(anxiety_scale)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateAnxietyLog));
            statement.setInt(2, anxietyValue);
            statement.executeUpdate();
            successPopup("Anxiety rate saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    @FXML public void onPhysicalWellbeingLogSaverButtonClick(){
        LocalDate datePhysicalWellbeingLog = LocalDate.now();
        int physicalValue = (int) physicalWellbeingLogSlider.getValue();
        String sql = "INSERT INTO daily_entries (entry_date, physical_scale) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE physical_scale = VALUES(physical_scale)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(datePhysicalWellbeingLog));
            statement.setInt(2, physicalValue);
            statement.executeUpdate();
            successPopup("Physical wellbeing rate saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }
}