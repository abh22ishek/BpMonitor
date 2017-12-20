package bpmonitor.bpl.com.bplbpmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.edan.btdevicesdk.device.DeviceFactory;
import com.edan.btdevicesdk.device.NIBPDevice;
import com.edan.btdevicesdk.service.NIBPService;
import com.model.BPMeasurementModel;

import java.io.Serializable;
import java.util.List;

import constants.Constants;
import database.BPDBManager;
import database.BPDatabaseHelper;
import fragments.BPBaseChartFragment;
import fragments.BPBluetoothFragment;
import fragments.BPHomeFragment;
import fragments.BPOngoingMeasurement;
import fragments.BPSettingsFragment;
import fragments.BPUserGuideFragment;
import fragments.BPUserHelpFragment;
import fragments.BpGeneralKnowledgeFragment;
import fragments.RecordFragment;
import report.ReportFragment;


public class BplHomeScreenActivity extends FragmentActivity  implements HomeActivityListner {


    private boolean mExit;
    public NIBPService mDeviceService;
    private final String TAG=BplHomeScreenActivity.class.getSimpleName();
    private RelativeLayout header;
    private BPHomeFragment bpHomeFragment;
    private boolean mOngoingMeasurement;



    @Override
    public void onDataPass(String data) {

        header.setVisibility(View.VISIBLE);
        if(data.equalsIgnoreCase(BPOngoingMeasurement.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.VISIBLE);
            help.setVisibility(View.VISIBLE);
            menuRecords.setVisibility(View.VISIBLE);
            headerTitle.setVisibility(View.INVISIBLE);

        }else if(data.equalsIgnoreCase(BPSettingsFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.settings));
        }

