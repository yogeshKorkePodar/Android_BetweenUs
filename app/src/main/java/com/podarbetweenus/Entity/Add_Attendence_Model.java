package com.podarbetweenus.Entity;

/**
 * Created by Gayatri on 3/6/2017.
 */

public class Add_Attendence_Model {

    public String Roll_No = "";
    public String Student_Name = "";
    public String stu_ID = "";
    private boolean absentChecked = false ;
    private boolean smsChecked = false;
    public  String absent_reason = "";


    // getter for absent checked or not
    public boolean isAbsentChecked() {

        return absentChecked;
    }
    // setter for absent checkbox
    public void setAbsentChecked(boolean checked) {
        this.absentChecked = checked;
    }

    // getter for sms checked or not
    public boolean isSmsChecked() {
        return smsChecked ;
    }
    // setter for sms checkbox
    public void setSmsChecked(boolean checked) {
        this.smsChecked = checked;
    }
}
