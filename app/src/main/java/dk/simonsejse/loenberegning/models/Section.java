package dk.simonsejse.loenberegning.models;

import java.io.Serializable;
import java.util.List;

import dk.simonsejse.loenberegning.database.Shift;

public class Section implements Serializable {

    private String sectionName;
    private List<Shift> shiftList;

    public Section(final String sectionName, final List<Shift> shiftList){
        this.sectionName = sectionName;
        this.shiftList = shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }
    public List<Shift> getShiftList() {
        return shiftList;
    }
    public String getSectionName() {
        return sectionName;
    }
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
