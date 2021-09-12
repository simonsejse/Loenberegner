package dk.simonsejse.loenberegning.models;

import java.time.LocalTime;

public class Extra {
    public LocalTime start;
    public LocalTime end;
    public Integer integer;

    public Extra(LocalTime start, LocalTime end, Integer integer) {
        this.start = start;
        this.end = end;
        this.integer = integer;
    }
}
