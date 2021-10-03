package dk.simonsejse.loenberegning.directions;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;

import dk.simonsejse.loenberegning.R;
import dk.simonsejse.loenberegning.database.Shift;
import dk.simonsejse.loenberegning.utilities.BundleStoringKeyEnum;

public class NavigateFromExtraToAdd implements NavDirections {
    protected Shift shift;

    public NavigateFromExtraToAdd(final Shift shift){
        this.shift = shift;
    }

    @Override
    public int getActionId() {
        return R.id.navigateFromExtraAdditionToAddShiftFragment;
    }

    @NonNull
    @Override
    public Bundle getArguments() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BundleStoringKeyEnum.SHIFT_KEY.key, this.shift);
        return bundle;
    }
}
