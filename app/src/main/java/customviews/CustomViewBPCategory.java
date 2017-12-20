package customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewBPCategory extends View{


    Paint mPaint;
    int innerColor=0;


    public CustomViewBPCategory(Context context) {
        super(context);
    }

    public CustomViewBPCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewBPCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint=new Paint();
        mPaint.setColor(innerColor);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(getWidth()/2,getHeight()/2,10,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth()/2,getHeight()/2,20,mPaint);


    }


    public void setInnerColor(int color)
    {
        innerColor=color;
    }
}
