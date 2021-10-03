package dk.simonsejse.loenberegning.utilities;

import static dk.simonsejse.loenberegning.utilities.DateUtil.*;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.exceptions.ExtraAdditionDateBeforeAfterShiftException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ParseUtil {

    /**
     *
     * @param start String to be parsed as LocalTime as to when the shift starts
     * @param end  String to be parsed as LocalTime as to when the shift ends
     * @return Shift object
     * @throws DateTimeParseException If Strings couldn't be parsed to dates
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Shift parseToShift(String start, String end) throws DateTimeParseException, ExtraAdditionDateBeforeAfterShiftException{
        try{
            final LocalDateTime startTime = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
            final LocalDateTime endTime = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
            if (endTime.isBefore(startTime))
                throw new ExtraAdditionDateBeforeAfterShiftException("Sluttidspunkt er før starttidspunkt?");
            return new Shift(startTime, endTime);
        }catch(DateTimeParseException e) {
            throw new DateTimeParseException(MessagesUtil.COULD_NOT_PARSE_DATES_TO_SHIFT, "", 0);
        }
    }

    /**
     *
     * @param localDateTime String to be parsed as LocalTime as to when the date starts/ends
     * @return LocalDateTime object
     * @throws DateTimeParseException if the String couldn't be parsed as a LocalTime object
     */
    public static LocalDateTime parseToLocalDateTime(String localDateTime) throws DateTimeParseException{
        try{
            return LocalDateTime.parse(localDateTime, DateUtil.DATE_TIME_FORMATTER);
        }catch(DateTimeParseException dateTimeParseException){
            throw new DateTimeParseException(MessagesUtil.LOCAL_DATES_COULD_NOT_BE_PARSED, "", 0);
        }
    }

}
