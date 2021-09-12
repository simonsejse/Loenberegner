package dk.simonsejse.loenberegning.models;

import java.time.LocalTime;

public class ResponseDataFromDialogFragmentModel {
    public final LocalTime localStart, localEnd;
    public final Integer amount;

    public ResponseDataFromDialogFragmentModel(LocalTime localStart, LocalTime localEnd, Integer amount) {
        this.localStart = localStart;
        this.localEnd = localEnd;
        this.amount = amount;
    }

}
