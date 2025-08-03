package ib.ia_programme.others;

import ib.ia_programme.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Random;

public class MotivationalMessagesCreator{
    private static final String SQLMOOD = "SELECT mood_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLANXIETY = "SELECT anxiety_scale FROM daily_entries WHERE entry_date = ?";
    private static final String SQLPHYSICAL = "SELECT physical_scale FROM daily_entries WHERE entry_date = ?";

    public static int mood(){
        LocalDate today = LocalDate.now();
        int moodAverage = 0;
        int divider = 7;
        for (int i=0; i<7; i++){
            int mood = -1;
            try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SQLMOOD)){
                statement.setDate(1, java.sql.Date.valueOf(today.minusDays(i)));
                var resultSet = statement.executeQuery();
                if (resultSet.next()){
                    mood = resultSet.getInt("mood_scale");
                }
                if (mood == -1){
                    divider--;
                }
                else{
                    moodAverage = moodAverage + mood;
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        if (divider == 0){
            return 0;
        }
        else {
            moodAverage = moodAverage / divider;
            return moodAverage;
        }
    }

    public static int anxiety(){
        LocalDate today = LocalDate.now();
        int anxietyAverage = 0;
        int divider = 7;
        for (int i=0; i<7; i++){
            int anxiety = -1;
            try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SQLANXIETY)){
                statement.setDate(1, java.sql.Date.valueOf(today.minusDays(i)));
                var resultSet = statement.executeQuery();
                if (resultSet.next()){
                    anxiety = resultSet.getInt("anxiety_scale");
                }
                if (anxiety == -1){
                    divider--;
                }
                else{
                    anxietyAverage = anxietyAverage + anxiety;
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
        if (divider == 0){
            return 0;
        }
        else {
            anxietyAverage = anxietyAverage / divider;
            return anxietyAverage;
        }
    }

    public static int physical(){
        LocalDate today = LocalDate.now();
        int physicalAverage = 0;
        int divider = 7;
        for (int i=0; i<7; i++){
            int physical = -1;
            try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(SQLPHYSICAL)){
                statement.setDate(1, java.sql.Date.valueOf(today.minusDays(i)));
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    physical = resultSet.getInt("physical_scale");
                }
                if (physical == -1){
                    divider--;
                }
                else{
                    physicalAverage = physicalAverage + physical;
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
        if (divider == 0){
            return 0;
        }
        else {
            physicalAverage = physicalAverage / divider;
            return physicalAverage;
        }
    }

    public static String getMessageQuery(String tableName) {
        return "SELECT message FROM " + tableName + " WHERE entry_id = ?";
    }

    public static String messageCreator(){
        Random r = new Random();
        int scaleType = r.nextInt(3);
        int average = 0;
        String message = "Welcome to your mood tracker!";
        int entryID = r.nextInt(30) + 1;
        if (scaleType == 0){
            average = mood();
            if (average == 0){
                message = "Welcome to your mood tracker!";
            }
            else{
                if (average > 4){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("mood_very_good"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in goodmood");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (4 >= average && average >= 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("mood_neutral"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in neutralmood");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (average < 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("mood_poor"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in poormood");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        }
        if (scaleType == 1){
            average = anxiety();
            if (average == 0){
                message = "Welcome to your mood tracker!";
            }
            else{
                if (average < 2){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("anxiety_very_good"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in goodanxiety");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (2 <= average && average <= 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("anxiety_neutral"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in neutralanxiety");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (average > 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("anxiety_poor"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in pooranxiety");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        }
        if (scaleType == 2){
            average = physical();
            if (average == 0){
                message = "Welcome to your mood tracker!";
            }
            else{
                if (average > 4){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("physical_very_good"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in goodphysical");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (4 >= average && average >= 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("physical_neutral"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in neutralphysical");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                else if (average < 3){
                    try (Connection connection = DBConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(getMessageQuery("physical_poor"))){
                        statement.setInt(1, entryID);
                        var resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            message = resultSet.getString("message");
                        } else {
                            System.out.println("No result for entry_id: " + entryID + " in poorphysical");
                        }
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        }
        return message;
    }
}
