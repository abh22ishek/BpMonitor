package chart;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.model.BPMeasurementModel;
import com.util.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import customviews.CustomBPDayChart;
import customviews.PulseDayChart;

public class DaysChartFragment extends Fragment{


    private final String TAG=DaysChartFragment.class.getSimpleName();
    String selectedDate;
    List<BPMeasurementModel> mRecordDetail;
    GraphView custom_pulse_chart;
    CustomBPDayChart custom_day_chart;
    PulseDayChart pulseDayChart;
    TextView date;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.days_chart_frag,container,false);

        date= (TextView) view.findViewById(R.id.date);

        custom_day_chart= (CustomBPDayChart) view.findViewById(R.id.custom_day_chart);
        pulseDayChart= (PulseDayChart) view.findViewById(R.id.pulse_day_chart);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments().getSerializable(Constants.CHART)!=null){

           mRecordDetail= (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
            Logger.log(Level.WARNING,TAG,"Get serializable list="+mRecordDetail.size());

        }


        if(getArguments().getString(Constants.DATE)!=null){
            selectedDate=getArguments().getString(Constants.DATE);
        }


        date.setText(selectedDate.substring(0,10));



        custom_day_chart.setHorizontalLabels(sameDateRecords(mRecordDetail,selectedDate));
        pulseDayChart.setHorizontalLabel(sameDateRecords(mRecordDetail,selectedDate));



    }



    private List<BPMeasurementModel> sameDateRecords(List<BPMeasurementModel> mRecordDetailList,String selectedDate)
    {
        final List<BPMeasurementModel> listToReturn = new ArrayList<>();


        for(BPMeasurementModel b:mRecordDetailList){
            if(selectedDate.equals(b.getMeasurementTime().substring(0,10))){
                listToReturn.add(b);
            }
        }

        Logger.log(Level.WARNING,TAG,"****Record with same date****"+listToReturn.size());
        return listToReturn;
    }










    private void plotPulsePerMinGraph(List<BPMeasurementModel> mRecordDetail,String selectedDate){

        DataPoint[] dataPoints = new DataPoint[mRecordDetail.size()];

        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        Date startDate;

        try {
            startDate = df.parse(selectedDate);
            String newDateString = df.format(startDate);
            Logger.log(Level.WARNING,TAG,"date format="+newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        for (int i =0; i <dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i,
                    mRecordDetail.get(i).getPulsePerMin());

        }

        PointsGraphSeries<DataPoint> mSeries = new PointsGraphSeries<>(dataPoints);
        Viewport viewport = custom_pulse_chart.getViewport();
        // viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        //  spo2_graph.setTitle("PR");


        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);
        custom_pulse_chart.getGridLabelRenderer().setTextSize(23f);
        custom_pulse_chart.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        custom_pulse_chart.getGridLabelRenderer().setVerticalLabelsVisible(true);
        // graphview_spo2.getGridLabelRenderer().set
        custom_pulse_chart.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        custom_pulse_chart.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        custom_pulse_chart.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        custom_pulse_chart.getGridLabelRenderer().setHighlightZeroLines(false);
        custom_pulse_chart.setBackgroundColor(Color.TRANSPARENT);
        custom_pulse_chart.getGridLabelRenderer().setGridColor(Color.CYAN);
        custom_pulse_chart.getGridLabelRenderer().reloadStyles();


        mSeries.setSize(10f);
        custom_pulse_chart.addSeries(mSeries);
        //   spo2_graph.getGridLabelRenderer().setNumHorizontalLabels(12);
        custom_pulse_chart.getViewport().setXAxisBoundsManual(true);
        custom_pulse_chart.getViewport().setMinX(1);
        custom_pulse_chart.getViewport().setMaxX(5);


        custom_pulse_chart.getGridLabelRenderer().setNumVerticalLabels(7);


        custom_pulse_chart.getViewport().setYAxisBoundsManual(true);
        custom_pulse_chart.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        custom_pulse_chart.getViewport().setMaxY(140);
        custom_pulse_chart.getGridLabelRenderer().setLabelsSpace(4);

        //  spo2_graph.getViewport().setScalable(true);
        custom_pulse_chart.getViewport().setScrollable(true);


        custom_pulse_chart.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }

}
