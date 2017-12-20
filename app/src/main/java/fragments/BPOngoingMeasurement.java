package fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpl.adapter.CustomBluetoothAdapter;
import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.edan.btdevicesdk.bean.EdanTime;
import com.edan.btdevicesdk.bean.NIBP;
import com.edan.btdevicesdk.device.DeviceFactory;
import com.edan.btdevicesdk.device.NIBPDevice;
import com.edan.btdevicesdk.service.NIBPService;

import java.util.ArrayList;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;

import static constants.Constants.PERMISSION_REQUEST_COARSE_LOCATION;


public class BPOngoingMeasurement extends Fragment implements View.OnClickListener{

    RelativeLayout circularBall;
    HomeActivityListner homeActivityListner;

    LinearLayout bottomLayout;


    private Button btnFindDevice,btnStartTest,btnStopTest,
            btnDisconnect;

    private TextView tvWorkMode,tvWorkStep,
            tvErrorCode,tvCurrentValue;


    private NIBPService mDeviceService;
    private ListView blueToothListView;
    public CustomBluetoothAdapter mBlueAdapter;
    public ArrayList<BluetoothDevice> blueToothList;
    private ProgressDialog dialog;
    private static final int TEST_RESULT = 1000;
    private static final int CURRENT_DATA = 1001;
    private static final int WORK_MODE = 1002;
    private static final int WORK_STEP = 1003;
    private static final int ERROR_CODE = 1004;
    private static final int CURRENT_PRESSURE_VALUE = 1005;
    private static final int DEVICE_TIME = 1006;
    private static final int CURRENT_USER = 1007;
    private static final int REQUEST_CODE_SELECT_BLUETOOTH = 1008;


    String mConnectedState="";
    String mTestResult="";

    String mSystolic="0";
    String mDiabolic="0";
    String mPulse="0";
    String mTestingTime="";


