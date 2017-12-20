package customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.util.Utility;

import bpmonitor.bpl.com.bplbpmonitor.R;


public class CustomViewCircle extends View {

    private Paint mPaint;
    private Path mPath;

    private Context context;

    public CustomViewCircle(Context context) {
        super(context);
        init();
    }

    public CustomViewCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        RectF rectF=new RectF(0,0,getWidth(),getHeight());
        mPath.addArc(rectF,0f,360f);

        mPaint.setAntiAlias(true);
        mPaint.setColor(Utility.getColorWrapper(context,R.color.indigo));



        //canvas.drawCircle(getWidth()/2,getHeight()/2,200,mPaint);

    }


    private void init()
    {
        if(mPaint==null)
        {
            mPaint=new Paint();
        }


        if(mPath==null)
        {
            mPath=new Path();
        }
    }
}
