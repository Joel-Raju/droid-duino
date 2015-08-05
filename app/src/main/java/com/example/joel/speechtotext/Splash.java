package com.example.joel.speechtotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Joel on 03-Apr-15.
 */
public class Splash extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        Thread timer= new Thread()
        {
            public void run()
            {
                try{

                    sleep(3000);
                }

                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                finally{
                    Intent open_menu=new Intent(Splash.this,MainActivity.class);
                    startActivity(open_menu);
                }
            }
        };

        timer.start();
    }

    protected void onPause()
    {
        super.onPause();
        finish();
    }

}
