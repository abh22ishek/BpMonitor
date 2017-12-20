package chart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.model.BPMeasurementModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import customviews.CustomBPMonthChart;
import customviews.PulseMonthChart;


public class MonthChartFragment extends Fragment {

    private List<BPMeasurementModel> mRecordDetail;
    private final String TAG = MonthChartFragment.class.getSimpleName();
    private String selectedDate;
    private String monthOFYear;
    private List<BPMeasurementModel> sameMonthOfYearWithDuplicates;
    private List<String> dates_list;

    private PulseMonthChart custom_pulse_chart_month;
    private CustomBPMonthChart customBPMonthChart;

    private Map<String, Integer> pulseValue;


    private Map<String, Integer> SystolicValue;
    private Map<String, Integer> DiabolicValue;
    private List<String> systolicList;
    private List<String> diabolicList;

    private List<String> pulsePlotPoints;



    TextView monthDate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_chart_frag, container, false);
        custom_pulse_chart_month = (PulseMonthChart) view.findViewById(R.id.custom_pulse_chart_month);
        customBPMonthChart = (CustomBPMonthChart) view.findViewById(R.id.custom_bp_chart_month);
        monthDate= (TextView) view.findViewById(R.id.monthDate);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // this list will retain the no. of months of specified year which was clicked
        sameMonthOfYearWithDuplicates = new ArrayList<>();
        pulsePlotPoints = new ArrayList<>();

        systolicList=new ArrayList<>();
        diabolicList=new ArrayList<>();

        if (getArguments().getSerializable(Constants.CHART) != null) {

            if (mRecordDetail == null) {
                mRecordDetail = (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
                Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
            }


        }


        if (getArguments().getString(Constants.DATE) != null) {
            selectedDate = getArguments().getString(Constants.DATE);
            monthDate.setText(selectedDate.substring(3,10));
            monthOFYear = selectedDate.substring(3, 5);

            dates_list = getDaysBetweenDates(get_starting_date_month(selectedDate), get_last_date_month(selectedDate));
            custom_pulse_chart_month.setHorizontalLabels(dates_list);
            customBPMonthChart.setHorizontalLabels(dates_list);

        }


        for (BPMeasurementModel b : mRecordDetail) {
            if (b.getMeasurementTime().substring(3, 5).equals(monthOFYear) &&
                    b.getMeasurementTime().substring(6, 10).equals(selectedDate.substring(6, 10))) {
                sameMonthOfYearWithDuplicates.add(b);
            }

        }


        Logger.log(Level.WARNING, TAG, "Get selected list of days in month with duplicates" +
                "=" + sameMonthOfYearWithDuplicates.size());


        pulseValue = new TreeMap<>();
        SystolicValue=new TreeMap<>();
        DiabolicValue=new TreeMap<>();


        // first pass

        Map<String, List<Integer>> firstPass = new TreeMap<>();
        Map<String, List<Integer>> firstPassSystolic = new TreeMap<>();
        Map<String, List<Integer>> firstPassDiabolic = new TreeMap<>();

        for (BPMeasurementModel bp : sameMonthOfYearWithDuplicates) {
            String name = bp.getMeasurementTime().substring(0,10);
            if (firstPass.containsKey(name)) {
                firstPass.get(name).add((int) bp.getPulsePerMin());
                firstPassSystolic.get(name).add((int) bp.getSysPressure());
                firstPassDiabolic.get(name).add((int) bp.getDiabolicPressure());
            } else {
                List<Integer> pulseVal = new ArrayList<>();
                List<Integer> sysVal = new ArrayList<>();
                List<Integer> diabolicVal = new ArrayList<>();


                pulseVal.add((int) bp.getPulsePerMin());
                firstPass.put(name,pulseVal);

                sysVal.add((int) bp.getSysPressure());
                firstPassSystolic.put(name,sysVal);


                diabolicVal.add((int) bp.getDiabolicPressure());
                firstPassDiabolic.put(name,diabolicVal);


            }
        }


        // Second pass
        for (int i = 0; i < dates_list.size(); i++) {

            pulseValue.put(dates_list.get(i),0);
            SystolicValue.put(dates_list.get(i),0);
            DiabolicValue.put(dates_list.get(i),0);
        }


        // for pulse

        for (Map.Entry<String, List<Integer>> entry : firstPass.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            pulseValue.put(entry.getKey(), average);
            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }


       // for systolic

        for (Map.Entry<String, List<Integer>> entry : firstPassSystolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            SystolicValue.put(entry.getKey(), average);
            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }


        // for diabolic

        for (Map.Entry<String, List<Integer>> entry : firstPassDiabolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            DiabolicValue.put(entry.getKey(), average);
            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }



       // pulse
        for (String key : pulseValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + pulseValue.get(key));
            pulsePlotPoints.add("" +  pulseValue.get(key));
        }



        // systolic
        for (String key : SystolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + SystolicValue.get(key));
            systolicList.add("" +  SystolicValue.get(key));
        }


        // diabolic

        for (String key : DiabolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + DiabolicValue.get(key));
            diabolicList.add("" +  DiabolicValue.get(key));
        }


        customBPMonthChart.setSystolicPlotPoints(systolicList);
        customBPMonthChart.setDiabolicPlotPoints(diabolicList);

        custom_pulse_chart_month.setPulsePlotPoints(pulsePlotPoints);


}

    private double calcAverage(List<Integer> values) {
        double result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result / values.size();
    }

    // Get the begining date of month


    private String get_starting_date_month(String date)
    {
        String start_date="";
        Date d=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        Calendar c = Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");

        try {
            convertedDate = dateFormat.parse(date);
            c.setTime(convertedDate);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            d=c.getTime();
            start_date=df.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Logger.log(Level.WARNING,TAG,"STarting date="+start_date);

        return start_date;
    }


    private String get_last_date_month(String date)
    {
        String end_date="";
        Date d=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        Calendar c = Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");

        try {
            convertedDate = dateFormat.parse(date);
            c.setTime(convertedDate);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            d=c.getTime();
            end_date=df.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.log(Level.WARNING,TAG,"Ending  date="+end_date);
        return end_date;
    }


    // get no.of list of dates

    public  List<String> getDaysBetweenDates(String  start_date, String end_date)
    {

        List<String> dates_list_=new ArrayList<String>();
        Date startdate=null;
        Date enddate=null;

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        try {
            startdate=sdf.parse(start_date);
            enddate=sdf.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = Calendar.getInstance();
        start.setTime(startdate);
        Calendar end = Calendar.getInstance();
        end.setTime(enddate);
        end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dfx=new SimpleDateFormat("dd-MM-yyyy");
        while (start.before(end)) {
            String str=df.format(start.getTime());
            dates_list_.add(str);
            Date d=null;
            Logger.log(Level.DEBUG,TAG,"str="+str);
            try {
                d=dfx.parse(str);
                start.add(Calendar.DAY_OF_YEAR, 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Logger.log(Level.WARNING,TAG,"List of dates="+dates_list_);
        return dates_list_;
    }


}
