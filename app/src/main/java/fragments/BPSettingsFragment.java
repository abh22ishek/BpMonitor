package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;




public class BPSettingsFragment extends Fragment{



    private Switch switchVoice;
    ToggleButton toggleUnitConversion;
    private final String TAG=BPSettingsFragment.class.getName();

    HomeActivityListner homeActivityListner;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.settings_frag,container,false);
        switchVoice= (Switch) view.findViewById(R.id.switchVoice);
        toggleUnitConversion= (ToggleButton) view.findViewById(R.id.unitConversion);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(BPSettingsFragment.class.getName());


        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    saveSwitchStatePref(getActivity(),Constants.SWITCH_STATE_KEY,Constants.SWITCH_ON);
                }
                else{
                    saveSwitchStatePref(getActivity(),Constants.SWITCH_STATE_KEY,Constants.SWITCH_OFF);
                }

            }
        });





        toggleUnitConversion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    saveCurrentUnitMeasurement(getActivity(),Constants.MM_UNIT_MEASUREMENT_KEY,Constants.MM_HG);
                }else{
                    saveCurrentUnitMeasurement(getActivity(),Constants.MM_UNIT_MEASUREMENT_KEY,Constants.KPA);

                }
            }
        });


    }



    // save switch state in sharedPref file

    private void saveSwitchStatePref(Context context,String key, String toggleState)
    {

        SharedPreferences switchStatePref;
        switchStatePref=context.getSharedPreferences(Constants.SWITCH_STATE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =switchStatePref.edit();
        editor.putString(key,toggleState);
        editor.apply();

        Logger.log(Level.DEBUG, TAG, "// *shared preference switch state gets stored // *"+toggleState);

    }



    private void saveCurrentUnitMeasurement(Context context,String key, String switchState)
    {
        SharedPreferences togglePref;
        togglePref=context.getSharedPreferences(Constants.TOGGLE_STATE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =togglePref.edit();
        editor.putString(key,switchState);
        editor.apply();
        Logger.log(Level.DEBUG, TAG, "// *shared preference toggle state gets stored // *"+switchState);
    }





    @Override
    public void onPause() {
        super.onPause();
    }
}
