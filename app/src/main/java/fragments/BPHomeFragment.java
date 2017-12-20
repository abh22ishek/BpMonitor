package fragments;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.edan.btdevicesdk.bean.EdanTime;
import com.edan.btdevicesdk.bean.NIBP;
import com.edan.btdevicesdk.device.NIBPDevice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import bpmonitor.bpl.com.bplbpmonitor.BplHomeScreenActivity;
import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import customviews.CustomBPChart;
import database.BPDBManager;
import database.BPDatabaseHelper;

import static constants.Constants.CURRENT_PRESSURE_VALUE;
import static constants.Constants.CURRENT_USER;
import static constants.Constants.DEVICE_TIME;
import static constants.Constants.ERROR_CODE;
import static constants.Constants.TEST_RESULT;
import static constants.Constants.WORK_MODE;
import static constants.Constants.WORK_STEP;


public class BPHomeFragment extends Fragment implements View.OnClickListener {


    TextView sysPressure,diaPressure,pulseRateText,textOngoingBP,textError;
    Button save,comment,startTest,stopTest;


    String mComment="";
    Dialog dialog;
    HomeActivityListner homeActivityListner;
    CustomBPChart customBPChart;

    private String TAG=BPHomeFragment.class.getSimpleName();
    private TextToSpeech textToSpeech;

    int pointX=0;
    int pointY=0;
    private BplHomeScreenActivity bplHomeScreenActivity;

    private RelativeLayout bpOngoingLayout,bpReadingLayout;
    boolean mStartTest,mKpa;

    TextView mmHgText,textUnit,bpPressureText;


