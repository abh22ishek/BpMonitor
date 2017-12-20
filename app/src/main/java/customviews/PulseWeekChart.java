package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;
import java.util.List;

import bpmonitor.bpl.com.bplbpmonitor.R;


public class PulseWeekChart extends View {


    int pixels_per_unit=0;


    Paint mPaint;
    Paint mTextPaint;


    int startX=0;
    int startY=0;

    int stopX=0;
    int stopY=0;



    int nStartX=0;
    int nStartY=0;



    float mLeft=0;
    float mTop=0;




    int mX=0;
    float maxY=0;

    List<String> dates;
    List<String> mPoints;

    final String TAG=PulseWeekChart.class.getSimpleName();


    public PulseWeekChart(Context context) {
        super(context);
        init();
    }

    public PulseWeekChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }







    private void init()
    {
        int density=getResources().getDisplayMetrics().densityDpi;
        pixels_per_unit= (int) (density/5f);
        Logger.log(Level.DEBUG,TAG,"pixel per unit="+pixels_per_unit);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(mPaint==null){
            mPaint=new Paint();
        }


        if(mTextPaint==null){
            mTextPaint=new TextPaint();
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);


        stopY=getHeight();


        // draw vertical grids
      /*  for(int i=0;i<getWidth();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,mPaint);
            startX=startX+pixels_per_unit;
            stopX=startX;

        }*/



        // draw horizontal grids


        stopX=getWidth();
        startX=pixels_per_unit;
        startY=0;
        stopY=0;
        for(int i=0;i<getHeight();i+=pixels_per_unit){

            canvas.drawLine(startX,startY,stopX,stopY,mPaint);
            startY=startY+pixels_per_unit;
            stopY=stopY+pixels_per_unit;

        }


        nStartY=getHeight()-pixels_per_unit;
        nStartX=pixels_per_unit;

        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(25f);

        // draw text for horizontal labels
       for(int i=0;i<dates.size();i++)
        {
            canvas.drawText(dates.get(i).substring(0,5), nStartX-10f,nStartY+20f,mTextPaint);
            nStartX=nStartX+2*pixels_per_unit;
        }


        nStartX=10;
        nStartY=0;


        // draw txt for vertical labels
        canvas.drawText("120",nStartX,nStartY+17,mTextPaint);
        nStartY=pixels_per_unit+2;
        canvas.drawText("100",nStartX,nStartY+5f,mTextPaint);
        canvas.drawText("80",nStartX,2*nStartY,mTextPaint);
        canvas.drawText("60",nStartX,3*nStartY,mTextPaint);
        canvas.drawText("40",nStartX,4*nStartY,mTextPaint);
        canvas.drawText("20",nStartX,5*nStartY,mTextPaint);
        canvas.drawText("0",nStartX,6*nStartY,mTextPaint);


        nStartX=0;
        nStartY=pixels_per_unit;


        maxY=120;
        // scaling y axis
        final float height_scale=((float)getHeight()-pixels_per_unit)/maxY;



        // plot the points

        mX=pixels_per_unit;
        for(int i=0;i<mPoints.size();i++){

            mLeft=mX;
            mTop=(maxY-Integer.parseInt(mPoints.get(i)))*height_scale;

            mTextPaint.setStrokeWidth(15f);
            mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
            canvas.drawPoint(mX,mTop,mTextPaint);
            mX=mX+2*pixels_per_unit;

        }


    }

    public List<String> setPlotPoints(List<String> points){

        mPoints=points;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+mPoints.size());
        return mPoints;
    }

    public List<String> setHorizontalLabel(List<String> WeekDates){

        dates=WeekDates;
        Logger.log(Level.DEBUG,TAG,"**Day record size**="+dates.size());
        return dates;
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(14 * pixels_per_unit, 7 * pixels_per_unit);

    }


}
