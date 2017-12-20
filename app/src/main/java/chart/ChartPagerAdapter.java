package chart;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.model.BPMeasurementModel;

import java.io.Serializable;
import java.util.List;

import constants.Constants;

public class ChartPagerAdapter extends FragmentStatePagerAdapter{


    private int nTabs;
    public List<BPMeasurementModel> mRecordDetailList;
    String date;


    public ChartPagerAdapter(FragmentManager fm, int mTabs, List<BPMeasurementModel> mRecordDetail,String date) {
        super(fm);
        this.nTabs=mTabs;
        this.mRecordDetailList=mRecordDetail;
        this.date=date;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
        case 0:
        DaysChartFragment daysChartFragment=new DaysChartFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
            bundle.putString(Constants.DATE,date);
            daysChartFragment.setArguments(bundle);
        return daysChartFragment;



        case 1:
        WeekChartFragment weekChartFragment=new WeekChartFragment();
            Bundle b=new Bundle();
            b.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
            b.putString(Constants.DATE,date);
            weekChartFragment.setArguments(b);
            return weekChartFragment;


            case 2:

                MonthChartFragment monthChartFragment =new MonthChartFragment();
                Bundle bun=new Bundle();
                bun.putSerializable(Constants.CHART, (Serializable) mRecordDetailList);
                bun.putString(Constants.DATE,date);
                monthChartFragment.setArguments(bun);
                return monthChartFragment;

            default:
        return null;
    }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
