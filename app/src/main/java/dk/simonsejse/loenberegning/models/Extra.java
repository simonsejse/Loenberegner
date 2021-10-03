package dk.simonsejse.loenberegning.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dk.simonsejse.loenberegning.utilities.DateUtil;


public class Extra {
    @JsonProperty(value="start")
    public LocalDateTime start;
    @JsonProperty(value="end")
    public LocalDateTime end;
    @JsonProperty(value="integer")
    public Integer integer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Extra(LocalDateTime start, LocalDateTime end, Integer integer) {
        this.start = start;
        this.end = end;
        this.integer = integer;
    }
    //Dummy constructor or jackson mapping won't work xD
    public Extra(){

    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }
}
