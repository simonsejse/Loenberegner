package dk.simonsejse.loenberegning.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dk.simonsejse.loenberegning.models.Extra;


@Entity(tableName = "shift")
public class Shift implements Serializable {

    public Shift(LocalDate date, LocalTime shiftStartAt, LocalTime shiftEndsAt){
        this.workDate = date;
        this.shiftStartAt = shiftStartAt;
        this.shiftEndsAt = shiftEndsAt;
        this.extras = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Shift(){
    }

    @PrimaryKey
    private LocalDate workDate;
    @ColumnInfo(name = "shift_start")
    private LocalTime shiftStartAt;

    @ColumnInfo(name = "shift_end")
    private LocalTime shiftEndsAt;

    @ColumnInfo(name="extra_salary")
    private List<Extra> extras;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addExtras(Extra extra){
        if (shiftStartAt.isBefore(extra.start.plusSeconds(1)) && shiftEndsAt.isAfter(extra.end.minusSeconds(1))) return extras.add(extra);
        return false;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getShiftStartAt() {
        return shiftStartAt;
    }

    public void setShiftStartAt(LocalTime shiftStartAt) {
        this.shiftStartAt = shiftStartAt;
    }

    public LocalTime getShiftEndsAt() {
        return shiftEndsAt;
    }

    public void setShiftEndsAt(LocalTime shiftEndsAt) {
        this.shiftEndsAt = shiftEndsAt;
    }
}
