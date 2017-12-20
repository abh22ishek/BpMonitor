package fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.model.BPMeasurementModel;

import java.util.List;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import chart.ChartPagerAdapter;
import chart.ZoomOutPageTransformer;
import constants.Constants;


public class BPBaseChartFragment extends Fragment {


    HomeActivityListner homeActivityListner;
    ViewPager viewPager;
    ChartPagerAdapter chartPagerAdapter;
    TabLayout tabLayout;
    List<BPMeasurementModel> mRecordDetail;
     String date;

    private final String TAG=BPBaseChartFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner= (HomeActivityListner) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.base_chart_frag,container,false);
        viewPager= (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout= (TabLayout) view.findViewById(R.id.tabLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(getArguments().getSerializable(Constants.CHART)!=null){

            mRecordDetail= (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
            Logger.log(Level.WARNING,TAG,"GEt serializable list="+mRecordDetail.size());

        }

        if(getArguments().getString(Constants.DATE)!=null){
            date= getArguments().getString(Constants.DATE);
        }

        homeActivityListner.onDataPass(BPBaseChartFragment.class.getName());


        /*if(null!=getArguments().getParcelableArrayList(Constants.MEASURED_BP))
        {
            DaysList=getArguments().getParcelableArrayList(Constants.MEASURED_BP);

        }*/






       /* for(BPMeasurementModelList b:DaysList)
        {
            Logger.log(Level.WARNING,TAG,"Get intent days list="+b.getMeasurementTime()+" "+b.getSysPressure());
        }*/

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        chartPagerAdapter=new ChartPagerAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount(),mRecordDetail,date);


        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(chartPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText(getString(R.string.day));
        tabLayout.getTabAt(1).setText(getString(R.string.week));
        tabLayout.getTabAt(2).setText(getString(R.string.month));


        tabLayout.setTabTextColors(Color.GRAY,Color.WHITE);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }





}
