package dk.simonsejse.loenberegning.models;

import dk.simonsejse.loenberegning.database.Shift;

public interface IOnEditShiftListener {
    void onShiftButtonClick(Shift shift);
}
