package dk.simonsejse.loenberegning.models;

public enum EnumDateVarchar {
    WORK_START("workStart"),
    WORK_END("workEnd"),
    WORK("work");

    private final String text;

    EnumDateVarchar(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}