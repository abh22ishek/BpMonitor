package com.bpl.customfonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bpl.logger.Level;
import com.bpl.logger.Logger;

import bpmonitor.bpl.com.bplbpmonitor.R;


public class CustomTextView extends TextView {

    private static final int CENTRALESANS_XBOLD = 1;
    private static final  int CENTRALSANS_LIGHT = 2;
    private static final  int CENTRALSANS_BOOK = 3;
    private static final  int AVANT_GARDE=4;
    private static final  int AVANT_GAMI=5;
    private final static int ARIAL=6;




    private String TAG=CustomTextView.class.getSimpleName();




    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context,attrs);
    }



    private void parseAttributes(Context context, AttributeSet attrs) {
         String TYPEFACE_CENTRALSANS_XBOLD = "fonts/CentraleSans-XBold.otf";
         String TYPEFACE_CENTRALSANS_Light = "fonts/CentraleSans-Light.otf";
         String TYPEFACE_CENTRALSANS_BOOK = "fonts/CentraleSans-Book.otf";
         String TYPEFACE_AVANT_GARDE="fonts/ufonts.com_avantgarde.ttf";
         String TYPEFACE_AVANT_GAMI= "fonts/avangami.ttf";
        String TYPEFACE_ARIAL="fonts/arial.ttf";

        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.CustomTextView_fontname,1);
        Logger.log(Level.INFO,TAG,"styleable typeface value="+typeface);
        switch (typeface) {

            case CENTRALESANS_XBOLD:
                //Typeface  centralSansXBold = Typeface.createFromAsset(context.getAssets(), "fonts/CentraleSans-XBold.otf");
                Typeface centralSansXBold= FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_XBOLD,context);
                setTypeface(centralSansXBold);
                break;

            case CENTRALSANS_LIGHT:
                Typeface centralSansLight=FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_Light,context);
                setTypeface(centralSansLight);
                break;

            case CENTRALSANS_BOOK:
                Typeface centralSansBook =FontTypeFace.getTypeFace(TYPEFACE_CENTRALSANS_BOOK,context);
                setTypeface(centralSansBook);
                break;

            case AVANT_GARDE:
                Typeface avantGarde=FontTypeFace.getTypeFace(TYPEFACE_AVANT_GARDE,context);
                setTypeface(avantGarde);
                break;

            case AVANT_GAMI:
                Typeface avanGami= FontTypeFace.getTypeFace(TYPEFACE_AVANT_GAMI,context);
                setTypeface(avanGami);
                break;
        }

        values.recycle();
    }


}

