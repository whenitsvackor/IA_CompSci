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
        if (!moodGraphVisible) {
            moodGraphContainer.getChildren().clear();
            LineChart<String, Number> lineChart = createGraph("mood_scale", predictionCheck);
            moodGraphContainer.getChildren().add(lineChart);
            moodGraphContainer.setVisible(true);
            moodGraphContainer.setManaged(true);
            moodGraphButton.setText("Hide Rate My Day");
            moodGraphVisible = true;
        } else {
            moodGraphContainer.setVisible(false);
            moodGraphContainer.setManaged(false);
            moodGraphButton.setText("Rate My Day");
            moodGraphVisible = false;
        }
    }

    @FXML
    public void onAnxietyGraphButtonClick() {
        if (!anxietyGraphVisible) {
            anxietyGraphContainer.getChildren().clear();
            LineChart<String, Number> lineChart = createGraph("anxiety_scale", predictionCheck);
            anxietyGraphContainer.getChildren().add(lineChart);
            anxietyGraphContainer.setVisible(true);
            anxietyGraphContainer.setManaged(true);
            anxietyGraphButton.setText("Hide Anxiety Log");
            anxietyGraphVisible = true;
        } else {
            anxietyGraphContainer.setVisible(false);
            anxietyGraphContainer.setManaged(false);
            anxietyGraphButton.setText("Anxiety Log");
            anxietyGraphVisible = false;
        }
    }

    @FXML
    public void onPhysicalGraphButtonClick(){
        if (!physicalGraphVisible){
            physicalGraphContainer.getChildren().clear();
            LineChart<String, Number> lineChart = createGraph("physical_scale", predictionCheck);
            physicalGraphContainer.getChildren().add(lineChart);
            physicalGraphContainer.setVisible(true);
            physicalGraphContainer.setManaged(true);
            physicalGraphButton.setText("Hide Physical Wellbeing Log");
            physicalGraphVisible = true;
        } else {
            physicalGraphContainer.setVisible(false);
            physicalGraphContainer.setManaged(false);
            physicalGraphButton.setText("Physical Wellbeing Log");
            physicalGraphVisible = false;
        }
    }

    @FXML public void onDailyThoughtsCalendarButtonClick(){
        if (!calendarVisible){
            calendarContainer.setVisible(true);
            calendarContainer.setManaged(true);
            dailyThoughtsCalendarButton.setText("Hide Daily Thoughts");
            calendarVisible = true;
            createCalendar();
        } else{
            calendarContainer.setVisible(false);
            calendarContainer.setManaged(false);
            dailyThoughtsCalendarButton.setText("Daily Thoughts");
            calendarVisible = false;
        }
    }

    @FXML public void onPreviousMonthButtonClick(){
        currentYearMonth = currentYearMonth.minusMonths(1);
        createCalendar();
    }

    @FXML public void onNextMonthButtonClick(){
        currentYearMonth = currentYearMonth.plusMonths(1);
        createCalendar();
    }

    public List<NumericDataGetter> getScaleData(String type) throws SQLException{
        List<NumericDataGetter> scaleData = new ArrayList<>();
        String sql = "SELECT entry_date, " + type + " FROM daily_entries ORDER BY entry_date";
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int val = resultSet.getInt(type);
                LocalDate date = resultSet.getDate("entry_date").toLocalDate();
                if (val != 0){
                    scaleData.add(new NumericDataGetter(date, val));
                }
            }
        }
        return scaleData;
    }

    public LineChart<String, Number> createGraph(String type, CheckBox predictionCheck){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(1, 5, 1);
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        try{
            List<NumericDataGetter> data = getScaleData(type);
            XYChart.Series<String, Number> currentSeries = new XYChart.Series<>();
            currentSeries.setName("Actual");
            int seriesCounter = 1;
            for (NumericDataGetter point : data){
                LocalDate date = point.getDate();
                Integer value = point.getValue();
                if (value != 0){
                    currentSeries.getData().add(new XYChart.Data<>(date.toString(), value));
                } else{
                    if (!currentSeries.getData().isEmpty()){
                        currentSeries.setName("Actual " + seriesCounter++);
                        lineChart.getData().add(currentSeries);
                        currentSeries = new XYChart.Series<>();
                    }
                }
            }
            if (!currentSeries.getData().isEmpty()) {
                currentSeries.setName("Actual " + seriesCounter);
                lineChart.getData().add(currentSeries);
            }
            if (predictionCheck.isSelected()){
                List<XYChart.Data<String, Number>> allData = new ArrayList<>();
                for (XYChart.Series<String, Number> s : lineChart.getData()){
                    allData.addAll(s.getData());
                }
                if (allData.size() >= 2){
                    XYChart.Series<String, Number> predictionSeries = new XYChart.Series<>();
                    predictionSeries.setName("Predicted");
                    addPrediction(allData, predictionSeries);
                    lineChart.getData().add(predictionSeries);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return lineChart;
    }

    public void addPrediction(List<XYChart.Data<String, Number>> actualData, XYChart.Series<String, Number> predictionSeries){
        int size = actualData.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < size; i++){
            double x = i;
            double y = actualData.get(i).getYValue().doubleValue();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }
        double slope = (size * sumXY - sumX * sumY) / (size * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / size;
        String lastDateString = actualData.get(size - 1).getXValue();
        LocalDate lastDate = LocalDate.parse(lastDateString);
        for (int i = 1; i <= 7; i++){
            LocalDate futureDate = lastDate.plusDays(i);
            double predictedY = slope * (size + i - 1) + intercept;
            predictionSeries.getData().add(new XYChart.Data<>(futureDate.toString(), predictedY));
        }
    }

    public Map<LocalDate, TextualDataGetter> getLogsForMonth(YearMonth month){
        Map<LocalDate, TextualDataGetter> logs = new HashMap<>();
        String sql = "SELECT * FROM daily_entries WHERE YEAR(entry_date) = ? AND MONTH(entry_date) = ?";
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, month.getYear());
            statement.setInt(2, month.getMonthValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                LocalDate date = resultSet.getDate("entry_date").toLocalDate();
                logs.put(date, new TextualDataGetter(date, resultSet.getString("overview"), resultSet.getString("todo"), resultSet.getString("goals"), resultSet.getString("gratitude")));
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        return logs;
    }

    public void createCalendar(){
        calendarGrid.getChildren().clear();
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        monthLabel.setText(monthName + " " + currentYearMonth.getYear());
        Map<LocalDate, TextualDataGetter> logs = getLogsForMonth(currentYearMonth);
        DayOfWeek[] days = DayOfWeek.values();
        for (int i=0; i<days.length; i++){
            Label label = new Label(days[i].getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            label.getStyleClass().add("calendar-header");
            calendarGrid.add(label, i, 0);
        }
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int startDay = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int column = startDay;
        int row = 1;
        for (int day=1; day<=daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("calendar-day");
            if (logs.containsKey(date)) {
                TextualDataGetter log = logs.get(date);
                boolean hasEntry = isNotEmpty(log.getOverview()) || isNotEmpty(log.getTodo()) || isNotEmpty(log.getGoals()) || isNotEmpty(log.getGratitude());
                if (hasEntry){
                    dayLabel.getStyleClass().add("calendar-day-filled");
                    dayLabel.setOnMouseClicked(event -> showLogDetailsPopup(log));
                }
            }
            calendarGrid.add(dayLabel, column, row);
            column++;
            if (column > 6){
                column = 0;
                row++;
            }
        }
    }

    private boolean isNotEmpty(String text){
        return text != null && !text.trim().isEmpty();
    }

    private void showLogDetailsPopup(TextualDataGetter log){
        String content = "Things To Make The Day Special: " + "\n" + log.getOverview() + "\n" + "Today's To-Do List: " + "\n" + log.getTodo() + "\n" + "Goals To Keep Me Going: " + "\n" + log.getGoals() + "\n" + "Grateful Thoughts: " + "\n" + log.getGratitude();
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Daily Thoughts");
        alert.setHeaderText("Entries for " + log.getDate());
        alert.setContentText(content);
        alert.showAndWait();
    }

}
