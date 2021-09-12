package dk.simonsejse.loenberegning.utilities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateUtil {
    public static final DateTimeFormatter DATES = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    public static int getSimonCurrentAge(){
        return LocalDate.now().getYear() - LocalDate.of(2001, 02, 14).getYear();
    }
}
