package dk.simonsejse.loenberegning.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.AppDatabase;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.database.ShiftDataAccessObject;
import dk.simonsejse.loenberegning.databinding.FragmentAddShiftBinding;
import dk.simonsejse.loenberegning.utilities.AlertUtil;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.ParseUtil;
import io.reactivex.Completable;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddShiftFragment extends Fragment {

    private FragmentAddShiftBinding fragmentAddShiftBinding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.fragmentAddShiftBinding = FragmentAddShiftBinding.inflate(inflater, container, false);
        this.navController = Navigation.findNavController(getActivity(), R.id.navHostControllerFragment);
        return fragmentAddShiftBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.fragmentAddShiftBinding.datePickerActions.setOnTouchListener(this::openDatePicker);
        this.fragmentAddShiftBinding.workStartTimePicker.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                openTimePicker(v, motionEvent, true);
                return true;
            }
            return false;
        });

        this.fragmentAddShiftBinding.workEndTimePicker.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                openTimePicker(v, motionEvent, false);
                return true;
            }
            return false;
        });
        this.fragmentAddShiftBinding.goToExtraAdditionFragmentButton.setOnClickListener(this::goToExtraAdditionFragmentEvent);
        this.fragmentAddShiftBinding.addShiftButton.setOnClickListener(this::addShiftEvent);
        this.fragmentAddShiftBinding.cancelButton.setOnClickListener(this::popBackStackOnCancelEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addShiftEvent(View view) {
       final String dateAsString = this.fragmentAddShiftBinding.datePickerActions.getText().toString();
       final String workStartTime = this.fragmentAddShiftBinding.workStartTimePicker.getText().toString();
       final String workEndTime = this.fragmentAddShiftBinding.workEndTimePicker.getText().toString();

       CompletableFuture<String> responseFromDatabaseCall = CompletableFuture.supplyAsync(() -> {
           AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
           final ShiftDataAccessObject shiftDataAccessObject = db.shiftDao();
           final LocalDate date;
           final LocalTime startTime;
           final LocalTime endTime;
           try{
               date = LocalDate.parse(dateAsString, DateUtil.DATES).atStartOfDay().atZone(TimeZone.getDefault().toZoneId()).toLocalDate();
               if (shiftDataAccessObject.doesShiftExist(date)){
                   return String.format("Vagten %s findes allerede", dateAsString);
               }
               startTime = LocalTime.parse(workStartTime, DateUtil.TIME);
               endTime = LocalTime.parse(workEndTime, DateUtil.TIME);
           }catch(DateTimeParseException dateTimeParseException){
               return String.format("Noget var galt med dine datoer: %s", dateTimeParseException.getMessage());
           }
           db.shiftDao().insertShift(new Shift(
                   date,
                   startTime,
                   endTime
           ));
           return String.format("Tilføjet ny vagt d. %s", dateAsString);
       });
       try{
           final String responseMessage = responseFromDatabaseCall.get();
           AlertUtil.send(view, responseMessage, BaseTransientBottomBar.LENGTH_LONG);
           this.navController.navigate(R.id.navigateFromAddShiftToHome);
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (ExecutionException e) {
           e.printStackTrace();
       }

    }

    @SuppressLint("DefaultLocale")
    private void openTimePicker(View view, MotionEvent event, boolean isStartPicker){
        if (event.getAction() == MotionEvent.ACTION_UP){
            final MaterialTimePicker build = new MaterialTimePicker
                    .Builder()
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Vælg vagtens tidspunkt")
                    .build();
            build.show(getParentFragmentManager(), build.getTag());

            build.addOnPositiveButtonClickListener((v) -> {
                if (isStartPicker) {
                    this.fragmentAddShiftBinding.workStartTimePicker.setText(String.format("%02d:%02d", build.getHour(), build.getMinute()));
                } else {
                    this.fragmentAddShiftBinding.workEndTimePicker.setText(String.format("%02d:%02d", build.getHour(), build.getMinute()));
                }
            });
        }
    }

    private boolean openDatePicker(View view, MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_UP){
            final MaterialDatePicker<Long> build = MaterialDatePicker.Builder
                    .datePicker()
                    .setTitleText("Vælg dato du var på arbejde")
                    .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            build.show(getParentFragmentManager(), build.getTag());
            build.addOnPositiveButtonClickListener(this::onDateSelectionEvent);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onDateSelectionEvent(Long date){
        final String newDate = Instant.ofEpochMilli(date).atZone(TimeZone.getDefault().toZoneId()).format(DateUtil.DATES);
        this.fragmentAddShiftBinding.datePickerActions.setText(newDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAddShiftBinding = null;
    }

    private void goToExtraAdditionFragmentEvent(View view) {
        final String workText = this.fragmentAddShiftBinding.datePickerActions.getText().toString();
        final String workStartText = this.fragmentAddShiftBinding.workStartTimePicker.getText().toString();
        final String workEndText = this.fragmentAddShiftBinding.workEndTimePicker.getText().toString();
        if (workEndText.trim().isEmpty()
                || this.fragmentAddShiftBinding.workStartTimePicker.getText() == null
                || workEndText.trim().isEmpty()
                || this.fragmentAddShiftBinding.workEndTimePicker.getText() == null
                || workText.trim().isEmpty()
                || this.fragmentAddShiftBinding.datePickerActions.getText() == null){
            AlertUtil.send(view, "Du skal tilføje din vagt timer før du kan tilføje tillæg!", BaseTransientBottomBar.LENGTH_SHORT);
            return;
        }
        Shift shift = ParseUtil.parseToShift(workText, workEndText, workEndText);
        if (shift == null){
            AlertUtil.send(view, "Kunne ikke parse dataen! Der mangler noget!", BaseTransientBottomBar.LENGTH_SHORT);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("shift",  shift);
        this.navController.navigate(R.id.navigateFromAddShiftToExtraFragment, bundle);
    }

    private void popBackStackOnCancelEvent(View view){
        this.navController.popBackStack();
    }
}