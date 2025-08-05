package ib.ia_programme.others;

import java.time.LocalDate;

public class TextualDataGetter {

    private final LocalDate entry_date;
    private final String overview;
    private final String todo;
    private final String goals;
    private final String gratitude;

    public TextualDataGetter(LocalDate entry_date, String overview, String todo, String goals, String gratitude){
        this.entry_date = entry_date;
        this.overview = overview;
        this.todo = todo;
        this.goals = goals;
        this.gratitude = gratitude;
    }

    public LocalDate getDate(){
        return entry_date;
    }

    public String getOverview(){
        return overview;
    }

    public String getTodo(){
        return todo;
    }

    public String getGoals(){
        return goals;
    }

    public String getGratitude(){
        return gratitude;
    }

    public boolean isEmpty(){
        return (overview == null || overview.isBlank()) && (todo == null || todo.isBlank()) && (goals == null || goals.isBlank()) && (gratitude == null || gratitude.isBlank());
    }
}
