package com.flesh.washingtonhearld.app.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.flesh.washingtonhearld.app.R;


public class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Custom animation for entering Activity that is The same through out he app ( as long as BaseActivity is extended. )
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
        //Custom animation for exiting Activity that is The same through out he app ( as long as BaseActivity is extended. )
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
