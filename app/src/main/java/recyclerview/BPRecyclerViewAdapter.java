package recyclerview;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import com.model.BPMeasurementModel;

import java.util.ArrayList;
import java.util.List;

import bpmonitor.bpl.com.bplbpmonitor.HomeActivityListner;
import bpmonitor.bpl.com.bplbpmonitor.R;
import constants.Constants;
import customviews.CustomViewBPCategory;
import database.BPDBManager;


public class BPRecyclerViewAdapter extends RecyclerView.Adapter<BPRecyclerViewAdapter.CustomViewHolder>{

    private List<BPMeasurementModel> recordsDetailList;
    private List<BPMeasurementModel> forDayRecordList;
    private Context context;
    private Dialog dialog;
    HomeActivityListner  homeActivityListner ;




    private String TAG=BPRecyclerViewAdapter.class.getSimpleName();

    public BPRecyclerViewAdapter(Context context, List<BPMeasurementModel> recordsDetailList,HomeActivityListner listner) {
        this.context = context;
        this.recordsDetailList = recordsDetailList;
        this.homeActivityListner=listner;
        forDayRecordList=new ArrayList<>();

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bp_cardview,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        BPDBManager.getInstance().openDatabase();
        Logger.log(Level.INFO,BPRecyclerViewAdapter.this.getClass().getSimpleName(),"recordsDetailList List size="+recordsDetailList.size());



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


        Logger.log(Level.DEBUG,TAG," sysPressure="+recordsDetailList.get(position).getSysPressure());
        Logger.log(Level.DEBUG,TAG," diaPressure="+recordsDetailList.get(position).getDiabolicPressure());
        Logger.log(Level.DEBUG,TAG," pulPerMin="+recordsDetailList.get(position).getPulsePerMin());
        Logger.log(Level.DEBUG,TAG," testTime="+recordsDetailList.get(position).getComments());

        Logger.log(Level.DEBUG,TAG," testTime="+recordsDetailList.get(position).getTypeBP());



        holder.sysPressure.setText(String.valueOf(recordsDetailList.get(position).getSysPressure()));
        holder.diaPressure.setText(String.valueOf(recordsDetailList.get(position).getDiabolicPressure()));
        holder.pulsePerMin.setText(String.valueOf(recordsDetailList.get(position).getPulsePerMin()));
        holder.bpResult.setText(String.valueOf(recordsDetailList.get(position).getTypeBP()));
        holder.comment.setText(recordsDetailList.get(position).getComments());

        /*holder.sysPressure.setText(""+recordsDetailList.get(position).getSysPressure());
        holder.diaPressure.setText(""+recordsDetailList.get(position).getDiabolicPressure());
        holder.pulsePerMin.setText(""+recordsDetailList.get(position).getPulsePerMin());*/
        holder.testingTime.setText(recordsDetailList.get(position).getMeasurementTime());


        try {
            validateTypeBP((int) recordsDetailList.get(position).getSysPressure());
            if(typeBP.equalsIgnoreCase(Constants.DESIRABLE))
            {
                holder.bpIndicator.setInnerColor(Color.CYAN);
            }else if(typeBP.equalsIgnoreCase(Constants.NORMAL))
            {
                holder.bpIndicator.setInnerColor(Color.GREEN);
            }else if(typeBP.equalsIgnoreCase(Constants.PRE_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.YELLOW);
            }else if(typeBP.equalsIgnoreCase(Constants.HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }else if(typeBP.equalsIgnoreCase(Constants.MILD_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }else if(typeBP.equalsIgnoreCase(Constants.SEVERE_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }

        }catch (Exception e)
        {
         e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return (null!=recordsDetailList)?recordsDetailList.size():0;
    }


    private String typeBP="";
    private String validateTypeBP(int systolic)
    {
        if(systolic <120)
        {
            typeBP=Constants.DESIRABLE;
        }else if(systolic >120 && systolic <129)
        {
            typeBP=Constants.NORMAL;

        }else if(systolic >130 && systolic <139)
        {
            typeBP=Constants.PRE_HYPERTENSION;
        }else if(systolic >140 && systolic <159)
        {
            typeBP=Constants.MILD_HYPERTENSION;
        }else if(systolic >160 && systolic <179)
        {
            typeBP=Constants.HYPERTENSION;
        }else if(systolic >=180)
        {
            typeBP=Constants.SEVERE_HYPERTENSION;
        }

        return  typeBP;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView sysPressure;
        private TextView diaPressure;
        private TextView pulsePerMin;
        private ImageView delete,chart,report;
        private TextView testingTime;
        private TextView bpResult;
        private CustomViewBPCategory bpIndicator;
        private TextView comment;


        Context ctx;

        private CustomViewHolder(View itemView) {
            super(itemView);

            this.sysPressure=(TextView)itemView.findViewById(R.id.txtSys);
            this.diaPressure= (TextView)itemView.findViewById(R.id.txtDI);

            this.pulsePerMin=(TextView)itemView.findViewById(R.id.txtPulse);
            this.testingTime= (TextView)itemView.findViewById(R.id.txtTestingTime);
            this.delete= (ImageView)itemView.findViewById(R.id.iconDelete);
            this.bpResult= (TextView) itemView.findViewById(R.id.bpResult);
            this.bpIndicator= (CustomViewBPCategory) itemView.findViewById(R.id.bpIndicator);
            this.chart= (ImageView) itemView.findViewById(R.id.chart);
            this.report= (ImageView) itemView.findViewById(R.id.report);
            this.comment= (TextView) itemView.findViewById(R.id.comment);

            delete.setOnClickListener(this);
            chart.setOnClickListener(this);
            report.setOnClickListener(this);

            this.ctx=itemView.getContext();



        }

        @Override
        public void onClick(View v) {

            if(v==delete)
            {

                dialog = new Dialog(context);
                if(dialog.getWindow()!=null)
                {
                    dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.custom_dilaog);
                }

                final TextView content= (TextView) dialog.findViewById(R.id.content);
                final TextView header= (TextView) dialog.findViewById(R.id.header);
                final Button save= (Button) dialog.findViewById(R.id.save);
                final Button cancel= (Button) dialog.findViewById(R.id.cancel);

                cancel.setText(context.getResources().getString(R.string.no));
                header.setText(context.getResources().getString(R.string.del_rec));
                content.setText(context.getResources().getString(R.string.delete_rec));

                save.setText(context.getResources().getString(R.string.yes));
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BPDBManager.getInstance().deleteBPRecords(Constants.USER_NAME, recordsDetailList.get(getAdapterPosition()).getMeasurementTime());
                            recordsDetailList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.log(Level.INFO,TAG,recordsDetailList.get(getAdapterPosition()).getComments());
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }else if(v==chart){


               for(BPMeasurementModel m:recordsDetailList){
                if(m.getMeasurementTime().substring(0,9).equals(recordsDetailList.get(getAdapterPosition()).getMeasurementTime().substring(0))){
                    forDayRecordList.add(m);
                }
               }

                Logger.log(Level.INFO,TAG," get same date size="+forDayRecordList.size());

                for(BPMeasurementModel p:forDayRecordList)
                {
                    Logger.log(Level.INFO,TAG," get records with sane date="+p.getSysPressure() + p.getPulsePerMin()
                    +" "+p.getMeasurementTime());
                }
                //homeActivityListner.daysList(forDayRecordList);
                homeActivityListner.invokeRecordList(recordsDetailList,recordsDetailList.get(getAdapterPosition()).getMeasurementTime().substring(0,10));
            }else if(v==report)
            {
                homeActivityListner.invokeFragment(Constants.REPORT,sysPressure.getText().toString()
                ,diaPressure.getText().toString(),pulsePerMin.getText().toString(),testingTime.getText().toString(),comment.getText().toString());
            }

        }
    }



}
