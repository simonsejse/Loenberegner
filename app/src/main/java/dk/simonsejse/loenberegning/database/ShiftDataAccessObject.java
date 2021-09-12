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

    @Query("SELECT EXISTS(SELECT * FROM shift WHERE workDate = :workDate)")
    boolean doesShiftExist(LocalDate workDate);

    @Query("DELETE FROM shift WHERE workDate = :workDate")
    int deleteShift(LocalDate workDate);

    @Query("UPDATE shift SET workDate=:newLocalDate, shift_start=:shiftStart, shift_end=:shiftEnd WHERE workDate=:oldLocalDate")
    int updateShift(LocalDate oldLocalDate, LocalDate newLocalDate, LocalTime shiftStart, LocalTime shiftEnd);
}