        else if(data.equalsIgnoreCase(BPHomeFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.VISIBLE);
            help.setVisibility(View.VISIBLE);
            menuRecords.setVisibility(View.VISIBLE);
            headerTitle.setVisibility(View.INVISIBLE);
        }
        else if(data.equalsIgnoreCase(BPUserGuideFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.bp_user_guide));
        }
        else if(data.equalsIgnoreCase(BpGeneralKnowledgeFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.gen_knowledge));
        }

        else if(data.equalsIgnoreCase(BPUserHelpFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.user_help));
        }

        else if(data.equalsIgnoreCase(RecordFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.record));
        }
        else if(data.equalsIgnoreCase(BPBluetoothFragment.class.getName()))
        {
            backKey.setVisibility(View.GONE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.GONE);
        }else if(data.equalsIgnoreCase(BPBaseChartFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.chart));
        }else if(data.equalsIgnoreCase(ReportFragment.class.getName()))
        {
            backKey.setVisibility(View.VISIBLE);
            welcomeStatus.setVisibility(View.GONE);
            settings.setVisibility(View.GONE);
            help.setVisibility(View.GONE);
            menuRecords.setVisibility(View.GONE);
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(getString(R.string.report));
        }
    }

    @Override
    public void handleMessage(int state) {
        mHandler.sendEmptyMessage(state);

    }

    @Override
    public void invokeFragment(String tag,String mSystolic,String mDiabolic,String mPulse,String mTestingTime,String mComment) {

        android.support.v4.app.FragmentManager fragmentManager =getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();


            ReportFragment reportFragment=new ReportFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.systolic,mSystolic);
            bundle.putString(Constants.diabolic,mDiabolic);
            bundle.putString(Constants.pulse,mPulse);
            bundle.putString(Constants.testing_time,mTestingTime);
            bundle.putString(Constants.comment,mComment);

            reportFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.fragment_container,reportFragment);
            fragmentTransaction.addToBackStack(ReportFragment.class.getName());
            fragmentTransaction.commit();


    }

    @Override
    public void invokeRecordList(List<BPMeasurementModel> recordDetailList,String date) {

        android.support.v4.app.FragmentManager fragmentManager =getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();


        BPBaseChartFragment bpBaseChartFragment=new BPBaseChartFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable(Constants.CHART, (Serializable) recordDetailList);
        bundle.putString(Constants.DATE,date);
        bpBaseChartFragment.setArguments(bundle);


            fragmentTransaction.replace(R.id.fragment_container,bpBaseChartFragment);
            fragmentTransaction.addToBackStack(BPBaseChartFragment.class.getName());
            fragmentTransaction.commit();



    }

    private TextView headerTitle,welcomeStatus;
    private ImageView settings,backKey,help,menuRecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_activity);


       /*
        if(getIntent().getExtras().getString(Constants.USER_NAME)!=null ||
                !getIntent().getExtras().getString(Constants.USER_NAME).equals("")){


           Constants.LOGGED_USER_NAME= getIntent().getExtras().getString(Constants.USER_NAME);
        }*/

        // Initialize database
        BPDBManager.initializeInstance(new BPDatabaseHelper(BplHomeScreenActivity.this));
        try {
            mDeviceService = DeviceFactory.createDevice(DeviceFactory.DEVICE_EDAN_NIBP,
                    this,BplHomeScreenActivity.this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Logger.log(Level.WARNING,TAG,e.getMessage());

        }


        header= (RelativeLayout) findViewById(R.id.include);

        help= (ImageView) findViewById(R.id.help);
        help.setOnClickListener(mListner);

        menuRecords= (ImageView) findViewById(R.id.menu);
        menuRecords.setOnClickListener(mListner);

        headerTitle= (TextView) findViewById(R.id.base_header_title);
        headerTitle.setText(getString(R.string.bpl_bump));

        settings= (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(mListner);




        backKey= (ImageView) findViewById(R.id.imgBackKey);
        backKey.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!mOngoingMeasurement){
                    FragmentManager fm=getSupportFragmentManager();

                    if(fm.getBackStackEntryCount()>1) {
                        fm.popBackStack();
                    }else
                        finish();
                }


            }
        });


        if(getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY)!=null)
        {
            Logger.log(Level.DEBUG,TAG,"((***Get current Unit***))="+getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY));
        }


        welcomeStatus= (TextView) findViewById(R.id.welcomeStatus);
        // Assuming BPBLuetooth fragment is first screen
        header.setVisibility(View.GONE);



        if(getIntent().getExtras()!=null)
        {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            RecordFragment recordFragment = new RecordFragment();
            fragmentTransaction.replace(R.id.fragment_container,recordFragment);
            fragmentTransaction.addToBackStack(ReportFragment.class.getName());
            fragmentTransaction.commit();
        }else{
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            BPBluetoothFragment bpBluetoothFragment = new BPBluetoothFragment();
            fragmentTransaction.replace(R.id.fragment_container,bpBluetoothFragment);
            fragmentTransaction.addToBackStack(BPBluetoothFragment.class.getName());
            fragmentTransaction.commit();
        }




