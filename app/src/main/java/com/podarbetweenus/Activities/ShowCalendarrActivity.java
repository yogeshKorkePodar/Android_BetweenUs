package com.podarbetweenus.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.podarbetweenus.BetweenUsConstant.Constant;
import com.podarbetweenus.Entity.LoginDetails;
import com.podarbetweenus.R;
import com.podarbetweenus.Services.DataFetchService;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Gayatri on 31/01/2017.
 */
public class ShowCalendarrActivity extends LinearLayout

{
    public static String ShowCalenderData_Method_name = "AttenCalenderData";
    public static Context mycontext;
    DataFetchService dft;
    LoginDetails login_details;
    ProgressDialog progressDialog;
    String clt_id="", brd_name="", class_id = "", Acy_year = "";
    String strYear="";
    int year_index;
    public HashMap yearData = new HashMap();
    public HashMap monthData = new HashMap();
    ArrayList<String> yearList = new ArrayList<>();
    ArrayList<String> monthList = new ArrayList<>();
    ArrayList<String> AttendenceEnteredDates = new ArrayList<>();
    ArrayList<String> HolidayDates = new ArrayList<>();

    private static int month_number;
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private TextView txtDate;
    private GridView grid;
    private EditText ed_select_month,ed_select_year;

    // seasons' rainbow
    int[] rainbow = new int[] {
            R.color.summer,
            R.color.fall,
            R.color.winter,
            R.color.spring
    };

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public ShowCalendarrActivity(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
        mycontext = context;
       // Log.d("<< Called", "Constructor 1");


    }

   /* public ShowCalendarrActivity(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        Log.d("<< Called", "Constructor 2");
    }*/
    // Preferences are set inside TeacherAttendenceActivity
    public void getSharedPrefData(Context context){
        SharedPreferences pref = context.getSharedPreferences("AttendencePref", MODE_PRIVATE);

        clt_id = pref.getString("client_id",null);
        class_id = pref.getString("class_id",null);
        brd_name = pref.getString("board_name",null);
        Acy_year = pref.getString("academic_year",null);

    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        mycontext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);

        loadDateFormat(attrs);

        assignUiElements();

        dft = new DataFetchService(context);
        login_details = new LoginDetails();
        progressDialog = Constant.getProgressDialog(context);

        assignClickHandlers();

        addListenerOnMonthSpinnerItemSelection();

        addListenerOnYearSpinnerItemSelection();

        getSharedPrefData(context);

