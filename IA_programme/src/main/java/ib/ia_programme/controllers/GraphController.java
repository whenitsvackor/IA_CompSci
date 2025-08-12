package ib.ia_programme.controllers;

import java.util.List;

import ib.ia_programme.others.NumericDataGetter;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.chart.XYChart;
import ib.ia_programme.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GraphController {
    // ...existing fields...

    @FXML
    private VBox moodGraphContainer;
    @FXML
    private VBox anxietyGraphContainer;
    @FXML
    private VBox physicalGraphContainer;
    @FXML
    private CheckBox predictionCheck;
    @FXML
    private Button moodGraphButton;
    @FXML
    private Button anxietyGraphButton;
    @FXML
    private Button physicalGraphButton;

    private boolean moodGraphVisible = false;
    private boolean anxietyGraphVisible = false;
    private boolean physicalGraphVisible = false;

    @FXML 
    public void onMoodGraphButtonClick() {
        moodGraphVisible = !moodGraphVisible;
        if (moodGraphVisible) {
            moodGraphContainer.getChildren().clear();
            LineChart<String, Number> chart = createGraph("mood_scale", predictionCheck);
            applyChartSizing(chart, moodGraphContainer, 168);
            moodGraphContainer.getChildren().add(chart);
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
            LineChart<String, Number> chart = createGraph("anxiety_scale", predictionCheck);
            applyChartSizing(chart, anxietyGraphContainer, 168);
            anxietyGraphContainer.getChildren().add(chart);
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
            LineChart<String, Number> chart = createGraph("physical_scale", predictionCheck);
            applyChartSizing(chart, physicalGraphContainer, 168);
            physicalGraphContainer.getChildren().add(chart);
            physicalGraphContainer.setVisible(true);
            physicalGraphContainer.setManaged(true);
            physicalGraphButton.setText("Hide Physical Wellbeing Log");
        } else {
            physicalGraphContainer.setVisible(false);
            physicalGraphContainer.setManaged(false);
            physicalGraphButton.setText("Physical Wellbeing Log");
        }
    }

    private void applyChartSizing(LineChart<String, Number> chart, VBox container, double prefHeight) {
        container.setFillWidth(true);
        chart.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        chart.setPrefHeight(prefHeight);
        chart.setMaxWidth(Double.MAX_VALUE);
        chart.prefWidthProperty().bind(container.widthProperty());
        VBox.setVgrow(chart, Priority.NEVER);
    }

    public LineChart<String, Number> createGraph(String scaleType, CheckBox showPrediction) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(1, 5, 1);
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        
        // Aesthetics and proportion
        chart.getStyleClass().add("tracker-line-chart");
        chart.setCreateSymbols(true);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(false);
        
        xAxis.setTickLabelRotation(45);
        xAxis.setTickLabelGap(5);
        yAxis.setMinorTickVisible(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(1);
        yAxis.setUpperBound(5);
        yAxis.setTickUnit(1);
        
        try {
            List<NumericDataGetter> data = getScaleData(scaleType);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            
            for (NumericDataGetter point : data) {
                if (point.getValue() > 0) {
                    series.getData().add(new XYChart.Data<>(point.getDate().toString(), point.getValue()));
                }
            }
            chart.getData().add(series);
            
            if (showPrediction.isSelected() && !series.getData().isEmpty()) {
                addPrediction(series.getData(), chart);
            }
        } catch (Exception e) {
            System.err.println("Error creating graph: " + e.getMessage());
        }
        return chart;
    }

    // Fetch timeseries data for a given scale from the database
    public List<NumericDataGetter> getScaleData(String scaleType) throws SQLException {
        List<NumericDataGetter> data = new ArrayList<>();
        String sql = "SELECT entry_date, " + scaleType + " FROM daily_entries ORDER BY entry_date";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int value = resultSet.getInt(scaleType);
                LocalDate date = resultSet.getDate("entry_date").toLocalDate();
                if (value > 0) {
                    data.add(new NumericDataGetter(date, value));
                }
            }
        }
        return data;
    }

    // Add a simple linear regression prediction line for the next 7 days
    private void addPrediction(List<XYChart.Data<String, Number>> actualData, LineChart<String, Number> chart) {
        if (actualData.size() < 2) return;
        
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
        double denom = (n * sumX2 - sumX * sumX);
        if (denom == 0) return;
        double slope = (n * sumXY - sumX * sumY) / denom;
        double intercept = (sumY - slope * sumX) / n;
        
        XYChart.Series<String, Number> predictionSeries = new XYChart.Series<>();
        LocalDate lastDate = LocalDate.parse(actualData.get(n - 1).getXValue());
        for (int i = 1; i <= 7; i++) {
            LocalDate futureDate = lastDate.plusDays(i);
            double predictedValue = slope * (n + i - 1) + intercept;
            predictionSeries.getData().add(new XYChart.Data<>(futureDate.toString(), predictedValue));
        }
        chart.getData().add(predictionSeries);
    }

    // ...existing methods...
}