    int mSystolicPressure=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();
        bplHomeScreenActivity = (BplHomeScreenActivity) getActivity();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bp_home_frg,container,false);
        sysPressure= (TextView) view.findViewById(R.id.sysPressure);
        diaPressure= (TextView) view.findViewById(R.id.diaPressure);
        pulseRateText= (TextView) view.findViewById(R.id.pulseRate);

        textError= (TextView) view.findViewById(R.id.textError);



        bpPressureText= (TextView) view.findViewById(R.id.bpPressureText);

        save= (Button) view.findViewById(R.id.Save);
        comment= (Button) view.findViewById(R.id.Comment);
        startTest= (Button) view.findViewById(R.id.startTest);
        stopTest= (Button) view.findViewById(R.id.stopTest);

        customBPChart= (CustomBPChart) view.findViewById(R.id.customBPView);

        bpOngoingLayout= (RelativeLayout) view.findViewById(R.id.bpOngoingLayout);
        bpReadingLayout= (RelativeLayout) view.findViewById(R.id.bpReadLayout);
        textOngoingBP= (TextView) view.findViewById(R.id.textOngoingBP);
        mmHgText=(TextView)view.findViewById(R.id.mmHgText);
        textUnit= (TextView) view.findViewById(R.id.textUnit);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bpPressureText.setText(getString(R.string.blood_press));
        homeActivityListner.onDataPass(BPHomeFragment.class.getName());
        bpOngoingLayout.setVisibility(View.VISIBLE);
        bpReadingLayout.setVisibility(View.GONE);
        stopTest.setVisibility(View.GONE);
        startTest.setOnClickListener(this);
        stopTest.setOnClickListener(this);

        save.setVisibility(View.GONE);
        comment.setVisibility(View.GONE);


        textError.setVisibility(View.GONE);
        if(bplHomeScreenActivity.getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY).equalsIgnoreCase(Constants.MM_HG)){
            textUnit.setText(getString(R.string.mm_hg));
            mmHgText.setText(getString(R.string.mm_hg));
            mKpa=false;
        }

        else{
            textUnit.setText(getString(R.string.kpa));
            mmHgText.setText(getString(R.string.kpa));
            mKpa=true;
        }



       /* if(mKpA)
        mmHgText.setText(getString(R.string.kpa));
        else
            mmHgText.setText(getString(R.string.mm_hg));*/

        /*if(getSWitchState(Constants.SWITCH_STATE_KEY).equalsIgnoreCase(SWITCH_ON))
        {

        }*/


        // check from shared pref file switch state is on or off



        textToSpeech=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.UK);



                }
            }
        });


        textToSpeech.setPitch(1f);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Database records insertion with username
                SQLiteDatabase database = BPDBManager.getInstance().openDatabase();
                database.insert(BPDatabaseHelper.TABLE_NAME,
                        null, content_values_bp_monitor(Constants.USER_NAME,
                                sysPressure.getText().toString(),diaPressure.getText().toString()
                        ,pulseRateText.getText().toString(),getCurrentDateTime(),
                                validateTypeBP(mSystolicPressure),mComment));
                Toast.makeText(getActivity(),"Record Saved",Toast.LENGTH_SHORT).show();


                save.setVisibility(View.GONE);
                if(comment.isEnabled()){
                    comment.setVisibility(View.GONE);
                }


            }
        });




        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // opens a dialog to enter text to comment
                comment_dialog(getActivity());

            }
        });






    }


    @Override
    public void onDestroyView() {


        if(textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        Logger.log(Level.DEBUG,TAG,"** On Destroy View() gets Called **");
        super.onDestroyView();

    }

    public ContentValues content_values_bp_monitor(String username, String systolicPressure, String diastolicPressure,
                                                   String pulsePerMin, String testingTime, String typeBP, String comment) {

        ContentValues values = new ContentValues();
        values.put(BPDatabaseHelper.USER_NAME, username);
        values.put(BPDatabaseHelper.SYS,systolicPressure);
        values.put(BPDatabaseHelper.DIA,diastolicPressure);
        values.put(BPDatabaseHelper.PUL,pulsePerMin);
        values.put(BPDatabaseHelper.TESTING_TIME,testingTime);
        values.put(BPDatabaseHelper.TYPE_BP,typeBP);
        values.put(BPDatabaseHelper.COMMENT,comment);
        return values;
    }





    private void comment_dialog(Context context)
    {
        dialog = new Dialog(context);
        if(dialog.getWindow()!=null){
            dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_comment);
        }

        final EditText content= (EditText) dialog.findViewById(R.id.content);
        final TextView header= (TextView) dialog.findViewById(R.id.header);
        final Button save= (Button) dialog.findViewById(R.id.save);
        final Button cancel= (Button) dialog.findViewById(R.id.cancel);

        cancel.setVisibility(View.VISIBLE);
        header.setText(getResources().getString(R.string.comment));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mComment=content.getText().toString();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    // get shared pref file

    private String getSWitchVoiceState(String key_name)
    {
        SharedPreferences switchStatePref;
        switchStatePref = getActivity().getSharedPreferences(Constants.SWITCH_STATE_PREFERENCE_FILE, Context.MODE_PRIVATE);

        final String switchState=switchStatePref.getString(key_name,Constants.SWITCH_ON);
        Logger.log(Level.INFO, TAG, "((get switch state from shared pref**=))" + switchState);
        return switchState;

    }







    // Get current date and time

    private String getCurrentDateTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.ENGLISH);
        Date date = new Date();
        return  df.format(date);
    }


    String bpCategory="";




    private String validatePressureForVoice(int systolic)
    {
        if(systolic <120)
        {
            bpCategory=getString(R.string.sound_desirable);
        }else if(systolic >=120 && systolic <=129)
        {
            bpCategory=getString(R.string.sound_normal);

        }else if(systolic >=130 && systolic <=139)
        {
            bpCategory=getString(R.string.sound_pre_hyper);
        }else if(systolic >=140 && systolic <=159)
        {
            bpCategory=getString(R.string.sound_stage1_hyper);
        }else if(systolic >=160 && systolic <=179)
        {
            bpCategory=getString(R.string.sound_stage2_hyper);
        }else if(systolic >=180)
        {
            bpCategory=getString(R.string.sound_hypersensitive);
        }

        return  bpCategory;
    }



    String typeBP="";
    private String validateTypeBP(int systolic)
    {
        if(systolic <120)
        {
            typeBP=Constants.DESIRABLE;
        }else if(systolic >=120 && systolic <=129)
        {
            typeBP=Constants.NORMAL;

        }else if(systolic >=130 && systolic <=139)
        {
            typeBP=Constants.PRE_HYPERTENSION;
        }else if(systolic >=140 && systolic <=159)
        {
            typeBP=Constants.HYPERTENSION;
        }else if(systolic >=160 && systolic <=179)
        {
            typeBP=Constants.MILD_HYPERTENSION;
        }else if(systolic >=180)
        {
            typeBP=Constants.SEVERE_HYPERTENSION;
        }

        return  typeBP;
    }





    @Override
    public void onPause() {
        super.onPause();
    }



    public void checkDeviceConnection(boolean isConnected)
    {
        if(isConnected){
            Logger.log(Level.DEBUG,TAG,"***Device is Connected***");
        }

        else{
            Logger.log(Level.DEBUG,TAG,"***Device is DisConnected***");
            Toast.makeText(getActivity(),"Device DisConnected",Toast.LENGTH_SHORT).show();
          //  getFragmentManager().popBackStackImmediate();
            if (isResumed() && !isRemoving()){
                getChildFragmentManager().popBackStack(BPBluetoothFragment.class.getName(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }



        }
    }




    public void updateError(String data){

        if(data.equalsIgnoreCase("Error"))
        {
            textError.setVisibility(View.VISIBLE);
            textError.setText(data);

        }

    }

    public void updateBPData(String data,String TAG)
    {
        if(TAG.equalsIgnoreCase(Constants.ONGOING_BP) ){

            bpPressureText.setText(getString(R.string.measuring));
            bpReadingLayout.setVisibility(View.GONE);
            bpOngoingLayout.setVisibility(View.VISIBLE);
            textOngoingBP.setText(data);
            stopTest.setVisibility(View.VISIBLE);
            startTest.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            Logger.log(Level.DEBUG,TAG,"** On Going Bp condition gets called");
        }

        else{
            bpPressureText.setText(getString(R.string.blood_press));
            stopTest.setVisibility(View.GONE);
            startTest.setVisibility(View.VISIBLE);
            comment.setVisibility(View.VISIBLE);
            bpOngoingLayout.setVisibility(View.GONE);
            bpReadingLayout.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);

            save.setEnabled(true);
            save.setAlpha(1f);

            textOngoingBP.setText(data);
            Logger.log(Level.DEBUG,TAG,"** On Read Bp condition gets called");

            String myString = data.trim().replaceAll("[\r\n]", "");
            String [] arr=myString.split(":");
            for (String anArr : arr) {
                Logger.log(Level.INFO, TAG, "Arr[]=" + anArr);
            }


            Logger.log(Level.DEBUG,TAG,"Replaced carriage**="+myString);
            final String mSystolic= arr[2].replaceAll("[^0-9]", "");
            final String mDiabolic=arr[3].replaceAll("[^0-9]", "");
            final String mPulse= arr[4].replaceAll("[^0-9]", "");

            final String mTestingTime=arr[5];
            Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mSystolic);
            Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mDiabolic);
            Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mPulse);
            Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mTestingTime);


            mSystolicPressure= Integer.parseInt(mSystolic);

            if(bplHomeScreenActivity.getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY)!=null)
            {
                if(bplHomeScreenActivity.getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY).
                        equalsIgnoreCase(Constants.MM_HG)) {
                    sysPressure.setText(mSystolic);
                    diaPressure.setText(mDiabolic);
                }else{
                    sysPressure.setText(bplHomeScreenActivity.mmHgTokPa(Integer.parseInt(mSystolic)));
                    diaPressure.setText(bplHomeScreenActivity.mmHgTokPa(Integer.parseInt(mDiabolic)));
                }
            }

            pulseRateText.setText(mPulse);

            pointX= Integer.parseInt(mSystolic);
            pointY=Integer.parseInt(mDiabolic);

            // enabled voice listner
            // set x as systolic and y as a diastolic
            customBPChart.set_XY_points(pointY,pointX);
            customBPChart.invalidate();


            if(getSWitchVoiceState(Constants.SWITCH_STATE_KEY)!=null)
            {
                if(getSWitchVoiceState(Constants.SWITCH_STATE_KEY).equalsIgnoreCase(Constants.SWITCH_ON))
                {
                    textToSpeech.speak(validatePressureForVoice(pointX),TextToSpeech.QUEUE_FLUSH,null,"uk");
                    Logger.log(Level.WARNING,TAG,"Text to voice is  working");
                }
            }
        }
    }




    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        String cmdControlStr = null;
        String cmdSetStr = null;
        String paraXML = null;
        switch(v.getId()){


            case R.id.startTest:
               // openWaitDialog("Start Test");
                textError.setVisibility(View.GONE);
                mStartTest=true;
                cmdControlStr = NIBPDevice.START_TEST;
                break;
            case R.id.stopTest:
               // openWaitDialog("Stop Test");
                // visible start save and comment

                startTest.setVisibility(View.VISIBLE);

                if(save.isEnabled()){

                    save.setVisibility(View.GONE);
                }

                if(comment.isEnabled()){
                    comment.setVisibility(View.GONE);
                }

                if(!bpPressureText.getText().toString().equals(R.string.blood_press)){
                    bpPressureText.setText(getString(R.string.blood_press));
                }



                if(!textOngoingBP.getText().toString().equals("0")){
                    textOngoingBP.setText("00");
                }
                stopTest.setVisibility(View.GONE);
                mStartTest=false;
                cmdControlStr = NIBPDevice.STOP_TEST;
                break;


            case R.id.btnDisconnect:
                cmdControlStr = NIBPDevice.DEVICE_DISCONNECT;
                break;



        }

        if(cmdSetStr != null){
            if(paraXML != null && bplHomeScreenActivity.mDeviceService != null){
                bplHomeScreenActivity.mDeviceService.SendSettingCMD(cmdSetStr, paraXML, new NIBPDevice.SetResponse() {

                    @Override
                    public void onSettingResult(int result) {
                        // TODO Auto-generated method stub
                        homeActivityListner.handleMessage(NIBPDevice.SETTING_SUCCESS);

                    }
                });
            }
        }


        if(cmdControlStr != null && bplHomeScreenActivity.mDeviceService != null){
            bplHomeScreenActivity.mDeviceService.ControlDeviceCMD(cmdControlStr, new NIBPDevice.ControlResponse() {

                @Override
                public void onDeviceTestResultData(NIBP mNIBP) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.obj = mNIBP.toString();
                    msg.what = TEST_RESULT;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentUser(int userNO) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.arg1 = userNO;
                    msg.what = CURRENT_USER;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentTime(EdanTime mEdanTime) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.obj = mEdanTime;
                    msg.what = DEVICE_TIME;
                    bplHomeScreenActivity. mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceSuccessReceiveCMD() {
                    // TODO Auto-generated method stub
                    bplHomeScreenActivity.mHandler.sendEmptyMessage(NIBPDevice.CMD_RECEIVE_SUCCESS);
                }

                @Override
                public void onDeviceWorkMode(int mode) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.arg1 = mode;
                    msg.what = WORK_MODE;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceTestErrorCode(int code) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.arg1 = code;
                    msg.what = ERROR_CODE;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);

                }

                @Override
                public void onDeviceWorkStep(int step) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.arg1 = step;
                    msg.what = WORK_STEP;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentTestValue(int value) {
                    // TODO Auto-generated method stub
                    Message msg = bplHomeScreenActivity.mHandler.obtainMessage();
                    msg.arg1 = value;
                    msg.what = CURRENT_PRESSURE_VALUE;
                    bplHomeScreenActivity.mHandler.sendMessage(msg);
                }
            });
        }


    }


}
