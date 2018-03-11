package com.metro.myapplication4;

import android.content.Intent;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.GestureDetector;
import android.provider.Settings;


public class Main2Activity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    public static int speed = 10;
    public static int accel = 0;
    //public static boolean accel = true;
    public static int counter = 0;
    public static int scrollCount = 0;
    public static float scrollYDist = 0;
    public static String DEBUG_TAG = "BILL";
    private GestureDetector mDetector;
    private Context mContext;
    private int brightness = 0;
    public int values[] = {20,35,35,20,20,10,10};
    public int values2[] =  {1,1,0,-1,0,-1,0};
    public int valuesIndex = 0;

    public void setScreenBrightness(int brightnessValue){
        if(brightnessValue >= 0 && brightnessValue <= 255){
            Settings.System.putInt(
                    mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
        }
    }

    protected int getScreenBrightness(){
        int brightnessValue = Settings.System.getInt(
                mContext.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,0);
        return brightnessValue;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }
    @Override
    public void onLongPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        scrollCount++;
        if(scrollCount == 1) {

            if(distanceY > 0) {
                Log.i(DEBUG_TAG, "brighter: ");
                brightness += 10;
                if(brightness > 255) brightness = 255;
                setScreenBrightness(brightness);
            } else {
                Log.i(DEBUG_TAG, "dimmer: " );
                brightness -= 10;
                if(brightness < 0) brightness = 0;
                setScreenBrightness(brightness);
            }
            scrollCount = 0;

        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        mDetector = new GestureDetector(this,this);
        mDetector.setOnDoubleTapListener(this);

        mContext = getApplicationContext();
        brightness = getScreenBrightness();                             // initial brightness
        //TextView helloTextView = (TextView) findViewById(R.id.textView);
        //helloTextView.setText(String.valueOf(values[valuesIndex]));

        Thread t = new Thread() {

            @Override
            public void run() {

            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    //System.out.println("got it");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(++counter % 10 == 0) {
                                String advised = "";
                                //speed -= 5;
                                //if (speed < -10) speed = 10;
                                speed = values[valuesIndex];
                                accel = values2[valuesIndex];
                                if(++valuesIndex >= values.length) valuesIndex = 0;

                                advised = String.valueOf(speed);

                                TextView speedTextView = (TextView) findViewById(R.id.textView);
                                ImageView arrowImageView = (ImageView) findViewById(R.id.imageViewArrow);
                                //speedTextView.setText(advised);
                                if(accel == -2) {
                                    speedTextView.setText("---");
                                    arrowImageView.setVisibility(View.INVISIBLE);
                                } else if(accel == -1) {
                                    speedTextView.setText(advised);
                                    arrowImageView.setImageResource(R.drawable.red_down2);
                                    arrowImageView.setVisibility(View.VISIBLE);
                                } else if (accel == 0) {
                                    speedTextView.setText(advised);
                                    speedTextView.setTextColor(getResources().getColor(R.color.colorBlack));
                                    arrowImageView.setVisibility(View.INVISIBLE);
                                } else {
                                    speedTextView.setText(advised);
                                    arrowImageView.setImageResource(R.drawable.green_up2);
                                    arrowImageView.setVisibility(View.VISIBLE);
                                }

                                //TextView connTextView = (TextView) findViewById(R.id.textView2);
                                //if (BatteryManager.BATTERY_PLUGGED_USB == 2) connTextView.setText("Conn");
                                //else connTextView.setText("DIS");
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
            }
        };

         t.start();




    }
}
