package dk.simonsejse.loenberegning.utilities;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

import dk.simonsejse.loenberegning.database.Shift;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ParseUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Shift parseToShift(String work, String start, String end){
        try{
            final LocalDate workDate = LocalDate.parse(work, DateUtil.DATES);
            final LocalTime startTime = LocalTime.parse(start, DateUtil.TIME);
            final LocalTime endTime = LocalTime.parse(end, DateUtil.TIME);
            return new Shift(workDate, startTime, endTime);
        }catch(DateTimeParseException dateTimeParseException){
            dateTimeParseException.printStackTrace();
           return null;
        }
    }

}
