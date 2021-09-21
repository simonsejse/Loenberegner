package dk.simonsejse.loenberegning.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Dao
public interface ShiftDataAccessObject {
    @Query("SELECT * from shift")
    List<Shift> getAllShifts();

    @Query("DELETE FROM shift")
    void nukeTable();

    @Insert
    void insertShift(Shift shift);

    @Query("SELECT EXISTS(SELECT * FROM shift WHERE shift_start = :shift_start)")
    boolean doesShiftExist(LocalDateTime shift_start);

    @Query("DELETE FROM shift WHERE id = :id")
    int deleteShift(long id);

    @Query("UPDATE shift SET shift_start=:shiftStart, shift_end=:shiftEnd WHERE id=:id")
    int updateShift(long id, LocalDateTime shiftStart, LocalDateTime shiftEnd);
}
