package dk.simonsejse.loenberegning.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Shift.class}, version = 1)
@TypeConverters({DateConverters.class, ExtraConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShiftDataAccessObject shiftDao();
}
