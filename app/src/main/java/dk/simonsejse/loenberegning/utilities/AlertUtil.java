package dk.simonsejse.loenberegning.utilities;

import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import dk.simonsejse.loenberegning.R;

public class AlertUtil {
    public static void send(View view, String message, int length){
        Snackbar.make(view, message, length)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(view.getResources().getColor(R.color.purple_200))
                .show();
    }
}
