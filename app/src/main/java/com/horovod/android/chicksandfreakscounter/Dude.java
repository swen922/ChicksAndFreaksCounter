package com.horovod.android.chicksandfreakscounter;

import java.util.Date;

public class Dude {

    private String dudeType;
    private String dateString;
    private String description;
    private int spinnerSelectedPosition;

    public Dude(String dudeType, String dateString, String description, int selected) {
        this.dudeType = dudeType;
        this.dateString = dateString;
        this.description = description;
        this.spinnerSelectedPosition = selected;
    }

    // Constructor with Date formatting to two Strings
    public Dude(String dudeType, Date date, String description, int selected) {
        this.dudeType = dudeType;
        this.dateString = Util.formatDate(date);
        this.description = description;
        this.spinnerSelectedPosition = selected;
    }

    public String getDudeType() {
        return dudeType;
    }

    public String getDateString() {
        return dateString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpinnerSelectedPosition() {
        return spinnerSelectedPosition;
    }

    public void setSpinnerSelectedPosition(int spinnerSelectedPosition) {
        this.spinnerSelectedPosition = spinnerSelectedPosition;
    }

}
