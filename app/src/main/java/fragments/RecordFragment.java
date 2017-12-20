package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.model.BPMeasurementModel;

import java.util.ArrayList;
import java.util.List;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import database.BPDBManager;
import recyclerview.BPRecyclerViewAdapter;


public class RecordFragment extends Fragment {


    RecyclerView recyclerView;
    List<BPMeasurementModel> mRecordDetailList;
    HomeActivityListner homeActivityListner;
    List<String> dateTimeList;

    private final String TAG=RecordFragment.class.getSimpleName();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(RecordFragment.class.getName());

        BPDBManager.getInstance().openDatabase();
        mRecordDetailList=new ArrayList<>();
        dateTimeList=new ArrayList<>();

        mRecordDetailList= BPDBManager.getInstance().getBPRecords(Constants.USER_NAME);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        BPRecyclerViewAdapter recyclerViewAdapter=new BPRecyclerViewAdapter(getActivity(),
                mRecordDetailList,homeActivityListner);



        // get list of dateTime

        if(mRecordDetailList!=null){

           for(BPMeasurementModel m:mRecordDetailList){

               dateTimeList.add(m.getMeasurementTime());
           }
        }

        Logger.log(Level.DEBUG,TAG,"// get Measurement Time List // "+dateTimeList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.record_header,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }












}




