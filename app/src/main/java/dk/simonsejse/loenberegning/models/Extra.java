package dk.simonsejse.loenberegning.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Extra {
    public LocalDateTime start;
    public LocalDateTime end;
    public Integer integer;

    public Extra(LocalDateTime start, LocalDateTime end, Integer integer) {
        this.start = start;
        this.end = end;
        this.integer = integer;
    }
}
