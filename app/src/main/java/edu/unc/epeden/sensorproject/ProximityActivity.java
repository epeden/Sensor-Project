package edu.unc.epeden.sensorproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sm;
    private Sensor prox_s;
    PlotView pv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);


          /* Set up proximity sensor. */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        prox_s = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        sm.registerListener(this, prox_s, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onSensorChanged(SensorEvent event) {
        float f = event.values[0];

        PlotView pv = (PlotView) findViewById(R.id.plotview_prox);
        pv.addPoint(f);
        pv.invalidate();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void backButtonPushed(View v) {
        //setContentView(R.layout.activity_main);
        Intent main_intent = new Intent(this,MainActivity.class);
        startActivity(main_intent);
    }

}
