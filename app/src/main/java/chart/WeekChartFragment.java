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
import customviews.CustomBPWeekChart;
import customviews.PulseWeekChart;

public class WeekChartFragment extends Fragment{


    PulseWeekChart pulseWeekChart;
    private String selectedDate;
    private List<String> dates_list;
    private final String TAG = WeekChartFragment.class.getSimpleName();
    private List<BPMeasurementModel> sameMonthOfYearWithDuplicates;
    int noOfDays=7;
    List<String> week1List,plotPoint1List,systolicPoint1List,diabolicPoint1List;
    List<String> week2List,plotPoint2List,systolicPoint2List,diabolicPoint2List;
    List<String> week3List,plotPoint3List,systolicPoint3List,diabolicPoint3List;
    List<String> week4List,plotPoint4List,systolicPoint4List,diabolicPoint4List;
    List<String> week5List,plotPoint5List,systolicPoint5List,diabolicPoint5List;



    CustomBPWeekChart bpChartWeek;
    TextView weekDate;
    private List<BPMeasurementModel> mRecordDetail;
    private String monthOFYear;

    private Map<String, Integer> pulseValue;



    private Map<String, Integer> SystolicValue;
    private Map<String, Integer> DiabolicValue;
    private List<String> systolicList;
    private List<String> diabolicList;

    List<String> pulsePlotPoints;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.week_chart_frag,container,false);
        pulseWeekChart= (PulseWeekChart) view.findViewById(R.id.pulseWeekChart);
        bpChartWeek= (CustomBPWeekChart) view.findViewById(R.id.bpChartWeek);
        weekDate= (TextView) view.findViewById(R.id.weekDate);
        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        week1List=new ArrayList<>();
        week2List=new ArrayList<>();
        week3List=new ArrayList<>();
        week4List=new ArrayList<>();
        week5List=new ArrayList<>();



        sameMonthOfYearWithDuplicates=new ArrayList<>();
        pulsePlotPoints=new ArrayList<>();

        systolicList=new ArrayList<>();
        diabolicList=new ArrayList<>();


        if (getArguments().getString(Constants.DATE) != null) {
            selectedDate = getArguments().getString(Constants.DATE);
            dates_list = getDaysBetweenDates(get_starting_date_month(selectedDate),
                    get_last_date_month(selectedDate));
            monthOFYear = selectedDate.substring(3, 5);

        }
        if (getArguments().getSerializable(Constants.CHART) != null) {

            if (mRecordDetail == null) {
                mRecordDetail = (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
                Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
            }


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


        for (int i = 0; i < dates_list.size(); i++) {

            pulseValue.put(dates_list.get(i),0);
            SystolicValue.put(dates_list.get(i),0);
            DiabolicValue.put(dates_list.get(i),0);
        }


        // Second pass for systolic

        for (Map.Entry<String, List<Integer>> entry : firstPassSystolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            SystolicValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }

        // Second pass for diabolic

        for (Map.Entry<String, List<Integer>> entry : firstPassDiabolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            DiabolicValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }



        // Second pass for pulse

        for (Map.Entry<String, List<Integer>> entry : firstPass.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            pulseValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }



        // for systolic
        for (String key : SystolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + SystolicValue.get(key));
            systolicList.add("" +  SystolicValue.get(key));
        }



        // for diabolic

        for (String key : DiabolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + DiabolicValue.get(key));
            diabolicList.add("" +  DiabolicValue.get(key));
        }


            //for pulse

        for (String key : pulseValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + pulseValue.get(key));
            pulsePlotPoints.add("" +  pulseValue.get(key));
        }



        storeWeekList();


        weekDate.setText(firstToLastDayOfWeek(checkSelectedDateWeeks(selectedDate)));
        pulseWeekChart.setHorizontalLabel(checkSelectedDateWeeks(selectedDate));
        pulseWeekChart.setPlotPoints(plotPulseValue(selectedDate));

        bpChartWeek.setHorizontalLabelsBP(checkSelectedDateWeeks(selectedDate));
        bpChartWeek.setSystolicPlotPoints(plotSystolicValue(selectedDate));
        bpChartWeek.setDiabolicPlotPoints(plotDiabolicValue(selectedDate));

    }

    private double calcAverage(List<Integer> values) {
        double result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result / values.size();
    }




    private List<String> checkSelectedDateWeeks(String selectedDate)
    {


        if(week1List.contains(selectedDate)){

           plotPoint1List=pulsePlotPoints.subList(0,7);
            return week1List;
        }


        else if(week2List.contains(selectedDate)){

            plotPoint2List=pulsePlotPoints.subList(7,14);
            return  week2List;
        }



        else if(week3List.contains(selectedDate)){
            plotPoint3List=pulsePlotPoints.subList(14,21);
            return week3List;
        }


        else if(week4List.contains(selectedDate)){
            plotPoint4List=pulsePlotPoints.subList(21,28);
            return week4List;
        }


        else{
            plotPoint4List=pulsePlotPoints.subList(28,dates_list.size());
            return week5List;
        }


    }

    private List<String> plotPulseValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            plotPoint1List=pulsePlotPoints.subList(0,7);
            return plotPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            plotPoint2List=pulsePlotPoints.subList(7,14);
            return  plotPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            plotPoint3List=pulsePlotPoints.subList(14,21);
            return plotPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            plotPoint4List=pulsePlotPoints.subList(21,28);
            return plotPoint4List;
        }


        else{
            plotPoint5List=pulsePlotPoints.subList(28,dates_list.size());
            return plotPoint5List;
        }


    }




    private List<String> plotSystolicValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            systolicPoint1List=systolicList.subList(0,7);
            return systolicPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            systolicPoint2List=systolicList.subList(7,14);
            return  systolicPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            systolicPoint3List=systolicList.subList(14,21);
            return systolicPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            systolicPoint4List=systolicList.subList(21,28);
            return systolicPoint4List;
        }


        else{
            systolicPoint5List=systolicList.subList(28,dates_list.size());
            return systolicPoint5List;
        }


    }

    private List<String> plotDiabolicValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            diabolicPoint1List=diabolicList.subList(0,7);
            return diabolicPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            diabolicPoint2List=diabolicList.subList(7,14);
            return  diabolicPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            diabolicPoint3List=diabolicList.subList(14,21);
            return diabolicPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            diabolicPoint4List=diabolicList.subList(21,28);
            return diabolicPoint4List;
        }


        else{
            diabolicPoint5List=diabolicList.subList(28,dates_list.size());
            return diabolicPoint5List;
        }


    }

    private String firstToLastDayOfWeek(List<String> weekList){
        return  weekList.get(0)+" to "+ weekList.get(weekList.size()-1);
    }



    private void storeWeekList()
    {
        int k=noOfDays;
        int pX=0;
        int weekCounter=0;

        for(int i=pX; i<dates_list.size() ;i++)
        {
            Logger.log(Level.WARNING,TAG,"//Get Week dates//"+dates_list.get(i));
            if(weekCounter==0){
                week1List.add(dates_list.get(i));

            }

            else if(weekCounter==1)
            {
                week2List.add(dates_list.get(i));

            }

            else if(weekCounter==2){
                week3List.add(dates_list.get(i));

            }
            else if(weekCounter==3){
                week4List.add(dates_list.get(i));

            }

            else if(weekCounter==4){
                week5List.add(dates_list.get(i));

            }


            if(i==k-1){
                Logger.log(Level.WARNING,TAG,"// Week interval //");
                pX=pX+7;
                k=k+7;
                weekCounter++;
            }

        }
    }







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

    public List<String> getDaysBetweenDates(String  start_date, String end_date)
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