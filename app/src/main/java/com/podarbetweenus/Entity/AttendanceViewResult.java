package com.podarbetweenus.Entity;

/**
 * Created by Gayatri on 3/16/2017.
 */

public class AttendanceViewResult {
    public String Roll_No = "";
    public String StudentName = "";
    public String atnFlag = "";
    public String atnReason = "";
    public String cls_id = "";
    public String msd_id = "";
    public boolean absentChecked = false ;
    public boolean smsChecked = false;

    // getter for absent checked or not
    public boolean getAbsentStatus() {

        if(atnFlag.equalsIgnoreCase("1")){
            absentChecked = true;
        }

        return absentChecked;
    }

    // setter for absent checkbox
    public void setAbsentStatus(boolean checked) {
        this.absentChecked = checked;
        if (absentChecked==true){
            atnFlag = "1";
        }
        else{
            atnFlag = "";
        }
    }

    // getter for absent reason
    public String getAbsentReason() {
        return atnReason ;
    }
    // setter for absent reason
    public void setAbsentReason(String reason) {
        this.atnReason = reason;
    }

    // getter for sms checked or not
    public boolean getSmsStatus() {

        return smsChecked;
    }

    // setter for absent checkbox
    public void setSmsStatus(boolean checked) {
        this.smsChecked = checked;

    }
}
