package fragments;

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


public class BpGeneralKnowledgeFragment extends Fragment {

    HomeActivityListner homeActivityListner;
    private TextView generalKnowledge;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.bp_general_knowledge_frag,container,false);
        generalKnowledge= (TextView) view.findViewById(R.id.generalKnowledge);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        generalKnowledge.setText(getString(R.string.bp_general_knowledge));
        homeActivityListner.onDataPass(BpGeneralKnowledgeFragment.class.getName());

    }

}