    private TextView connectedState;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.ongoing_frag,container,false);
        circularBall= (RelativeLayout) view.findViewById(R.id.circular_ball);
        connectedState= (TextView) view.findViewById(R.id.connectedState);
        bottomLayout= (LinearLayout) view.findViewById(R.id.bottomLayout);
        connectedState.setVisibility(View.INVISIBLE);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(BPOngoingMeasurement.class.getName());

        circularBall.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.INVISIBLE);
        blueToothListView.setVisibility(View.VISIBLE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final LocationManager manager = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );

            if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(getActivity(),"Please Enable your Location" +
                        " in your device Settings",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }





        circularBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();


                BPHomeFragment bpHomeFragment=new BPHomeFragment();
                Bundle bundle=new Bundle();
                bundle.putString(Constants.systolic,mSystolic);
                bundle.putString(Constants.diabolic,mDiabolic);
                bundle.putString(Constants.pulse,mPulse);

                bundle.putString(Constants.testing_time,mTestingTime);
                bpHomeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,bpHomeFragment);
                fragmentTransaction.addToBackStack(BPHomeFragment.class.getName());
                fragmentTransaction.commit();

            }
        });

    }



    private void initView(View view){
        btnFindDevice = (Button) view.findViewById(R.id.btnFindDevice);
      //  tvConnectState = (TextView) view.findViewById(R.id.tvConnectState);
        btnStartTest = (Button) view.findViewById(R.id.btnStartTest);
        btnStopTest = (Button) view.findViewById(R.id.btnStopTest);
        btnDisconnect = (Button) view.findViewById(R.id.btnDisconnect);

        blueToothListView = (ListView)view. findViewById(R.id.listDevices);

        // tvTestResult = (TextView)view. findViewById(R.id.tvTestReuslt);
        tvWorkMode = (TextView)view. findViewById(R.id.tvWorkMode);
        tvWorkStep = (TextView)view. findViewById(R.id.tvWorkStep);
        tvErrorCode = (TextView)view. findViewById(R.id.tvErrorCode);
        tvCurrentValue = (TextView)view. findViewById(R.id.mmHgValue);


        mConnectedState="Device Disconnected";


        try {
            mDeviceService = DeviceFactory.createDevice(DeviceFactory.DEVICE_EDAN_NIBP,getActivity(),getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        btnFindDevice.setOnClickListener(this);
        btnStartTest.setOnClickListener(this);
        btnStopTest.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);

        blueToothList = new ArrayList<>();
        mBlueAdapter = new CustomBluetoothAdapter(getActivity(),blueToothList);

        blueToothListView.setAdapter(mBlueAdapter);
        blueToothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (!blueToothList.get(position).getAddress().toString().equals("")) {
                    String address = blueToothList.get(position).getAddress().toString();
                    if(mDeviceService != null){
                        mDeviceService.ConnectDevice(address, new NIBPDevice.ConnectResponse() {

                            @Override
                            public void ConnectState(int state) {
                                // TODO Auto-generated method stub

                                mHandler.sendEmptyMessage(state);
                            }
                        });
                    }
                }

            }
        });
    }


    public Handler mHandler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case NIBPDevice.CONNECTING:

                    mConnectedState="Connecting Device...";
                    connectedState.setVisibility(View.VISIBLE);
                    connectedState.setText("Connecting Device...");
                    connectedState.setTextColor(Color.BLUE);
                    break;
                case NIBPDevice.DISCONNECTED:
                    mConnectedState="Device Disconnected...";
                    connectedState.setVisibility(View.VISIBLE);
                    connectedState.setText("Device Disconnected");
                    connectedState.setTextColor(Color.RED);
                    break;
                case NIBPDevice.CONNECTED:

                    mConnectedState="Device Connected";
                    connectedState.setVisibility(View.VISIBLE);
                    connectedState.setText("Device Connected");
                    connectedState.setTextColor(Color.GREEN);
                    bottomLayout.setVisibility(View.VISIBLE);

                    break;
                case NIBPDevice.CMD_RECEIVE_SUCCESS:
                case NIBPDevice.SETTING_SUCCESS:
                    if(dialog != null){
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    }
                    Toast.makeText(getActivity(), "send successfully", Toast.LENGTH_SHORT).show();
                    break;

                case TEST_RESULT:

                    mTestResult="Test Result:" +msg.obj;
                    Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"//****//"+mTestResult);

                    mSystolic=msg.obj.toString().trim().substring(10,13);
                    mDiabolic=msg.obj.toString().trim().substring(25,27);
                    mPulse=msg.obj.toString().trim().substring(35,38);

                    mTestingTime=msg.obj.toString().trim().substring(45,60);

                    Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mSystolic);
                    Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mDiabolic);
                    Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mPulse);
                    Logger.log(Level.DEBUG,BPOngoingMeasurement.class.getSimpleName(),"****"+mTestingTime);



                    //  tvTestResult.setText("Test Result: " + (String)msg.obj);
                    break;
                case CURRENT_PRESSURE_VALUE:
                    circularBall.setVisibility(View.VISIBLE);
                    blueToothListView.setVisibility(View.GONE);
                    tvCurrentValue.setText("" + msg.arg1);

                    break;
                case ERROR_CODE:
                    switch(msg.arg1){
                        case NIBPDevice.ERROR_CODE_NORMAL:
                            tvErrorCode.setText("Error information: No Error");
                            break;
                        case NIBPDevice.ERROR_CODE_CORRECT_RESULT:
                            tvErrorCode.setText("Error information: No Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_2:
                            tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_3:
                            tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_TEST_ERROR_5:
                            tvErrorCode.setText("Error information: Test Error");
                            break;
                        case NIBPDevice.ERROR_CODE_PRESSURE_OVER_PROTECTION:
                            tvErrorCode.setText("Error information: Pressure over Protection");
                            break;
                        case NIBPDevice.ERROR_CODE_RETURN_TO_ZERO_TIMEOUT:
                            tvErrorCode.setText("Error information: Return to zero timeout");
                            break;
                        case NIBPDevice.ERROR_CODE_RETURN_TO_ZERO_OUT_OF_RANGE:
                            tvErrorCode.setText("Error information: return to zero out of range");
                            break;
                        case NIBPDevice.ERROR_CODE_LOW_BATTERY:
                            tvErrorCode.setText("Error information: Low battery,the Device will be shut off");
                            break;
                        case NIBPDevice.ERROR_CODE_LOOSE_CUFF:
                            tvErrorCode.setText("Error information: Loose cuff");
                            break;
                    }
                    break;
                case WORK_MODE:
                    switch(msg.arg1){
                        case NIBPDevice.WORK_MODE_SLEEP://Sleep Mode
                            tvWorkMode.setText("Device Current Mode: Sleep Mode");
                            break;
                        case NIBPDevice.WORK_MODE_STANDBY://Standby Mode
                            tvWorkMode.setText("Device Current Mode: Standby Mode");
                            break;
                        case NIBPDevice.WORK_MODE_MEASURE://Measure Mode
                            tvWorkMode.setText("Device Current Mode: Measure Mode");
                            break;
                        case NIBPDevice.WORK_MODE_QUERY://Query Mode
                            tvWorkMode.setText("Device Current Mode: Query Mode");
                            break;
                        case NIBPDevice.WORK_MODE_SETUP://Set up Mode
                            tvWorkMode.setText("Device Current Mode: Set up Mode");
                            break;
                    }
                    break;
                case WORK_STEP:
                    switch(msg.arg1){
                        case NIBPDevice.WORK_STEP_INIT://Init Stage
                            tvWorkStep.setText("Device Current Stage: Init Stage");
                            break;
                        case NIBPDevice.WORK_STEP_TO_ZERO://Return To Zero Stage
                            tvWorkStep.setText("Device Current Stage: Return To Zero Stage");
                            break;
                        case NIBPDevice.WORK_STEP_LISTEN://Listen Stage
                            tvWorkStep.setText("Device Current Stage: Listen Stage");
                            break;
                        case NIBPDevice.WORK_STEP_RAPID_INFLATION://Rapid Inflation Stage
                            tvWorkStep.setText("Device Current Stage: Rapid Inflation Stage");
                            break;
                        case NIBPDevice.WORK_STEP_UNIFORM_INFLATION://Uniform Inflation Stage
                            tvWorkStep.setText("Device Current Stage: Uniform Inflation Stage");
                            break;
                        case NIBPDevice.WORK_STEP_STATIC_PRESSURE://Static Pressure Stage
                            tvWorkStep.setText("Device Current Stage: Static Pressure Stage");
                            break;
                        case NIBPDevice.WORK_STEP_TEST_END://Test End Stage
                            tvWorkStep.setText("Device Current Stage: Test End Stage");
                            break;
                    }
                    break;

            }
        }
    };



    @Override
    public void onClick(View v) {


// TODO Auto-generated method stub
        String cmdControlStr = null;
        String cmdSetStr = null;
        String paraXML = null;
        switch(v.getId()){

            case R.id.btnFindDevice:

                if(blueToothList != null){
                    blueToothList.clear();
                }
                mDeviceService.FindDevice(new NIBPDevice.FindResponse() {

                    @Override
                    public void onFindDevice(BluetoothDevice mBluetoothDevice) {
                        // TODO Auto-generated method stub
                        if(!blueToothList.contains(mBluetoothDevice)){
                            blueToothList.add(mBluetoothDevice);
                            mBlueAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;

            case R.id.btnStartTest:
                openWaitDialog("Start Test");
                cmdControlStr = NIBPDevice.START_TEST;
                break;
            case R.id.btnStopTest:
                openWaitDialog("Stop Test");
                cmdControlStr = NIBPDevice.STOP_TEST;
                break;
            case R.id.btnDisconnect:
                cmdControlStr = NIBPDevice.DEVICE_DISCONNECT;
                break;



        }

        if(cmdSetStr != null){
            if(paraXML != null && mDeviceService != null){
                mDeviceService.SendSettingCMD(cmdSetStr, paraXML, new NIBPDevice.SetResponse() {

                    @Override
                    public void onSettingResult(int result) {
                        // TODO Auto-generated method stub
                        mHandler.sendEmptyMessage(NIBPDevice.SETTING_SUCCESS);
                    }
                });
            }
        }

        if(cmdControlStr != null && mDeviceService != null){
            mDeviceService.ControlDeviceCMD(cmdControlStr, new NIBPDevice.ControlResponse() {

                @Override
                public void onDeviceTestResultData(NIBP mNIBP) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.obj = mNIBP.toString();
                    msg.what = TEST_RESULT;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentUser(int userNO) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = userNO;
                    msg.what = CURRENT_USER;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentTime(EdanTime mEdanTime) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.obj = mEdanTime;
                    msg.what = DEVICE_TIME;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceSuccessReceiveCMD() {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(NIBPDevice.CMD_RECEIVE_SUCCESS);
                }

                @Override
                public void onDeviceWorkMode(int mode) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = mode;
                    msg.what = WORK_MODE;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceTestErrorCode(int code) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = code;
                    msg.what = ERROR_CODE;
                    mHandler.sendMessage(msg);

                }

                @Override
                public void onDeviceWorkStep(int step) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = step;
                    msg.what = WORK_STEP;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onDeviceCurrentTestValue(int value) {
                    // TODO Auto-generated method stub
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = value;
                    msg.what = CURRENT_PRESSURE_VALUE;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }



    private void openWaitDialog(String msg){
        if(dialog == null){
            dialog = new ProgressDialog(getActivity());
        }else if(dialog.isShowing()){
            dialog.dismiss();
        }
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(msg);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Failed,please click it again",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, 2000);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(requestCode==Constants.PERMISSION_REQUEST_COARSE_LOCATION)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

        }

    }



}





