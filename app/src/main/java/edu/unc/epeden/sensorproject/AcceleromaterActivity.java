package edu.unc.epeden.sensorproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by epeden on 2/20/16.
 */
public class AcceleromaterActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor acc_s;
    PlotView pv;
    ImageView ani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        /* Set up accelerometer sensor. */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc_s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        sm.registerListener(this, acc_s, 1000000);
        sm.registerListener(this, acc_s, SensorManager.SENSOR_DELAY_NORMAL);


    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float f = (x*x) + (y*y) + (z*z);

        f = (float) Math.sqrt(f);

        PlotView pv = (PlotView) findViewById(R.id.plotview_acc);
        pv.addPoint(f);
        pv.invalidate();

        double last_mean = pv.getLastMean();
        if (last_mean > 12) {
            ani = (ImageView) findViewById(R.id.animation_acc);
            ani.setBackgroundResource(R.drawable.red_light_xml);
            ani.setMaxHeight(50);

            ((AnimationDrawable) ani.getBackground()).start();
        } else if (last_mean > 9.85) {
            ani = (ImageView) findViewById(R.id.animation_acc);
            ani.setBackgroundResource(R.drawable.yellow_light_xml);
            ani.setMaxHeight(50);

            ((AnimationDrawable) ani.getBackground()).start();
        } else {
            ani = (ImageView) findViewById(R.id.animation_acc);
            ani.setBackgroundResource(R.drawable.green_light_xml);
            ani.setMaxHeight(50);

            ((AnimationDrawable) ani.getBackground()).start();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void backButtonPushed(View v) {
        Intent main_intent = new Intent(this,MainActivity.class);
        startActivity(main_intent);
    }
}
