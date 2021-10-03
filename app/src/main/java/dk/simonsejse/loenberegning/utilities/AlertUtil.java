package dk.simonsejse.loenberegning.utilities;

import android.app.AlertDialog;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import dk.simonsejse.loenberegning.R;

public class AlertUtil {
    public static void send(View view, String title, String message, int img){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(img);
        alertDialog.setPositiveButton(R.string.ok_alert, null);
        alertDialog.create().show();
    }
}
