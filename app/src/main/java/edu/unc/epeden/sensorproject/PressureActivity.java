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

public class PressureActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sm;
    private Sensor pres_s;
    PlotView pv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressure);


        /* Set up perssure sensor. */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pres_s = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);

        sm.registerListener(this, pres_s, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onSensorChanged(SensorEvent event) {
        float f = event.values[0];

        PlotView pv = (PlotView) findViewById(R.id.plotview_pres);
        pv.addPoint(f);
        pv.invalidate();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void backButtonPushed(View v) {
        Intent main_intent = new Intent(this,MainActivity.class);
        startActivity(main_intent);
    }

}
