package com.util;


import android.content.Context;
import android.os.Build;

public class Utility {

    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //no inspection deprecation
            return context.getResources().getColor(id);
        }
    }
}
