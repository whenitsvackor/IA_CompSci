package ib.ia_programme.others;

import ib.ia_programme.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Random;

public class MotivationalMessagesCreator{
    // SQL queries for retrieving scale values from database
    private static final String SQLMOOD = "SELECT mood_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLANXIETY = "SELECT anxiety_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLPHYSICAL = "SELECT physical_scale FROM daily_entries WHERE entry_date = ?";

    private static final String DEFAULT_MESSAGE = "Welcome to your mood tracker!";

    private static int calculateWeeklyAverage(String sql, String columnName) {
        LocalDate today = LocalDate.now();
        int total = 0;
        int validDays = 0;
        
        for (int i = 0; i < 7; i++) {
            try (Connection connection = DBConnection.getConnection(); 
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(today.minusDays(i)));
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int value = resultSet.getInt(columnName);
                    if (value > 0) { // Assuming valid values are positive
                        total += value;
                        validDays++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Database error while calculating average: " + e.getMessage());
            }
        }
        
        return validDays > 0 ? total / validDays : 0;
    }

    public static int mood() {
        return calculateWeeklyAverage(SQLMOOD, "mood_scale");
    }

    public static int anxiety() {
        return calculateWeeklyAverage(SQLANXIETY, "anxiety_scale");
    }

    public static int physical() {
        return calculateWeeklyAverage(SQLPHYSICAL, "physical_scale");
    }

    public static String getMessageQuery(String tableName) {
        return "SELECT message FROM " + tableName + " WHERE entry_id = ?";
    }

    private static String fetchMessage(String tableName, int entryID) {
        try (Connection connection = DBConnection.getConnection(); 
             PreparedStatement statement = connection.prepareStatement(getMessageQuery(tableName))) {
            statement.setInt(1, entryID);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("message");
            } else {
                System.out.println("No message found for entry_id: " + entryID + " in " + tableName);
                return DEFAULT_MESSAGE;
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return DEFAULT_MESSAGE;
        }
    }

    public static String messageCreator(){
        Random rand = new Random();
        int scaleChoice = rand.nextInt(3);
        int weeklyAvg = 0;
        String message = DEFAULT_MESSAGE;
        int randomEntryID = rand.nextInt(30) + 1;  // Random message from available entries
        
        switch (scaleChoice) {
            case 0:
                weeklyAvg = mood();
                if (weeklyAvg > 0) {
                    String tableName = getTableName("mood", weeklyAvg);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
            case 1:
                weeklyAvg = anxiety();
                if (weeklyAvg > 0) {
                    String tableName = getTableName("anxiety", weeklyAvg);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
            case 2:
                weeklyAvg = physical();
                if (weeklyAvg > 0) {
                    String tableName = getTableName("physical", weeklyAvg);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
        }
        return message;
    }
    
    private static String getTableName(String scaleType, int average) {
        String suffix;
        
        switch (scaleType) {
            case "mood":
            case "physical":
                if (average > 4) {
                    suffix = "_very_good";
                } else if (average >= 3) {
                    suffix = "_neutral";
                } else {
                    suffix = "_poor";
                }
                break;
            case "anxiety":
                if (average < 2) {
                    suffix = "_very_good";
                } else if (average <= 3) {
                    suffix = "_neutral";
                } else {
                    suffix = "_poor";
                }
                break;
            default:
                suffix = "_neutral";
        }
        
        return scaleType + suffix;
    }
}