package dk.simonsejse.loenberegning.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.fragments.DialogFragment;

public class ResponseDataFromDialogFragmentModel {
    public final Shift shift;
    public ResponseDataFromDialogFragmentModel(Shift shift) {
        this.shift = shift;
    }

}
