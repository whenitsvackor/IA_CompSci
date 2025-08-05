package ib.ia_programme.controllers;

import ib.ia_programme.util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DailyThoughtsController {

    private Stage stage;
    @FXML private Button backButton;
    @FXML private TextArea thingsToMakeTheDaySpecialTextArea;
    @FXML private TextArea todaysToDoListTextArea;
    @FXML private TextArea goalsToKeepMeGoingTextArea;
    @FXML private TextArea gratefulThoughtsTextArea;
    @FXML private Button thingsToMakeTheDaySpecialSaverButton;
    @FXML private Button todaysToDoListSaverButton;
    @FXML private Button goalsToKeepMeGoingSaverButton;
    @FXML private Button gratefulThoughtsSaverButton;

    private static final String SQL = "SELECT overview, todo, goals, gratitude FROM daily_entries WHERE entry_date = ?";

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
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                String overview = resultSet.getString("overview");
                String todo = resultSet.getString("todo");
                String goals = resultSet.getString("goals");
                String gratitude = resultSet.getString("gratitude");
                thingsToMakeTheDaySpecialTextArea.setText(overview != null ? overview : "• ");
                todaysToDoListTextArea.setText(todo != null ? todo : "[ ] ");
                goalsToKeepMeGoingTextArea.setText(goals != null ? goals : "• ");
                gratefulThoughtsTextArea.setText(gratitude != null ? gratitude : "• ");
            } else{
                thingsToMakeTheDaySpecialTextArea.setText("• ");
                todaysToDoListTextArea.setText("[ ] ");
                goalsToKeepMeGoingTextArea.setText("• ");
                gratefulThoughtsTextArea.setText("• ");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        thingsToMakeTheDaySpecialTextArea.setOnKeyPressed
                (event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ENTER){
                        event.consume();
                        int caretPosition = thingsToMakeTheDaySpecialTextArea.getCaretPosition();
                        thingsToMakeTheDaySpecialTextArea.insertText(caretPosition, "• ");
                        thingsToMakeTheDaySpecialTextArea.positionCaret(caretPosition + 3);
                    }
                });
        todaysToDoListTextArea.setOnKeyPressed
                (event -> {
                    switch (event.getCode()){
                        case ENTER -> {
                            int caretPosition = todaysToDoListTextArea.getCaretPosition();
                            todaysToDoListTextArea.insertText(caretPosition, "[ ] ");
                        }
                    }
                });
        todaysToDoListTextArea.setOnMouseClicked
                (event -> {
                    if (event.getClickCount() == 2){
                        int caretPosition = todaysToDoListTextArea.getCaretPosition();
                        String[] lines = todaysToDoListTextArea.getText().split("\n");
                        int characterCount = 0;
                        for (int i = 0; i < lines.length; i++){
                            characterCount += lines[i].length() + 1;
                            if (caretPosition <= characterCount){
                                if (lines[i].startsWith("[ ]")){
                                    lines[i] = lines[i].replaceFirst("\\[ \\]", "[x]");
                                } else if (lines[i].startsWith("[x]")){
                                    lines[i] = lines[i].replaceFirst("\\[x\\]", "[ ]");
                                }
                                todaysToDoListTextArea.setText(String.join("\n", lines));
                                int newCaret = 0;
                                for (int j = 0; j <= i; j++) {
                                    newCaret += lines[j].length() + 1;
                                }
                                todaysToDoListTextArea.positionCaret(newCaret - 1);
                                break;
                            }
                        }
                    }
                });
        goalsToKeepMeGoingTextArea.setOnKeyPressed
                (event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ENTER){
                        event.consume();
                        int caretPosition = goalsToKeepMeGoingTextArea.getCaretPosition();
                        goalsToKeepMeGoingTextArea.insertText(caretPosition, "• ");
                        goalsToKeepMeGoingTextArea.positionCaret(caretPosition + 3);
                    }
                });
        gratefulThoughtsTextArea.setOnKeyPressed
                (event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ENTER){
                        event.consume();
                        int caretPosition = gratefulThoughtsTextArea.getCaretPosition();
                        gratefulThoughtsTextArea.insertText(caretPosition, "• ");
                        gratefulThoughtsTextArea.positionCaret(caretPosition + 3);
                    }
                });
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

    @FXML public void onThingsToMakeTheDaySpecialSaverButtonClick(){
        LocalDate dateThingsToMakeTheDaySpecial = LocalDate.now();
        String overview = thingsToMakeTheDaySpecialTextArea.getText();
        String sql = "INSERT INTO daily_entries (entry_date, overview) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE overview = VALUES(overview)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateThingsToMakeTheDaySpecial));
            statement.setString(2, overview);
            statement.executeUpdate();
            successPopup("Daily specials saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    @FXML public void onTodaysToDoListSaverButtonClick(){
        LocalDate dateTodaysToDoList = LocalDate.now();
        String todo = todaysToDoListTextArea.getText();
        String sql = "INSERT INTO daily_entries (entry_date, todo) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE todo = VALUES(todo)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateTodaysToDoList));
            statement.setString(2, todo);
            statement.executeUpdate();
            successPopup("To-do list saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    @FXML public void onGoalsToKeepMeGoingSaverButtonClick(){
        LocalDate dateGoalsToKeepMeGoing = LocalDate.now();
        String goals = goalsToKeepMeGoingTextArea.getText();
        String sql = "INSERT INTO daily_entries (entry_date, goals) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE goals = VALUES(goals)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateGoalsToKeepMeGoing));
            statement.setString(2, goals);
            statement.executeUpdate();
            successPopup("Goals saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }

    @FXML public void onGratefulThoughtsSaverButtonClick(){
        LocalDate dateGratefulThoughts = LocalDate.now();
        String gratitude = gratefulThoughtsTextArea.getText();
        String sql = "INSERT INTO daily_entries (entry_date, gratitude) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE gratitude = VALUES(gratitude)";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1, java.sql.Date.valueOf(dateGratefulThoughts));
            statement.setString(2, gratitude);
            statement.executeUpdate();
            successPopup("Grateful thoughts saved to database!😊");
        } catch (Exception e){
            System.out.println(e);;
        }
    }
}
