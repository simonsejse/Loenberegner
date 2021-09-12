package dk.simonsejse.loenberegning.fragments;

import android.icu.text.RelativeDateTimeFormatter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkRequest;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.BaseTransientBottomBar;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.AppDatabase;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FragmentEditShiftBinding;
import dk.simonsejse.loenberegning.models.EnumDateVarchar;
import dk.simonsejse.loenberegning.models.IOnEditShiftListener;
import dk.simonsejse.loenberegning.utilities.AlertUtil;
import dk.simonsejse.loenberegning.utilities.DateUtil;


@RequiresApi(api = Build.VERSION_CODES.O)
public class EditShiftFragment extends Fragment {

    private FragmentEditShiftBinding fragmentEditShiftBinding;
    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.fragmentEditShiftBinding = FragmentEditShiftBinding.inflate(inflater, container, false);
        final FragmentActivity activity = getActivity();

        if (activity != null){
            this.navController = Navigation.findNavController(activity, R.id.navHostControllerFragment);

            final Bundle arguments = getArguments();
            final View viewById = activity.findViewById(android.R.id.content);
            if (arguments == null){
                AlertUtil.send(viewById, "Kunne ikke finde data for vagten!", BaseTransientBottomBar.LENGTH_LONG);
                navController.popBackStack();
            }else{
                final LocalDate oldWorkDate;
                final LocalTime workStart;
                final LocalTime workEnd;
                try{
                    oldWorkDate = (LocalDate) arguments.getSerializable(EnumDateVarchar.WORK.getText());
                    workStart = (LocalTime) arguments.getSerializable(EnumDateVarchar.WORK_START.getText());
                    workEnd = (LocalTime) arguments.getSerializable(EnumDateVarchar.WORK_END.getText());

                    //Settings TextViews
                    final EditText datePickerActionsEditText = this.fragmentEditShiftBinding.datePickerActions.getEditText();
                    final EditText startTimePickerEditText = this.fragmentEditShiftBinding.workStartTimePicker.getEditText();
                    final EditText workEndTimePickerEditText = this.fragmentEditShiftBinding.workEndTimePicker.getEditText();

                    if (datePickerActionsEditText != null && startTimePickerEditText != null && workEndTimePickerEditText != null){
                        datePickerActionsEditText.setText(oldWorkDate.format(DateUtil.DATES));
                        startTimePickerEditText.setText(workStart.toString());
                        workEndTimePickerEditText.setText(workEnd.toString());

                        this.fragmentEditShiftBinding.deleteShiftButton.setOnClickListener((view) -> {
                            if (!deleteShift(oldWorkDate)){
                                AlertUtil.send(viewById, "Vagten kunne ikke blive slettet, prøv igen..", BaseTransientBottomBar.LENGTH_LONG);
                            }else {
                                AlertUtil.send(viewById, String.format("Slettede vagten d. %s", oldWorkDate.toString()), BaseTransientBottomBar.LENGTH_LONG);
                                this.navController.navigate(R.id.navigateFromEditShiftToAllSalariesFragment);
                            }
                        });

                        this.fragmentEditShiftBinding.changeShiftButton.setOnClickListener((view) -> {
                            final LocalDate newWorkDate;
                            final LocalTime newWorkStart;
                            final LocalTime newWorkEnd;
                            try {
                                newWorkDate = LocalDate.parse(datePickerActionsEditText.getText().toString(), DateUtil.DATES);
                                newWorkStart = LocalTime.parse(startTimePickerEditText.getText().toString(), DateUtil.TIME);
                                newWorkEnd = LocalTime.parse(workEndTimePickerEditText.getText().toString(), DateUtil.TIME);

                                if (updateShift(oldWorkDate, newWorkDate, newWorkStart, newWorkEnd))
                                    AlertUtil.send(viewById, "Vagten er blevet opdateret successfuldt!", BaseTransientBottomBar.LENGTH_LONG);
                                else
                                    AlertUtil.send(viewById, "Vagten kunne ikke opdateres prøv igen!", BaseTransientBottomBar.LENGTH_LONG);

                                this.navController.navigate(R.id.navigateFromEditShiftToAllSalariesFragment);
                            } catch (DateTimeParseException e) {
                                AlertUtil.send(viewById, String.format("Der var en fejl %s", e.getMessage()), BaseTransientBottomBar.LENGTH_LONG);
                                this.navController.navigate(R.id.navigateFromEditShiftToAllSalariesFragment);
                            }
                        });
                    }else {
                        AlertUtil.send(viewById, "Kunne ikke finde data for vagten!", BaseTransientBottomBar.LENGTH_SHORT);
                        navController.popBackStack();
                    }
                }catch(ClassCastException classCastException){
                    classCastException.printStackTrace();
                    AlertUtil.send(getActivity().findViewById(android.R.id.content), "Kunne ikke fortolke dataen!", BaseTransientBottomBar.LENGTH_LONG);
                    navController.popBackStack();
                }
            }
        } else navController.popBackStack();
        return fragmentEditShiftBinding.getRoot();
    }


    protected boolean updateShift(LocalDate oldDate, LocalDate newWorkDate, LocalTime newWorkStart, LocalTime workStart){
        CompletableFuture<Boolean> updatedSuccessFully = CompletableFuture.supplyAsync(() -> {
            AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
            if (!db.shiftDao().doesShiftExist(oldDate)) return false;
            return db.shiftDao().updateShift(oldDate, newWorkDate, newWorkStart, workStart) > 0;
        });
        try {
            return updatedSuccessFully.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean deleteShift(LocalDate workDate){
        CompletableFuture<Boolean> deletedSuccessFully = CompletableFuture.supplyAsync(() -> {
            AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
            if (!appDatabase.shiftDao().doesShiftExist(workDate)) return false;

            final int amountOfRowsAffected = appDatabase.shiftDao().deleteShift(workDate);
            return amountOfRowsAffected > 0;
        });
        try {
            return deletedSuccessFully.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentEditShiftBinding = null;
    }
}