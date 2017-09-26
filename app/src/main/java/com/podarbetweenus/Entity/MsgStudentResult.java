package com.podarbetweenus.Entity;

/**
 * Created by Gayatri on 4/21/2016.
 */
public class MsgStudentResult {
    public String Div= "";
    public String Roll_No = "";
    public String Section = "";
    public String Std = "";
    public String Student_Name = "";
    public String stu_ID = "";
    public String usl_Id = "";
    private boolean checked = false ;

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }
}
