package com.example.smartshare;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        super.run();
                        sleep(500); //delay
                    } catch (Exception e) {

                    } finally {

                        Intent i = new Intent(Loading.this,
                                MainActivity.class);
                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(Loading.this,android.R.anim.fade_in,android.R.anim.fade_out).toBundle();
                        startActivity(i, bundle);

                        finish();
                    }
                }
            };
            thread.start();
        }
    }
