package ib.ia_programme.others;

import ib.ia_programme.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Random;

public class MotivationalMessagesCreator {
    private static final String SQLMOOD = "SELECT mood_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLANXIETY = "SELECT anxiety_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLPHYSICAL = "SELECT physical_scale FROM daily_entries WHERE entry_date = ?";

    private static final String DEFAULT_MESSAGE = "Welcome to your mood tracker!";

    private static int calculateAverage(String sql, String columnName){
        LocalDate today = LocalDate.now();
        int total = 0;
        int validDays = 0;
        for (int i=0; i<7; i++) {
            try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setDate(1, java.sql.Date.valueOf(today.minusDays(i)));
                var resultSet = statement.executeQuery();
                if (resultSet.next()){
                    int value = resultSet.getInt(columnName);
                    if (value > 0){
                        total += value;
                        validDays++;
                    }
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
        if (validDays > 0){
            return total / validDays;
        } else{
            return 0;
        }
    }

    public static int mood(){
        return calculateAverage(SQLMOOD, "mood_scale");
    }

    public static int anxiety(){
        return calculateAverage(SQLANXIETY, "anxiety_scale");
    }

    public static int physical(){
        return calculateAverage(SQLPHYSICAL, "physical_scale");
    }

    public static String getMessageQuery(String tableName){
        return "SELECT message FROM " + tableName + " WHERE entry_id = ?";
    }

    private static String fetchMessage(String tableName, int entryID){
        try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery(tableName))){
            statement.setInt(1, entryID);
            var resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getString("message");
            } else{
                return DEFAULT_MESSAGE;
            }
        } catch (Exception e){
            System.out.println(e);
            return DEFAULT_MESSAGE;
        }
    }

    public static String messageCreator(){
        Random r = new Random();
        int scaleType = r.nextInt(3);
        int weeklyAverage = 0;
        String message = DEFAULT_MESSAGE;
        int randomEntryID = r.nextInt(30) + 1;
        switch (scaleType){
            case 0:
                weeklyAverage = mood();
                if (weeklyAverage > 0) {
                    String tableName = getTableName("mood", weeklyAverage);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
            case 1:
                weeklyAverage = anxiety();
                if (weeklyAverage > 0) {
                    String tableName = getTableName("anxiety", weeklyAverage);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
            case 2:
                weeklyAverage = physical();
                if (weeklyAverage > 0) {
                    String tableName = getTableName("physical", weeklyAverage);
                    message = fetchMessage(tableName, randomEntryID);
                }
                break;
        }
        return message;
    }

    private static String getTableName(String scaleType, int average){
        String suffix;
        switch (scaleType){
            case "mood":
            case "physical":
                if (average > 4){
                    suffix = "_very_good";
                } else if (average >= 3){
                    suffix = "_neutral";
                } else{
                    suffix = "_poor";
                }
                break;
            case "anxiety":
                if (average < 2){
                    suffix = "_very_good";
                } else if (average <= 3){
                    suffix = "_neutral";
                } else{
                    suffix = "_poor";
                }
                break;
            default:
                suffix = "_neutral";
        }
        return scaleType + suffix;
    }
}