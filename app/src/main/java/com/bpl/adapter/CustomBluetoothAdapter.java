package com.bpl.adapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bpmonitor.bpl.com.bplbpmonitor.R;

public class CustomBluetoothAdapter extends BaseAdapter{


    private LayoutInflater mInflater;
    private ArrayList<BluetoothDevice> blueToothList;

    public CustomBluetoothAdapter(Context context, ArrayList<BluetoothDevice> blueToothList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.blueToothList = blueToothList;
    }

    @Override
    public int getCount() {
        return blueToothList.size();
    }

    @Override
    public Object getItem(int position) {
        return blueToothList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false);
        } else {
            view = convertView;
        }
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView address = (TextView)view.findViewById(R.id.address);
        BluetoothDevice b = (BluetoothDevice)(this.getItem(position));
        name.setText(b.getName());
        address.setText(b.getAddress());
        return view;
    }
}
