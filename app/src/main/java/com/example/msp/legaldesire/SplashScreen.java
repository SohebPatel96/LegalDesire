package com.example.msp.legaldesire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread timer = new Thread(){

            public void run(){
                try{
                    sleep(2000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();

                }
                finally{
                    Intent i = new Intent(SplashScreen.this,Home.class);
                    startActivity(i);
                    finish();
                }

            }
        };timer.start();
    }
}
