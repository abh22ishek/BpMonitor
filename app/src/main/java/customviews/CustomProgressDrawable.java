package customviews;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.util.Utility;

import bpmonitor.bpl.com.bplbpmonitor.R;



public class CustomProgressDrawable extends Drawable {


    private Paint mPaint = new Paint();

    private float density;
    private Context context;
    int nRect = 15;

    public CustomProgressDrawable(float density,Context context) {
        this.density = density;
        this.context=context;
    }



    @Override
    public void draw(@NonNull Canvas canvas) {
        // TODO Auto-generated method stub
        int level = getLevel();

        mPaint.setAntiAlias(true);

        Rect b = getBounds();

        float height = b.height();
        float width=b.width();
        if(density== DisplayMetrics.DENSITY_MEDIUM) {

            draw_circle(15, 15, 10,nRect, level, canvas, width, height, 20, 30, mPaint);


        }else if(density==DisplayMetrics.DENSITY_XHIGH){
            nRect =9;
            draw_circle(30,30,20,nRect,level,canvas,width,height,35,50,mPaint);
        }
        else if(density==300)
        {
            nRect =12;
            draw_circle(30,30,20,nRect,level,canvas,width,height,35,48,mPaint);
        }

        else if(density==DisplayMetrics.DENSITY_XXXHIGH)
        {
            nRect = 12;
            draw_circle(50,50,40,nRect,level,canvas,width,height,60,80,mPaint);
        }
        else if(density==DisplayMetrics.DENSITY_XXHIGH) {
            nRect = 12;
            draw_circle(40,40,30,nRect,level,canvas,width,height,50,70,mPaint);
        }else if(density>200  && density<280)
        {
            nRect =8;
            draw_circle(30,30,15,nRect,level,canvas,width,height,35,35,mPaint);
        }
        else {

             nRect = 10;
            draw_circle(40, 40, 30, nRect, level, canvas, width, height, 50, 70, mPaint);


        }

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }



    private void draw_circle(float x,float y,float radius,int nRect,int level,Canvas canvas,float width,
                             float height,float increment_x,float increment_y,Paint mPaint)
    {


        for (int i =0; i<nRect; i++) {

            if((i+1)*10000/nRect>level)
            {
                mPaint.setColor(Color.GRAY);
            }else{
                if((i > 2) && (i < 6))
                    mPaint.setColor(Utility.getColorWrapper(context,R.color.indigo));
                else if ((i > 4) && (i < 8))
                    mPaint.setColor(Utility.getColorWrapper(context,R.color.indigo));

                else if(i>8 &&i<nRect)
                    mPaint.setColor(Utility.getColorWrapper(context,R.color.indigo));
                else //if ((i > 6) && (i < 11))
                    mPaint.setColor(Utility.getColorWrapper(context,R.color.indigo));

            }


            canvas.drawCircle(width/2, height-y,radius, mPaint);

            x=x+increment_x;
            y=y+increment_y;
        }

    }
}