        CallWSToShowCalenderData(clt_id, class_id, Acy_year,brd_name);

    }


    private void CallWSToShowCalenderData(String clt_id, String class_id, String Acy_year, String brd_name) {

        if(dft.isInternetOn()==true) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
        else{
            progressDialog.dismiss();
        }

        dft.getCalenderData(clt_id, class_id, Acy_year, brd_name, ShowCalenderData_Method_name, Request.Method.POST,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            login_details = (LoginDetails) dft.GetResponseObject(response, LoginDetails.class);
                            Log.d("<<ShowCalender RSP", response.toString());
                            if (login_details.Status.equalsIgnoreCase("1")) {
                               //success
                                //Log.d("<<ShowCalenderActivity", response.toString());
                                addDataToSpinner();

                            } else if (login_details.Status.equalsIgnoreCase("0")) {
                            //    Log.d("<<ShowCalenderActivity", "Request Failed");
                               //failed

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Show error or whatever...
                       // Log.d("TeacherEntrMesgActivity", "ERROR.._---" + error.getCause());
                    }
                });
    }



    private void loadDateFormat(AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckedTextView);

        try
        {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CustomCheckedTextView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally
        {
            ta.recycle();
        }
    }
    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
        monthSpinner=  (Spinner) findViewById(R.id.spinner);
        yearSpinner= (Spinner) findViewById(R.id.spinner3) ;
        header = (LinearLayout)findViewById(R.id.calendar_header);

        grid = (GridView)findViewById(R.id.calendar_grid);
    }
    // Getting years and adding them to year spinner
    public void addDataToSpinner(){
        Log.d("<<Call","addDataToSpinner");
        //adding all years to year list
        for(int i=0; i<login_details.YearMonthResult.size(); i++){
            strYear = login_details.YearMonthResult.get(i).strYear;
            yearList.add(strYear);
            yearData.put(strYear,i);   //{2016,0}, {2017,1}
            Log.d("<<yearData", strYear + ":" + String.valueOf(i));
        }
        // reversing yearlist to get current year as default in year spineer
        Collections.reverse(yearList);
        // populate year Year spinner with yearList data
        addYearsToSpinner();

    }
    //Adding items to months spinner
    public void addMonthsToSpinner(){
        int month = currentDate.get(Calendar.MONTH);
        String default_month = getMonth(month);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, monthList );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(dataAdapter);
        updateCalendar();

    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public void addYearsToSpinner(){

    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, yearList);
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    yearSpinner.setAdapter(dataAdapter);
        addMonthsToSpinner();
    }

    public void addListenerOnMonthSpinnerItemSelection(){
        Log.d("<<Called","addListenerOnMonthSpinnerItemSelection()");
        monthSpinner = (Spinner) findViewById(R.id.spinner);
        monthSpinner.setOnItemSelectedListener(new MonthOnItemSelectedListener());
    }

    public void addListenerOnYearSpinnerItemSelection(){
        Log.d("<<Called","addListenerOnYearSpinnerItemSelection()");
        yearSpinner = (Spinner) findViewById(R.id.spinner3);
        yearSpinner.setOnItemSelectedListener(new YearOnItemSelectedListener());
    }
    public class MonthOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            AttendenceEnteredDates.clear();
            HolidayDates.clear();
            Log.d("<<Called","MonthOnItemSelectedListener ");

            String month_selected = parent.getItemAtPosition(pos).toString();
            //Log.d("<<Selected month", month_selected);

            // Getting index to traverse holidays, attendence entered dates
            int month_index = Integer.parseInt(monthData.get(month_selected).toString());
            //Log.d("<<month index", String.valueOf(month_index));

            //Log.d("<<year index", String.valueOf(year_index));
            //Log.d("<<year at index", String.valueOf(yearData.get(year_index)));

            int list_size1 = login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendEnteredResult.size();
            Log.d("<<Attendence list size", String.valueOf(list_size1));

            int list_size2 = login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendHolidayResult.size();
            Log.d("<<Holiday list size", String.valueOf(list_size2));

            // Adding attendence entered date to list to highlight them in calender
            for (int i = 0; i< login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendEnteredResult.size();i++){
                String attendenceEnteredDate = login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendEnteredResult.get(i).Entereddate;
                //Log.d("<<Att Date retrieved", attendenceEnteredDate);

                if(String.valueOf(attendenceEnteredDate.charAt(0)).equalsIgnoreCase("0")){
                    attendenceEnteredDate = String.valueOf(attendenceEnteredDate.charAt(1));
                    //Log.d("<<Att Date Modified", attendenceEnteredDate);
                }
                AttendenceEnteredDates.add(attendenceEnteredDate);
            }

            // Adding  holiday date to list to highlight them in calender
            for (int i = 0; i< login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendHolidayResult.size();i++){
                String holidayDate = login_details.YearMonthResult.get(year_index).AttendMonthResult.get(month_index).AttendHolidayResult.get(i).Holidaydate;
                if(String.valueOf(holidayDate.charAt(0)).equalsIgnoreCase("0")){
                    holidayDate = String.valueOf(holidayDate.charAt(1));
                //    Log.d("<<Holiday Date Modified", holidayDate);
                }
                HolidayDates.add(holidayDate);
            }



            Date date = null;
            try {
                date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month_selected);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
             month_number = cal.get(Calendar.MONTH);
            Log.d("<<month_number ",String.valueOf(month_number));
            currentDate.set(Calendar.MONTH,month_number);

            updateCalendar();

            /*Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    public class YearOnItemSelectedListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            monthList.clear();
           // Log.d("<<Called","YearOnItemSelectedListener ");
            String year_selected = parent.getItemAtPosition(position).toString();
            year_index = Integer.parseInt(yearData.get(year_selected).toString());
             for (int i =0;i<login_details.YearMonthResult.get(year_index).AttendMonthResult.size();i++){
                String month = login_details.YearMonthResult.get(year_index).AttendMonthResult.get(i).StrMonth;
              //   Log.d("<<MonthListElement",month);
                 monthList.add(month);
                 monthData.put(month, i);
             }

            int year_selected2 = Integer.parseInt(parent.getItemAtPosition(position).toString());
            currentDate.set(Calendar.YEAR,year_selected2);
            addMonthsToSpinner();
            updateCalendar();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void assignClickHandlers()
    {

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id)
            {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
                return true;
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date dateClicked = (Date)parent.getItemAtPosition(position);
               // Log.d("<<calender date", String.valueOf(dateClicked.getDate()));
              //  Log.d("<<calender month", String.valueOf(dateClicked.getMonth()));
               // Log.d("<<calender year", String.valueOf(dateClicked.getYear()));

                Date today = new Date();
                int day = today.getDate();
                int month = today.getMonth();
                int year = today.getYear();

                SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
                String dayName = sdf_.format(dateClicked);

                //Log.d("<<Date Today",String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
                //Log.d("<<Date OnClicked",String.valueOf(dateClicked.getDate())+"/"+String.valueOf(dateClicked.getMonth())+"/"+String.valueOf(dateClicked.getYear()));
                //Log.d("<<Holiday check", String.valueOf(HolidayDates.contains(String.valueOf(dateClicked.getDate()).trim())==true));
                if(dayName.equalsIgnoreCase("SUNDAY")){
                    // its sunday so do nothing
                }
                else if(HolidayDates.contains(String.valueOf(dateClicked.getDate()))==true){
                    Constant.showOkPopup(mycontext, "Not allowed to add attendance for Holiday dates!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
                }
                else if (dateClicked.getDate() > day && dateClicked.getMonth() >= month && dateClicked.getYear() >= year)
                {
                    Constant.showOkPopup(mycontext, "Not allowed to add attendance for future dates!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });
                }
                else if (dateClicked.getMonth() != month_number){
                   // date does not belong to current selected month so Do nothing

                }

                 else {

                    Intent i = new Intent(view.getContext(), TeacherAddAttendenceActivity.class);
                    i.putExtra("date", String.valueOf(dateClicked.getDate()));
                    i.putExtra("month", String.valueOf(dateClicked.getMonth()));
                    i.putExtra("year", String.valueOf(dateClicked.getYear()));

                    if(AttendenceEnteredDates.contains(String.valueOf(dateClicked.getDate()))){
                        i.putExtra("mode","update");
                    }
                    else{
                        i.putExtra("mode", "Insert");

                    }
                    view.getContext().startActivity(i);
                }


            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
      // txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            //Getting each date view item in calender
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            //Log.d("<<current day",String.valueOf(day));
            //Log.d("<<current month",String.valueOf(month));
           // Log.d("<<current year",String.valueOf(year));

            SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
            String dayName = sdf_.format(date);

            //Log.e("<<DATE",String.valueOf(day));
            //Log.e("<<DAY",dayName);


            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);
            TextView tv_cal_grid = (TextView)view.findViewById(R.id.tv_cal_grid);
            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            tv_cal_grid.setTextColor(Color.parseColor("#ff4040"));


            for(int i=0;i<AttendenceEnteredDates.size();i++) {

                String value1 = AttendenceEnteredDates.get(i);
               // Log.d("<< value1",value1);
              //  Log.d("<< value2",String.valueOf(day));
                if (String.valueOf(day).equalsIgnoreCase(value1) /*&& month == today.getMonth()*/) {
                    tv_cal_grid.setTextColor(Color.parseColor("#00ff00"));
                    Log.d("<< Attendence Color set",value1);
                }
            }
            for(int i=0; i<HolidayDates.size();i++){
                String value1 = HolidayDates.get(i);
                Log.d("<<Holiday value",value1);
               // Log.d("<< value2",String.valueOf(day));
                if(String.valueOf(day).equalsIgnoreCase(value1) ){
                    tv_cal_grid.setTextColor(Color.parseColor("#800080"));
                    Log.d("<< Holiday Color set",value1);
                }
            }
            /*for(int i=0;i<=dates.size();i++){

              grid.getChildAt(i).setBackgroundColor(Color.BLUE);
             //   tv_cal_grid.setTextColor(getResources().getColor(R.color.green));

            }*/
           /* Log.d("<<today.getMonth()",String.valueOf(today.getMonth()));
            Log.d("<<month", String.valueOf(month));*/

            if (month != month_number /*|| year != today.getYear()*/)
            {
              /*  Log.d("<< i today.getMonth()",String.valueOf(today.getMonth()));
                Log.d("<< i month", String.valueOf(month));*/

                // if this day is outside current month, grey it out
              //  ((TextView)view).setTextColor(getResources().getColor(R.color.greyed_out));
                tv_cal_grid.setTextColor(getResources().getColor(R.color.greyed_out));
            }
             if (day == today.getDate() && month ==today.getMonth())
            {
                // if it is today, set it to blue/bold
               // ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.today));
                tv_cal_grid.setTypeface(null, Typeface.BOLD);
                tv_cal_grid.setTextColor(getResources().getColor(R.color.today));

            }

            if(dayName.equalsIgnoreCase("SUNDAY") /*&& month ==today.getMonth()*/){
               // ((TextView)view).setTextColor(getResources().getColor(R.color.red));
                tv_cal_grid.setTextColor(Color.parseColor("#ffa500"));
            }

            // set text
            //((TextView)view).setText(String.valueOf(date.getDate()));
           tv_cal_grid.setText(String.valueOf(date.getDate()));
            List<Date> disable = new ArrayList<>();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int mon = cal.get(Calendar.MONTH);
            do {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY)
                    disable.add(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } while (cal.get(Calendar.MONTH) == mon);

            SimpleDateFormat fmt = new SimpleDateFormat("EEE M/d/yyyy");
            Log.e("Size of disable",String.valueOf(disable.size()));
            for(int i = 0;i<=disable.size();i++){

               // ((TextView)view).setTextColor(getResources().getColor(R.color.red));
            }


            for (Date date1 : disable)
               // System.out.println(fmt.format(date1));
                Log.e("DATEEEE",fmt.format(date1));
          //  view.setBackgroundResource(R.color.red);
           // grid.setBackgroundColor(Color.RED);
        //    grid.getChildAt(position).setBackgroundColor(Color.RED);


            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
    }
}
