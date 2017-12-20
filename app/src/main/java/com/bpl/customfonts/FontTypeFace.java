package com.bpl.customfonts;


import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

import static bpmonitor.bpl.com.bplbpmonitor.R.attr.fontname;

public class FontTypeFace {


    private static HashMap<String,Typeface> fontCache=new HashMap<>();

     static Typeface getTypeFace(String fontName,Context context)
     {
        // fontName is path
        Typeface typeface=fontCache.get(fontname);

        if(typeface==null)
        {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(fontName,typeface);
        }
        return typeface;
    }
}
