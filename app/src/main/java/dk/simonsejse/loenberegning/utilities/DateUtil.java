package dk.simonsejse.loenberegning.utilities;

import android.content.DialogInterface;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Consumer;

import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.models.ITimePickerPositiveClickListenerCallback;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM-yyyy HH:mm");
    public static final DateTimeFormatter DATE_FORMAT_WITH_DASH = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter DATE_FORMAT_WITH_SLASH = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    protected enum DateEnumPicker{
        START_DATE,
        END_DATE,
    }

    public static void openDatePicker(
            Fragment fragment,
            String title,
            MaterialPickerOnPositiveButtonClickListener<? super Long> onPositiveButtonClickListener,
            View.OnClickListener onNegativeButtonClickListener,
            DialogInterface.OnCancelListener onCancelListener
    ){
        final MaterialDatePicker<Long> build = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(title)
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        build.addOnPositiveButtonClickListener(onPositiveButtonClickListener);
        build.addOnNegativeButtonClickListener(onNegativeButtonClickListener);
        build.addOnCancelListener(onCancelListener);

        build.show(fragment.getParentFragmentManager(), build.getTag());
    }

    public static void openSelectableDatePicker(
            Fragment fragment,
            LocalDate[] dates,
            MaterialPickerOnPositiveButtonClickListener<? super Long> onPositiveButtonClickListener,
            View.OnClickListener onNegativeButtonClickListener,
            DialogInterface.OnCancelListener onCancelListener
            ){

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward
                .from(dates[DateEnumPicker.START_DATE.ordinal()].atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward
                .before(dates[DateEnumPicker.END_DATE.ordinal()].atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        ArrayList<CalendarConstraints.DateValidator> listValidators = new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(dateValidatorMin);
        listValidators.add(dateValidatorMax);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);

        builder.setCalendarConstraints(constraintsBuilderRange.build());

        MaterialDatePicker<Long> picker = builder.build();
        picker.show(fragment.getParentFragmentManager(), picker.toString());
        if (onPositiveButtonClickListener != null)
            picker.addOnPositiveButtonClickListener(onPositiveButtonClickListener);
        if (onNegativeButtonClickListener != null)
            picker.addOnNegativeButtonClickListener(onNegativeButtonClickListener);
        if (onCancelListener != null)
            picker.addOnCancelListener(onCancelListener);
    }



    public static void openTimePicker(
            Fragment fragment,
            String title,
            ITimePickerPositiveClickListenerCallback iTimePickerPositiveClickListenerCallback,
            DialogInterface.OnCancelListener onCancelListener,
            View.OnClickListener onNegativeClickListener
    ){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        final MaterialTimePicker build = new MaterialTimePicker
                .Builder()
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute)
                .setTitleText(title)
                .build();

        build.show(fragment.getParentFragmentManager(), build.getTag());
        if (onCancelListener != null)
            build.addOnPositiveButtonClickListener((view) -> {
                iTimePickerPositiveClickListenerCallback.callback(build);
            });
        if (onCancelListener != null)
            build.addOnCancelListener(onCancelListener);
        if (onNegativeClickListener != null)
            build.addOnNegativeButtonClickListener(onNegativeClickListener);
    }

    public static void clearTextField(TextInputEditText textInputEditText) {
        if (textInputEditText.getText() != null)
            textInputEditText.getText().clear();
    }

    public static int getSimonCurrentAge(){
        return LocalDate.now().getYear() - LocalDate.of(2001, 02, 14).getYear();
    }
}
