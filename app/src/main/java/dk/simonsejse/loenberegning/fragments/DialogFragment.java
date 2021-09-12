package dk.simonsejse.loenberegning.fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalTime;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.databinding.FullDialogFragmentForAddingAdditionBinding;
import dk.simonsejse.loenberegning.models.IResponseDataFromDialogFragment;
import dk.simonsejse.loenberegning.models.ResponseDataFromDialogFragmentModel;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    private FullDialogFragmentForAddingAdditionBinding binding;

    private final IResponseDataFromDialogFragment iResponseDataFromDialogFragment;

    public DialogFragment(IResponseDataFromDialogFragment iResponseDataFromDialogFragment, Shift shift){
        this.iResponseDataFromDialogFragment = iResponseDataFromDialogFragment;
    }

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
        this.binding.addExtraAdditionBtn.setOnClickListener(this::addNewExtraAdditionToShift);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNewExtraAdditionToShift(View view) {
        final LocalTime start, end;
        //Get values instead of just doing this shit
        start = LocalTime.now();
        end = LocalTime.now();
        final Integer amount = 120;

        iResponseDataFromDialogFragment.callback(new ResponseDataFromDialogFragmentModel(
            start, end, amount
        ));
    }
}
