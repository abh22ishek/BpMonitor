package bpmonitor.bpl.com.bplbpmonitor;

import android.app.Activity;
import android.os.Bundle;


public abstract class AppBaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){

        }
    }
}
