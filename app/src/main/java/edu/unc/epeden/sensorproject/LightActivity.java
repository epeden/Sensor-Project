package edu.unc.epeden.sensorproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LightActivity extends AppCompatActivity implements SensorEventListener{


    private SensorManager sm;
    private Sensor light_s;
    PlotView pv;
    ImageView ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        pv = (PlotView) findViewById(R.id.plotview_light);

        /* Set up light sensor. */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light_s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

//        sm.registerListener(this, acc_s, 1000000);
        sm.registerListener(this, light_s, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onSensorChanged(SensorEvent event) {

        float f = event.values[0];

//        PlotView pv = (PlotView) findViewById(R.id.plotview_light);

        pv.addPoint(f);
        pv.invalidate();


        double last_mean = pv.getLastMean();
        if (last_mean > 180) {
            ani = (ImageView) findViewById(R.id.animation_light);
            ani.setBackgroundResource(R.drawable.light_bulb_xml);
            ani.setMaxHeight(50);

            ((AnimationDrawable) ani.getBackground()).start();
        } else {
            ani = (ImageView) findViewById(R.id.animation_light);
            ani.setBackgroundResource(R.drawable.bulb_off_xml);
            ani.setMaxHeight(50);

            ((AnimationDrawable) ani.getBackground()).start();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void backButtonPushed(View v) {
        //setContentView(R.layout.activity_main);
        Intent main_intent = new Intent(this,MainActivity.class);
        startActivity(main_intent);
    }

}
