package dk.simonsejse.loenberegning.models;

import com.google.android.material.timepicker.MaterialTimePicker;

@FunctionalInterface
public interface ITimePickerPositiveClickListenerCallback {
    void callback(MaterialTimePicker materialTimePickerCallback);
}