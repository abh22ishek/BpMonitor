package report;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import customviews.CustomBPChart;

public class ReportFragment extends Fragment{

    TextView sysPressure,diaPressure,pulseRateText,comment;
    private CustomBPChart customBPChart;

    HomeActivityListner homeActivityListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner= (HomeActivityListner) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.report_frag,container,false);
        sysPressure= (TextView) view.findViewById(R.id.sysPressure);
        diaPressure= (TextView) view.findViewById(R.id.diaPressure);
        pulseRateText= (TextView) view.findViewById(R.id.pulseRate);
        comment= (TextView) view.findViewById(R.id.comment);
        customBPChart= (CustomBPChart) view.findViewById(R.id.customBPView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(ReportFragment.class.getName());

        if(getArguments().getString(Constants.systolic)!=null){
            sysPressure.setText(getArguments().getString(Constants.systolic));
        }

        if(getArguments().getString(Constants.diabolic)!=null){
            diaPressure.setText(getArguments().getString(Constants.diabolic));
        }


        if(getArguments().getString(Constants.pulse)!=null){
            pulseRateText.setText(getArguments().getString(Constants.pulse));
        }

        if(getArguments().getString(Constants.comment)!=null){
            comment.setText("Comment :"+getArguments().getString(Constants.comment));
        }


        float sysF = Float.parseFloat(getArguments().getString(Constants.systolic));
        final int pointX=(int) sysF;

        float diaF = Float.parseFloat(getArguments().getString(Constants.diabolic));
        final int pointY=(int) diaF;


        customBPChart.set_XY_points(pointY,pointX);
        customBPChart.invalidate();
    }





}
