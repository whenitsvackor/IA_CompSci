package ib.ia_programme.controllers;

import ib.ia_programme.others.TextualDataGetter;
import ib.ia_programme.others.NumericDataGetter;
import ib.ia_programme.util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

import static ib.ia_programme.util.DBConnection.getConnection;


public class PersonalImprovementController {
    private Stage stage;
    @FXML private Button backButton;
    @FXML private CheckBox predictionCheck;
    @FXML private VBox moodGraphContainer;
    @FXML private Button moodGraphButton;
    @FXML private VBox anxietyGraphContainer;
    @FXML private Button anxietyGraphButton;
    @FXML private VBox physicalGraphContainer;
    @FXML private Button physicalGraphButton;
    @FXML private VBox calendarContainer;
    @FXML private Button dailyThoughtsCalendarButton;
    @FXML private Label monthLabel;
    @FXML private Button previousMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private GridPane calendarGrid;
    private YearMonth currentYearMonth = YearMonth.now();
    private boolean moodGraphVisible = false;
    private boolean anxietyGraphVisible = false;
    private boolean physicalGraphVisible = false;
    private boolean calendarVisible = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void switchScene(String fxmlFilePath, Button sourceButton) throws IOException {
        this.stage = (Stage) sourceButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
        Scene newScene = new Scene(root, 720, 400);
        String css = getClass().getResource("/ib/ia_programme/tracker.css").toExternalForm();
        newScene.getStylesheets().add(css);
        stage.setScene(newScene);
    }

    @FXML
    public void onBackButtonClick() throws IOException {
        switchScene("/ib/ia_programme/main-page-view.fxml", backButton);
        stage.setTitle("TRACKER - MAIN PAGE");
    }

    @FXML
    public void onMoodGraphButtonClick() {
        moodGraphVisible = !moodGraphVisible;
        if (moodGraphVisible) {
            moodGraphContainer.getChildren().clear();
            moodGraphContainer.getChildren().add(createGraph("mood_scale", predictionCheck));
            moodGraphContainer.setVisible(true);
            moodGraphContainer.setManaged(true);
            moodGraphButton.setText("Hide Rate My Day");
        } else {
            moodGraphContainer.setVisible(false);
            moodGraphContainer.setManaged(false);
            moodGraphButton.setText("Rate My Day");
        }
    }

    @FXML
    public void onAnxietyGraphButtonClick() {
        anxietyGraphVisible = !anxietyGraphVisible;
        if (anxietyGraphVisible) {
            anxietyGraphContainer.getChildren().clear();
            anxietyGraphContainer.getChildren().add(createGraph("anxiety_scale", predictionCheck));
            anxietyGraphContainer.setVisible(true);
            anxietyGraphContainer.setManaged(true);
            anxietyGraphButton.setText("Hide Anxiety Log");
        } else {
            anxietyGraphContainer.setVisible(false);
            anxietyGraphContainer.setManaged(false);
            anxietyGraphButton.setText("Anxiety Log");
        }
    }

    @FXML
    public void onPhysicalGraphButtonClick() {
        physicalGraphVisible = !physicalGraphVisible;
        if (physicalGraphVisible) {
            physicalGraphContainer.getChildren().clear();
            physicalGraphContainer.getChildren().add(createGraph("physical_scale", predictionCheck));
            physicalGraphContainer.setVisible(true);
            physicalGraphContainer.setManaged(true);
            physicalGraphButton.setText("Hide Physical Wellbeing Log");
        } else {
            physicalGraphContainer.setVisible(false);
            physicalGraphContainer.setManaged(false);
            physicalGraphButton.setText("Physical Wellbeing Log");
        }
    }

    @FXML 
    public void onDailyThoughtsCalendarButtonClick() {
        calendarVisible = !calendarVisible;
        if (calendarVisible) {
            calendarContainer.setVisible(true);
            calendarContainer.setManaged(true);
            dailyThoughtsCalendarButton.setText("Hide Daily Thoughts");
            createCalendar();
        } else {
            calendarContainer.setVisible(false);
            calendarContainer.setManaged(false);
            dailyThoughtsCalendarButton.setText("Daily Thoughts");
        }
    }

