package dk.simonsejse.loenberegning.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResponseDataFromDialogFragmentModel {
    public final LocalDateTime localStart, localEnd;
    public final Integer amount;

    public ResponseDataFromDialogFragmentModel(LocalDateTime localStart, LocalDateTime localEnd, Integer amount) {
        this.localStart = localStart;
        this.localEnd = localEnd;
        this.amount = amount;
    }

}
