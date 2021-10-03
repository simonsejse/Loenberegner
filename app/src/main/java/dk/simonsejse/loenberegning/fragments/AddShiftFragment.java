package dk.simonsejse.loenberegning.fragments;

import static dk.simonsejse.loenberegning.utilities.DateUtil.*;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dk.simonsejse.loenberegning.ShiftApplication;
import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.database.ShiftDataAccessObject;
import dk.simonsejse.loenberegning.databinding.FragmentAddShiftBinding;
import dk.simonsejse.loenberegning.directions.NavigateFromAddShiftToExtraAddition;
import dk.simonsejse.loenberegning.exceptions.ExtraAdditionDateBeforeAfterShiftException;
import dk.simonsejse.loenberegning.utilities.AlertUtil;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.IconUtil;
import dk.simonsejse.loenberegning.utilities.ParseUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddShiftFragment extends Fragment {

    private FragmentAddShiftBinding fragmentAddShiftBinding;
    private NavController navController;
    private Shift shift;

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
        if (getArguments() != null){
            this.shift = (Shift) getArguments().getSerializable(BundleStoringKeyEnum.SHIFT_KEY.key);
            this.fragmentAddShiftBinding.workStartTw.setText(shift.getShiftStartAt().format(DATE_TIME_FORMATTER));
            this.fragmentAddShiftBinding.workEndTw.setText(shift.getShiftEndsAt().format(DATE_TIME_FORMATTER));
        }

        this.fragmentAddShiftBinding.workStartTw.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                DateUtil.openDatePicker(
                    this,
                        "Vælg start dato til vagten",
                        (onPositiveLongDate) -> {
                            final String newDate = Instant.ofEpochMilli(onPositiveLongDate).atZone(TimeZone.getDefault().toZoneId()).format(DATE_FORMAT_WITH_SLASH);
                            this.fragmentAddShiftBinding.workStartTw.setText(newDate);
                            DateUtil.openTimePicker(
                                    this,
                                    "Vælg start tidspunkt til vagten",
                                    (materialTimePickerCallback) -> {
                                        final String time = String.format(" %02d:%02d", materialTimePickerCallback.getHour(), materialTimePickerCallback.getMinute());
                                        this.fragmentAddShiftBinding.workStartTw.append(time);
                                    }, (onCancelListener) -> {
                                        DateUtil.clearTextField(this.fragmentAddShiftBinding.workStartTw);
                                    }, (onNegativeListener) -> DateUtil.clearTextField(this.fragmentAddShiftBinding.workStartTw)
                            );
                        },
                        (onNegativeListener) -> {
                            DateUtil.clearTextField(this.fragmentAddShiftBinding.workStartTw);
                        }, (onCancelListener) -> {
                            DateUtil.clearTextField(this.fragmentAddShiftBinding.workStartTw);
                        }
                );
            }
            return true;
        });

        this.fragmentAddShiftBinding.workEndTw.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                DateUtil.openDatePicker(
                        this,
                        "Vælg slut dato til vagten",
                        (onPositiveLongDate) -> {
                            final String newDate = Instant.ofEpochMilli(onPositiveLongDate).atZone(TimeZone.getDefault().toZoneId()).format(DATE_FORMAT_WITH_SLASH);
                            this.fragmentAddShiftBinding.workEndTw.setText(newDate);
                            DateUtil.openTimePicker(
                                    this,
                                    "Vælg slut tidspunkt til vagten",
                                    (materialTimePickerCallback) -> {
                                        final String time = String.format(" %02d:%02d", materialTimePickerCallback.getHour(), materialTimePickerCallback.getMinute());
                                        this.fragmentAddShiftBinding.workEndTw.append(time);
                                    },
                                    (onCancelListener) -> {
                                        DateUtil.clearTextField(this.fragmentAddShiftBinding.workEndTw);
                                    },
                                    (onNegativeListener) -> {
                                        DateUtil.clearTextField(this.fragmentAddShiftBinding.workEndTw);
                                    }
                            );
                        }, (onNegativeListener) -> {
                            DateUtil.clearTextField(this.fragmentAddShiftBinding.workEndTw);
                        },(onCancelListener) -> {
                            DateUtil.clearTextField(this.fragmentAddShiftBinding.workEndTw);
                        }
                );
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
        final String startTimeAsString = this.fragmentAddShiftBinding.workStartTw.getText().toString();
        final String endTimeAsString = this.fragmentAddShiftBinding.workEndTw.getText().toString();

        CompletableFuture<String> responseFromDatabaseCall = CompletableFuture.supplyAsync(() -> {
            final ShiftDataAccessObject shiftDataAccessObject = ShiftApplication.database.shiftDao();
            if (this.shift == null){
                try{
                    this.shift = ParseUtil.parseToShift(startTimeAsString, endTimeAsString);
                }catch(DateTimeParseException | ExtraAdditionDateBeforeAfterShiftException message){
                    return message.getMessage();
                }
            }
            shiftDataAccessObject.insertShift(this.shift);
            return "Tilføjet ny vagt!";
        });
        try{
            final String responseMessage = responseFromDatabaseCall.get();
            AlertUtil.send(view, "Ny vagt tilføjet!",  responseMessage, IconUtil.ADDED);
            this.navController.navigate(R.id.navigateFromAddShiftToHome);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAddShiftBinding = null;
    }

    private void goToExtraAdditionFragmentEvent(View view) {
        final String workStartText = this.fragmentAddShiftBinding.workStartTw.getText().toString();
        final String workEndText = this.fragmentAddShiftBinding.workEndTw.getText().toString();
        try{
            final Shift parseToShift = ParseUtil.parseToShift(workStartText, workEndText);

            final boolean sameDate = this.shift != null && this.shift.getShiftStartAt().equals(parseToShift.getShiftStartAt()) && this.shift.getShiftEndsAt().equals(parseToShift.getShiftEndsAt());
            final Shift shiftArgument = sameDate ? this.shift : parseToShift;
            this.navController.navigate(new NavigateFromAddShiftToExtraAddition(shiftArgument));
        }catch(DateTimeParseException | ExtraAdditionDateBeforeAfterShiftException parseException){
            AlertUtil.send(getView(), "Kunne ikke parse data", parseException.getMessage(), IconUtil.ERROR);
        }
    }

    private void popBackStackOnCancelEvent(View view){
        this.navController.popBackStack();
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}