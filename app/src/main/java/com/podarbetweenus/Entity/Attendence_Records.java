package com.podarbetweenus.Entity;

/**
 * Created by Gayatri on 3/2/2017.
 */

public class Attendence_Records {

    String roll_no = "";
    String name = "";
    String absent = "";
    String reason = "";
    String sms = "";

    public Attendence_Records(String roll_no, String name, String absent, String reason, String sms) {
        this.roll_no = roll_no;
        this.name = name;
        this.absent = absent;
        this.reason = reason;
        this.sms = sms;
    }




    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
