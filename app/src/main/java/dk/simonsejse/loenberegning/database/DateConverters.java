package dk.simonsejse.loenberegning.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;


public class DateConverters {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime toLocalDateTime(String timeAsString){
        return timeAsString == null ? null : LocalDateTime.parse(timeAsString);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime localTime){
        return localTime == null ? null : localTime.toString();
    }
}
