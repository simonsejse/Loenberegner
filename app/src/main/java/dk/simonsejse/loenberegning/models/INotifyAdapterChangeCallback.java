package dk.simonsejse.loenberegning.models;

import java.util.List;

public interface INotifyAdapterChangeCallback {
    void performSectionUpdate(List<Section> sections);
}
