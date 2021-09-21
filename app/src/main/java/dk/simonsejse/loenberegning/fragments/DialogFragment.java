package dk.simonsejse.loenberegning.fragments;

import static dk.simonsejse.loenberegning.utilities.DateUtil.DATE_FORMAT_WITH_SLASH;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FullDialogFragmentForAddingAdditionBinding;
import dk.simonsejse.loenberegning.exceptions.ExtraAdditionDateBeforeAfterShiftException;
import dk.simonsejse.loenberegning.models.IResponseDataFromDialogFragment;
import dk.simonsejse.loenberegning.models.ResponseDataFromDialogFragmentModel;
import dk.simonsejse.loenberegning.utilities.AlertUtil;
import dk.simonsejse.loenberegning.utilities.DateUtil;
import dk.simonsejse.loenberegning.utilities.MessagesUtil;
import dk.simonsejse.loenberegning.utilities.ParseUtil;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private FullDialogFragmentForAddingAdditionBinding binding;

    private final IResponseDataFromDialogFragment iResponseDataFromDialogFragment;
    private final Shift shift;

    public DialogFragment(IResponseDataFromDialogFragment iResponseDataFromDialogFragment, Shift shift){
        this.iResponseDataFromDialogFragment = iResponseDataFromDialogFragment;
        this.shift = shift;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FullDialogFragmentForAddingAdditionBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public int getTheme() {
        return R.style.DialogTheme;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.binding.additionStartTw.setOnClickListener((listener -> {
            DateUtil.openSelectableDatePicker(
                             this,
                             new LocalDateTime[]{
                                     shift.getShiftStartAt(),
                                     shift.getShiftEndsAt()
                             }, (positiveDate) -> {
                                 final String format = Instant.ofEpochMilli(positiveDate).atZone(TimeZone.getDefault().toZoneId()).format(DATE_FORMAT_WITH_SLASH);
                                 this.binding.additionStartTw.setText(format);

                                 DateUtil.openTimePicker(
                                         this,
                                         "Vælg tillæg tidspunkt",
                                         (materialTimePicker) -> {
                                             final String time = String.format(" %02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute());
                                            this.binding.additionStartTw.append(time);
                                         }, (cancel) -> {
                                             this.binding.additionStartTw.getText().clear();
                                         },
                                         (onNegativeClickListener) -> {
                                             this.binding.additionStartTw.getText().clear();
                                         }
                                 );
                             },
                             null,
                             null
                     );
        }));

        this.binding.additionEndTw.setOnClickListener((listener -> {
            DateUtil.openSelectableDatePicker(
                    this,
                    new LocalDateTime[]{
                            shift.getShiftStartAt(),
                            shift.getShiftEndsAt()
                    }, (positiveDate) -> {
                        final String format = Instant.ofEpochMilli(positiveDate).atZone(TimeZone.getDefault().toZoneId()).format(DATE_FORMAT_WITH_SLASH);
                        this.binding.additionEndTw.setText(format);

                        DateUtil.openTimePicker(
                                this,
                                "Vælg tillæg tidspunkt",
                                (materialTimePicker) -> {
                                    final String time = String.format(" %02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute());
                                    this.binding.additionEndTw.append(time);
                                }, (cancel) -> {
                                    this.binding.additionEndTw.getText().clear();
                                },
                                (onNegativeClickListener) -> {
                                    this.binding.additionEndTw.getText().clear();
                                }
                        );
                    },
                    null,
                    null
            );
        }));
        this.binding.addExtraAdditionBtn.setOnClickListener(this::addNewExtraAdditionToShift);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNewExtraAdditionToShift(View view) {
        //Continue
        LocalDateTime extraStart;
        LocalDateTime extraEnd;
        try{
            extraStart = ParseUtil.parseToLocalDateTime(this.binding.additionStartTw.getText().toString());
            extraEnd = ParseUtil.parseToLocalDateTime(this.binding.additionEndTw.getText().toString());
            int amount = Integer.parseInt(this.binding.additionTextView.getText().toString().trim());

            if (shift.getShiftStartAt().isBefore(extraStart.plusSeconds(1)) && shift.getShiftEndsAt().isAfter(extraEnd.minusSeconds(1))){
                iResponseDataFromDialogFragment.callback(new ResponseDataFromDialogFragmentModel(
                        extraStart, extraEnd, amount
                ));
            } else throw new ExtraAdditionDateBeforeAfterShiftException(MessagesUtil.EXTRA_ADDITION_HAS_TO_BE_BETWEEN_SHIFT);
        }catch(DateTimeParseException dateTimeParseException){
            AlertUtil.send(view, dateTimeParseException.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
        }catch(NumberFormatException e){
            AlertUtil.send(view, MessagesUtil.NUMBER_FORMAT_EXCEPTION_ERROR_MESSAGE, BaseTransientBottomBar.LENGTH_LONG);
        }catch(ExtraAdditionDateBeforeAfterShiftException e){
            AlertUtil.send(view, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG);
        }
        //dismiss(); won't let alertutil work fix that l8
    }



}
