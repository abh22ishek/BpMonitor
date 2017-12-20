package fragments;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.bpl.adapter.CustomBluetoothAdapter;
import com.edan.btdevicesdk.device.NIBPDevice;

import java.util.ArrayList;

import bpmonitor.bpl.com.bplbpmonitor.BplHomeScreenActivity;
import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;

import static constants.Constants.PERMISSION_REQUEST_COARSE_LOCATION;

public class BPBluetoothFragment extends Fragment {

    public ArrayList<BluetoothDevice> blueToothList;
    public CustomBluetoothAdapter mBlueAdapter;
    private ListView blueToothListView;
  
    HomeActivityListner homeActivityListner;
    Button btnFindDevice;
    BplHomeScreenActivity bplHomeScreenActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner = (HomeActivityListner) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.ongoing_frg,container,false);
        btnFindDevice= (Button) view.findViewById(R.id.btnFindDevice);
        blueToothListView = (ListView)view. findViewById(R.id.listDevices);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(BPBluetoothFragment.class.getName());


        bplHomeScreenActivity = (BplHomeScreenActivity) getActivity();

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





        blueToothList = new ArrayList<>();
        mBlueAdapter = new CustomBluetoothAdapter(getActivity(),blueToothList);

        blueToothListView.setAdapter(mBlueAdapter);
        blueToothListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (!blueToothList.get(position).getAddress().toString().equals("")) {
                    String address = blueToothList.get(position).getAddress().toString();
                    if(bplHomeScreenActivity.mDeviceService != null){
                        bplHomeScreenActivity.mDeviceService.ConnectDevice(address, new NIBPDevice.ConnectResponse() {

                            @Override
                            public void ConnectState(int state) {
                                // TODO Auto-generated method stub
                                homeActivityListner.handleMessage(state);

                            }
                        });
                    }
                }

            }
        });




        btnFindDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(blueToothList != null){
                    blueToothList.clear();
                }
                bplHomeScreenActivity.mDeviceService.FindDevice(new NIBPDevice.FindResponse() {

                    @Override
                    public void onFindDevice(BluetoothDevice mBluetoothDevice) {
                        // TODO Auto-generated method stub
                        if(!blueToothList.contains(mBluetoothDevice)){
                            blueToothList.add(mBluetoothDevice);
                            mBlueAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });


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
