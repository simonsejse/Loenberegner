package dk.simonsejse.loenberegning.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dk.simonsejse.loenberegning.models.Extra;


@Entity(tableName = "shift")
public class Shift implements Serializable {

    public Shift(LocalDateTime shiftStartAt, LocalDateTime shiftEndsAt){
        this.shiftStartAt = shiftStartAt;
        this.shiftEndsAt = shiftEndsAt;
        this.extras = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Shift(){
    }

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "shift_start")
    private LocalDateTime shiftStartAt;

    @ColumnInfo(name = "shift_end")
    private LocalDateTime shiftEndsAt;

    @ColumnInfo(name="extra_salary")
    private List<Extra> extras;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean addExtras(Extra extra){
        if (extra.start.isAfter(shiftStartAt) && extra.end.isBefore(shiftEndsAt.minusSeconds(1))) return extras.add(extra);
        return false;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    public List<Extra> getExtras() {
        return extras;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getShiftStartAt() {
        return shiftStartAt;
    }

    public void setShiftStartAt(LocalDateTime shiftStartAt) {
        this.shiftStartAt = shiftStartAt;
    }

    public LocalDateTime getShiftEndsAt() {
        return shiftEndsAt;
    }

    public void setShiftEndsAt(LocalDateTime shiftEndsAt) {
        this.shiftEndsAt = shiftEndsAt;
    }
}
