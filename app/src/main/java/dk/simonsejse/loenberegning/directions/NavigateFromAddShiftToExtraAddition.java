package dk.simonsejse.loenberegning.directions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;

public class NavigateFromAddShiftToExtraAddition implements NavDirections {

    protected final Shift shift;

    public NavigateFromAddShiftToExtraAddition(Shift shift) {
        this.shift = shift;
    }

    @Override
    public int getActionId() {
        return R.id.navigateFromAddShiftToExtraFragment;
    }

    @NonNull
    @Override
    public Bundle getArguments() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(BundleStoringKeyEnum.SHIFT_KEY.key, this.shift);
        return bundle;
    }
}
