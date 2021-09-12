package dk.simonsejse.loenberegning.utilities;

public enum PreferenceUtil {
    hourlyRate("hourlyrate"),
    skatInProcent("skatInProcent"),
    fradrag("fradrag");


    private String text;

    PreferenceUtil(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
