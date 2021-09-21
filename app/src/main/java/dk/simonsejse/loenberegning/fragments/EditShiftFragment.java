package dk.simonsejse.loenberegning.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.room.Room;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import dk.simonsejse.loenberegning.database.AppDatabase;
import dk.simonsejse.loenberegning.databinding.FragmentEditShiftBinding;


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
        /*

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
                    final EditText startTimePickerEditText = this.fragmentEditShiftBinding.workStartTimePicker.getEditText();
                    final EditText workEndTimePickerEditText = this.fragmentEditShiftBinding.workEndTimePicker.getEditText();

                    if (startTimePickerEditText != null && workEndTimePickerEditText != null){
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
                            final LocalDateTime newWorkStart;
                            final LocalDateTime newWorkEnd;
                            try {
                                newWorkDate = LocalDate.parse(datePickerActionsEditText.getText().toString(), DateUtil.DATES);
                                newWorkStart = LocalDateTime.parse(startTimePickerEditText.getText().toString());
                                newWorkEnd = LocalDateTime.parse(workEndTimePickerEditText.getText().toString());

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

         */
        return fragmentEditShiftBinding.getRoot();
    }


    protected boolean updateShift(LocalDateTime oldStart, LocalDateTime newWorkStart, LocalDateTime workStart){
        CompletableFuture<Boolean> updatedSuccessFully = CompletableFuture.supplyAsync(() -> {
            AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
            if (!db.shiftDao().doesShiftExist(oldStart)) return false;
            //TODO: find id instead of just inputting 2
            return db.shiftDao().updateShift( 2, newWorkStart, workStart) > 0;
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

    protected boolean deleteShift(LocalDateTime dateTime, long id){
        CompletableFuture<Boolean> deletedSuccessFully = CompletableFuture.supplyAsync(() -> {
            AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "shift").build();
            if (!appDatabase.shiftDao().doesShiftExist(dateTime)) return false;

            final int amountOfRowsAffected = appDatabase.shiftDao().deleteShift(id);
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