// app updated

    }


    private View.OnClickListener mListner= new View.OnClickListener() {


        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.settings:

                    if(mOngoingMeasurement){
                        Toast.makeText(BplHomeScreenActivity.this,getString(R.string.measurement_going),
                                Toast.LENGTH_SHORT).show();
                    }else{
                        backKey.setVisibility(View.VISIBLE);
                        welcomeStatus.setVisibility(View.GONE);
                        android.support.v4.app.FragmentManager fragmentManager1 =getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction1=fragmentManager1.beginTransaction();
                        BPSettingsFragment bpSettingsFragment = new BPSettingsFragment();
                        fragmentTransaction1.replace(R.id.fragment_container,bpSettingsFragment);
                        fragmentTransaction1.addToBackStack(BPSettingsFragment.class.getName());
                        fragmentTransaction1.commit();
                    }

                    break;

                case R.id.help:
                    if(mOngoingMeasurement) {
                        Toast.makeText(BplHomeScreenActivity.this, getString(R.string.measurement_going),
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        android.support.v4.app.FragmentManager fragmentManager2 =getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction2=fragmentManager2.beginTransaction();
                        BPUserHelpFragment bpUserHelpFragment=new BPUserHelpFragment();
                        fragmentTransaction2.replace(R.id.fragment_container,bpUserHelpFragment);
                        fragmentTransaction2.addToBackStack(BPUserHelpFragment.class.getName());
                        fragmentTransaction2.commit();
                    }

                    break;


                case R.id.menu:

                    if(mOngoingMeasurement) {
                        Toast.makeText(BplHomeScreenActivity.this, getString(R.string.measurement_going),
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        android.support.v4.app.FragmentManager fragmentManager3 =getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction3=fragmentManager3.beginTransaction();
                        RecordFragment recordFragment=new RecordFragment();
                        fragmentTransaction3.replace(R.id.fragment_container,recordFragment);
                        fragmentTransaction3.addToBackStack(RecordFragment.class.getName());
                        fragmentTransaction3.commit();
                    }

                    break;


            }


        }
    };

    String mTestResult="";
    int mResultCount=0;
    int mCurrentValueCount=1;


    public  Handler mHandler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case NIBPDevice.CONNECTING:
                    Logger.log(Level.DEBUG,TAG,"Connecting to BPUMP ..");

                  /*  mConnectedState="Connecting Device...";
                    connectedState.setVisibility(View.VISIBLE);
                    connectedState.setText("Connecting Device...");
                    connectedState.setTextColor(Color.BLUE);*/
                    break;
                case NIBPDevice.DISCONNECTED:
                    Logger.log(Level.DEBUG,TAG,"DISCONNECTED to BPUMP");
                    bpHomeFragment = (BPHomeFragment) getSupportFragmentManager().
                            findFragmentByTag(BPHomeFragment.class.getName());

                    if(bpHomeFragment!=null){
                        bpHomeFragment.checkDeviceConnection(false);
                    }
                    //mConnectedState="Device Disconnected...";
                    //connectedState.setVisibility(View.VISIBLE);
                    // connectedState.setText("Device Disconnected");
                    // connectedState.setTextColor(Color.RED);
                    break;
                case NIBPDevice.CONNECTED:
                    Logger.log(Level.DEBUG,TAG,"CONNECTED to BPUMP");
                    android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    bpHomeFragment=new BPHomeFragment();

                   /* Bundle bundle=new Bundle();
                    bundle.putString(Constants.systolic,mSystolic);
                    bundle.putString(Constants.diabolic,mDiabolic);
                    bundle.putString(Constants.pulse,mPulse);

                    bundle.putString(Constants.testing_time,mTestingTime);
                    bpHomeFragment.setArguments(bundle);*/

                    fragmentTransaction.replace(R.id.fragment_container,bpHomeFragment,BPHomeFragment.class.getName());
                    fragmentTransaction.addToBackStack(BPHomeFragment.class.getName());
                    fragmentTransaction.commit();
                    bpHomeFragment.checkDeviceConnection(true);

                    // mConnectedState="Device Connected";
                    // connectedState.setVisibility(View.VISIBLE);
                    // connectedState.setText("Device Connected");
                    // connectedState.setTextColor(Color.GREEN);
                    // bottomLayout.setVisibility(View.VISIBLE);

                    break;
                case NIBPDevice.CMD_RECEIVE_SUCCESS:
                case NIBPDevice.SETTING_SUCCESS:

                    mResultCount=0;
                    mCurrentValueCount=1;
                    Toast.makeText(BplHomeScreenActivity.this, "send successfully", Toast.LENGTH_SHORT).show();
                    break;

                case Constants.TEST_RESULT:

                    mTestResult="Test Result:" +msg.obj;
                    Logger.log(Level.DEBUG,TAG,"//****//"+mTestResult);
                    if(bpHomeFragment!=null)
                    {
                        if(mResultCount==0){
                            bpHomeFragment.updateBPData(mTestResult,Constants.MEASURED_BP);
                        }

                        mResultCount++;
                    }


                    //  tvTestResult.setText("Test Result: " + (String)msg.obj);
                    break;
                case Constants.CURRENT_PRESSURE_VALUE:

                    if(mCurrentValueCount>0)
                    {
                        mOngoingMeasurement=true;
                        Logger.log(Level.INFO,TAG,"// *** Current Pressure Value*** Ongoing Measurement //"+msg.arg1);
                        if(bpHomeFragment!=null && getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY)!=null)
                        {
                            if(getCurrentUnit(Constants.MM_UNIT_MEASUREMENT_KEY).equalsIgnoreCase(Constants.MM_HG)) {
                                bpHomeFragment.updateBPData("" + msg.arg1, Constants.ONGOING_BP);
                            }
                            else
                                bpHomeFragment.updateBPData(mmHgTokPa(msg.arg1),Constants.ONGOING_BP);
                        }

                        Logger.log(Level.INFO,TAG,"// *** Current value count*** //"+mCurrentValueCount);
                        mCurrentValueCount++;
                    }

                    break;



                case Constants.ERROR_CODE:
                    switch(msg.arg1){
                        case NIBPDevice.ERROR_CODE_NORMAL:
                            Logger.log(Level.DEBUG,TAG,"Error information: No Error ");

                            // tvErrorCode.setText("Error information: No Error");
                            break;
                        case NIBPDevice.ERROR_CODE_CORRECT_RESULT:
                            Logger.log(Level.DEBUG,TAG,"Error information: No Error ");

                            // tvErrorCode.setText("Error information: No Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_2:
                            Logger.log(Level.DEBUG,TAG,"Error information: Test Error ");

                            // tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_3:
                            Logger.log(Level.DEBUG,TAG,"Error information: Test Error ");

                            //tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_5:
                            Logger.log(Level.DEBUG,TAG,"Error information: Test Error ");

                            //tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_PRESSURE_OVER_PROTECTION:
                            Logger.log(Level.DEBUG,TAG,"Error information: Pressure over Protection");

                            //tvErrorCode.setText("Error information: Pressure over Protection");
                            break;
                        case NIBPDevice.ERROR_CODE_RETURN_TO_ZERO_TIMEOUT:
                            Logger.log(Level.DEBUG,TAG,"Error information: Return to zero timeout");

                            // tvErrorCode.setText("Error information: Return to zero timeout");
                            break;
                        case NIBPDevice.ERROR_CODE_RETURN_TO_ZERO_OUT_OF_RANGE:
                            Logger.log(Level.DEBUG,TAG,"Error information: return to zero out of range");

                            // tvErrorCode.setText("Error information: return to zero out of range");
                            break;
                        case NIBPDevice.ERROR_CODE_LOW_BATTERY:
                            Logger.log(Level.DEBUG,TAG,"Error information: Low battery,the Device will be shut off");
                            //tvErrorCode.setText("Error information: Low battery,the Device will be shut off");

                            break;
                        case NIBPDevice.ERROR_CODE_LOOSE_CUFF:
                            Logger.log(Level.DEBUG,TAG,"Error information:Loose cuff");
                            // tvErrorCode.setText("Error information: Loose cuff");
                            if(bpHomeFragment!=null)
                            {
                                bpHomeFragment.updateError("Error");
                            }
                            break;
                    }
                    break;
                case Constants.WORK_MODE:
                    switch(msg.arg1){
                        case NIBPDevice.WORK_MODE_SLEEP://Sleep Mode
                            Logger.log(Level.DEBUG,TAG,"Device Current Mode: Sleep Mode");
                            // tvWorkMode.setText("Device Current Mode: Sleep Mode");
                            break;
                        case NIBPDevice.WORK_MODE_STANDBY://Standby Mode
                            Logger.log(Level.DEBUG,TAG,"Device Current Mode: Standby Mode");
                            mCurrentValueCount=0;
                            mOngoingMeasurement=false;
                            //tvWorkMode.setText("Device Current Mode: Standby Mode");
                            break;
                        case NIBPDevice.WORK_MODE_MEASURE://Measure Mode
                            Logger.log(Level.DEBUG,TAG,"Device Current Mode: Measure Mode");
                            //tvWorkMode.setText("Device Current Mode: Measure Mode");
                            break;
                        case NIBPDevice.WORK_MODE_QUERY://Query Mode
                            Logger.log(Level.DEBUG,TAG,"Device Current Mode: Query Mode");
                            // tvWorkMode.setText("Device Current Mode: Query Mode");
                            break;
                        case NIBPDevice.WORK_MODE_SETUP://Set up Mode
                            Logger.log(Level.DEBUG,TAG,"Device Current Mode: Set up Mode");
                            // tvWorkMode.setText("Device Current Mode: Set up Mode");
                            break;
                    }
                    break;
                case Constants.WORK_STEP:
                    switch(msg.arg1){
                        case NIBPDevice.WORK_STEP_INIT://Init Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Init Stage");
                            // tvWorkStep.setText("Device Current Stage: Init Stage");
                            break;
                        case NIBPDevice.WORK_STEP_TO_ZERO://Return To Zero Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Return To Zero Stage");
                            // tvWorkStep.setText("Device Current Stage: Return To Zero Stage");
                            break;
                        case NIBPDevice.WORK_STEP_LISTEN://Listen Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Listen Stage");
                            //  tvWorkStep.setText("Device Current Stage: Listen Stage");
                            break;
                        case NIBPDevice.WORK_STEP_RAPID_INFLATION://Rapid Inflation Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Rapid Inflation Stage");
                            // tvWorkStep.setText("Device Current Stage: Rapid Inflation Stage");
                            break;
                        case NIBPDevice.WORK_STEP_UNIFORM_INFLATION://Uniform Inflation Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Uniform Inflation Stage");
                            // tvWorkStep.setText("Device Current Stage: Uniform Inflation Stage");
                            break;
                        case NIBPDevice.WORK_STEP_STATIC_PRESSURE://Static Pressure Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Static Pressure Stage");
                            // tvWorkStep.setText("Device Current Stage: Static Pressure Stage");
                            break;
                        case NIBPDevice.WORK_STEP_TEST_END://Test End Stage
                            Logger.log(Level.DEBUG,TAG,"Device Current Stage: Test End Stage");
                            // tvWorkStep.setText("Device Current Stage: Test End Stage");
                            break;
                    }
                    break;

            }
        }
    };


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        FragmentManager fm=getSupportFragmentManager();
        if(fm.getBackStackEntryCount()>1) {
            fm.popBackStack();
        }
        else
        {
            if (mExit) {
                super.onBackPressed();
                this.finishAffinity(); // removes the activity from same task

            } else {
                Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                mExit = true;
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  mExit = false;
                                              }
                                          },3*1000);
            }
        }



    }


    // get shared pref file for current Unit Measurement
    public String getCurrentUnit(String keyName)
    {
        SharedPreferences toggleStatePref;
        toggleStatePref = BplHomeScreenActivity.this.
                getSharedPreferences(Constants.TOGGLE_STATE_PREFERENCE_FILE, Context.MODE_PRIVATE);

        final String toggleUnitMeasurementState=toggleStatePref.getString(keyName,Constants.MM_HG);
        Logger.log(Level.INFO, TAG, "((get current unit state from shared pref**=))" + toggleUnitMeasurementState);
        return toggleUnitMeasurementState;

    }

    // convert mmHg to kPa
    public String mmHgTokPa(int mmHg)
    {
        float kPaValue;
        kPaValue = (float) (mmHg *0.133322);
        return String.format("%.1f",kPaValue);
    }



}
