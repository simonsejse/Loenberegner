package dk.simonsejse.loenberegning.utilities;

public enum BundleStoringKeyEnum {
    SHIFT_KEY("shift_key"),
    WORK_START("workStart"),
    WORK_END("workEnd"),
    ALL_SECTIONS_LIST("sectionsList");

    public String key;

    BundleStoringKeyEnum(String key){
        this.key = key;
    }
}