    @FXML 
    public void onPreviousMonthButtonClick() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        createCalendar();
    }

    @FXML 
    public void onNextMonthButtonClick() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        createCalendar();
    }

    public List<NumericDataGetter> getScaleData(String scaleType) throws SQLException {
        List<NumericDataGetter> data = new ArrayList<>();
        String sql = "SELECT entry_date, " + scaleType + " FROM daily_entries ORDER BY entry_date";
        
        try (Connection connection = getConnection(); 
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int value = resultSet.getInt(scaleType);
                LocalDate date = resultSet.getDate("entry_date").toLocalDate();
                
                // Only include valid entries
                if (value > 0) {
                    data.add(new NumericDataGetter(date, value));
                }
            }
        }
        return data;
    }

    public LineChart<String, Number> createGraph(String scaleType, CheckBox showPrediction) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(1, 5, 1);
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        
        try {
            List<NumericDataGetter> data = getScaleData(scaleType);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Actual");
            
            for (NumericDataGetter point : data) {
                if (point.getValue() > 0) {
                    series.getData().add(new XYChart.Data<>(point.getDate().toString(), point.getValue()));
                }
            }
            
            chart.getData().add(series);
            
            // Add predictions if checkbox is selected
            if (showPrediction.isSelected() && !series.getData().isEmpty()) {
                addPrediction(series.getData(), chart);
            }
        } catch (Exception e) {
            System.err.println("Error creating graph: " + e.getMessage());
        }
        return chart;
    }

    private void addPrediction(List<XYChart.Data<String, Number>> actualData, LineChart<String, Number> chart) {
        if (actualData.size() < 2) return;
        
        // Simple linear regression
        int n = actualData.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        
        for (int i = 0; i < n; i++) {
            double x = i;
            double y = actualData.get(i).getYValue().doubleValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }
        
        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        
        XYChart.Series<String, Number> predictionSeries = new XYChart.Series<>();
        predictionSeries.setName("Predicted");
        
        LocalDate lastDate = LocalDate.parse(actualData.get(n - 1).getXValue());
        for (int i = 1; i <= 7; i++) {
            LocalDate futureDate = lastDate.plusDays(i);
            double predictedValue = slope * (n + i - 1) + intercept;
            predictionSeries.getData().add(new XYChart.Data<>(futureDate.toString(), predictedValue));
        }
        
        chart.getData().add(predictionSeries);
    }

    public Map<LocalDate, TextualDataGetter> getLogsForMonth(YearMonth month) {
        Map<LocalDate, TextualDataGetter> logs = new HashMap<>();
        String sql = "SELECT * FROM daily_entries WHERE YEAR(entry_date) = ? AND MONTH(entry_date) = ?";
        
        try (Connection connection = DBConnection.getConnection(); 
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, month.getYear());
            statement.setInt(2, month.getMonthValue());
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("entry_date").toLocalDate();
                TextualDataGetter log = new TextualDataGetter(
                    date,
                    resultSet.getString("overview"), 
                    resultSet.getString("todo"), 
                    resultSet.getString("goals"), 
                    resultSet.getString("gratitude")
                );
                logs.put(date, log);
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching logs: " + e.getMessage());
        }
        return logs;
    }

    public void createCalendar() {
        calendarGrid.getChildren().clear();
        
        // Set month header
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        monthLabel.setText(monthName + " " + currentYearMonth.getYear());
        
        // Add day headers
        DayOfWeek[] days = DayOfWeek.values();
        for (int i = 0; i < days.length; i++) {
            Label dayHeader = new Label(days[i].getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            dayHeader.getStyleClass().add("calendar-header");
            calendarGrid.add(dayHeader, i, 0);
        }
        
        // Get logs for the month
        Map<LocalDate, TextualDataGetter> logs = getLogsForMonth(currentYearMonth);
        
        // Add calendar days
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int startCol = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentYearMonth.lengthOfMonth();
        
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("calendar-day");
            
            // Check if this day has log entries
            if (logs.containsKey(date)) {
                TextualDataGetter log = logs.get(date);
                if (hasLogEntries(log)) {
                    dayLabel.getStyleClass().add("calendar-day-filled");
                    dayLabel.setOnMouseClicked(_ -> showLogPopup(log));
                }
            }
            
            int col = (startCol + day - 1) % 7;
            int row = (startCol + day - 1) / 7 + 1;
            calendarGrid.add(dayLabel, col, row);
        }
    }

    private boolean hasLogEntries(TextualDataGetter log) {
        return (log.getOverview() != null && !log.getOverview().trim().isEmpty()) ||
               (log.getTodo() != null && !log.getTodo().trim().isEmpty()) ||
               (log.getGoals() != null && !log.getGoals().trim().isEmpty()) ||
               (log.getGratitude() != null && !log.getGratitude().trim().isEmpty());
    }

    private void showLogPopup(TextualDataGetter log) {
        StringBuilder content = new StringBuilder();
        content.append("Things To Make The Day Special:\n").append(log.getOverview()).append("\n\n");
        content.append("Today's To-Do List:\n").append(log.getTodo()).append("\n\n");
        content.append("Goals To Keep Me Going:\n").append(log.getGoals()).append("\n\n");
        content.append("Grateful Thoughts:\n").append(log.getGratitude());
        
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Daily Thoughts");
        alert.setHeaderText("Entries for " + log.getDate());
        alert.setContentText(content.toString());
        alert.showAndWait();
    }

}
