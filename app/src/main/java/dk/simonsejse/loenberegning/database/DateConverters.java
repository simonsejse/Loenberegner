package dk.simonsejse.loenberegning.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import dk.simonsejse.loenberegning.models.Extra;
import dk.simonsejse.loenberegning.utilities.DateUtil;


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
