package dk.simonsejse.loenberegning.database;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
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
    public static LocalDate toLocalDate(Long dateLong){
        if (dateLong == null) {
            return null;
        } else {
            return Instant.ofEpochSecond(dateLong).atZone(TimeZone.getDefault().toZoneId()).toLocalDate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static Long fromLocalDate(LocalDate date){
        if (date == null) {
            return null;
        } else {
            return date.atStartOfDay(TimeZone.getDefault().toZoneId()).toEpochSecond();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalTime toLocalTime(String timeAsString){
        return timeAsString == null ? null : LocalTime.parse(timeAsString, DateUtil.TIME);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static String fromLocalTime(LocalTime localTime){
        return localTime == null ? null : localTime.toString();
    }
}
