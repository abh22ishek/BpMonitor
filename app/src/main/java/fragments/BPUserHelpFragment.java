package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;


public class BPUserHelpFragment extends Fragment {
    private TextView userGuide,bpKnowledge;


    HomeActivityListner homeActivityListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_help_frag,container,false);
        userGuide= (TextView) view.findViewById(R.id.bpUserGuide);
        bpKnowledge= (TextView) view.findViewById(R.id.bpKnowledge);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivityListner.onDataPass(BPUserHelpFragment.class.getName());


        userGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    BPUserGuideFragment bpUserGuideFragment = new BPUserGuideFragment();
                    fragmentTransaction.replace(R.id.fragment_container,bpUserGuideFragment);
                    fragmentTransaction.addToBackStack(BPUserGuideFragment.class.getName());
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });



        bpKnowledge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    BpGeneralKnowledgeFragment bpGeneralKnowledgeFragment = new BpGeneralKnowledgeFragment();
                    fragmentTransaction.replace(R.id.fragment_container,bpGeneralKnowledgeFragment);
                    fragmentTransaction.addToBackStack(BpGeneralKnowledgeFragment.class.getName());
                    fragmentTransaction.commit();
                    return true;
                }

                return false;
            }
        });
    }
}
