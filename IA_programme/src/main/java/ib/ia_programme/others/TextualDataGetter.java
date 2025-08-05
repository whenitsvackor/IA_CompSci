package ib.ia_programme.others;

import java.time.LocalDate;

public class TextualDataGetter {
    private final LocalDate entryDate;
    private final String overview;
    private final String todo;
    private final String goals;
    private final String gratitude;

    public TextualDataGetter(LocalDate entryDate, String overview, String todo, String goals, String gratitude) {
        this.entryDate = entryDate;
        this.overview = overview;
        this.todo = todo;
        this.goals = goals;
        this.gratitude = gratitude;
    }

    public LocalDate getDate() {
        return entryDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getTodo() {
        return todo;
    }

    public String getGoals() {
        return goals;
    }

    public String getGratitude() {
        return gratitude;
    }

    public boolean isEmpty() {
        return isBlankOrNull(overview) && 
               isBlankOrNull(todo) && 
               isBlankOrNull(goals) && 
               isBlankOrNull(gratitude);
    }
    
    private boolean isBlankOrNull(String text) {
        return text == null || text.isBlank();
    }
}
