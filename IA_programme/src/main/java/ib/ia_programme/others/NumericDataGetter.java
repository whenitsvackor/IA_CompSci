package ib.ia_programme.others;

import java.time.LocalDate;

public class NumericDataGetter {
    private LocalDate date;
    private Integer value;

    public NumericDataGetter(LocalDate date, Integer value){
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate(){
        return date;
    }

    public Integer getValue(){
        return value;
    }
}
