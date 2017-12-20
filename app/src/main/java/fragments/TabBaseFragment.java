package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bpmonitor.bpl.com.bplbpmonitor.R;
import report.ChartFragment;
import report.DataFragment;


public class TabBaseFragment extends Fragment {


    private Button chart,data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.base_tab_frag,container,false);
        chart= (Button) view.findViewById(R.id.chart);
        data= (Button) view.findViewById(R.id.data);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                ChartFragment chartFragment = new ChartFragment();
                fragmentTransaction.replace(R.id.fragment_container,chartFragment);
                fragmentTransaction.addToBackStack(TabBaseFragment.class.getName());
                fragmentTransaction.commit();
            }
        });


        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

                DataFragment dataFragment = new DataFragment();
                fragmentTransaction.replace(R.id.fragment_container,dataFragment);
                fragmentTransaction.addToBackStack(TabBaseFragment.class.getName());
                fragmentTransaction.commit();

            }
        });

    }
}
