package edu.unc.epeden.sensorproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor acc_sensor;
    private Sensor light_sensor;
    private Sensor prox_sensor;
    private Sensor pres_sensor;
    private List<Sensor> sl;

    Intent acc_intent;
    Intent light_intent;
    Intent prox_intent;
    Intent pres_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Strings for editing sensor's status & info in TextView. */
        String acc_tv_status = "";
        String acc_tv_info = "";
        String light_tv_status = "";
        String light_tv_info = "";
        String prox_tv_status = "";
        String prox_tv_info = "";
        String pres_tv_status = "";
        String pres_tv_info = "";


        /* Set Up sensor manager. */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sl = sm.getSensorList(Sensor.TYPE_ALL);
        acc_sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light_sensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        prox_sensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        pres_sensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);




        if (acc_sensor != null) {
            acc_tv_status = "Accelerometer is present.";
            acc_tv_info = "Range: "+acc_sensor.getMaximumRange()+", Resolution: "+acc_sensor.getResolution()+", Delay: "+acc_sensor.getMinDelay()+"";
            sm.registerListener(this, acc_sensor, 1000000);
        } else {
            acc_tv_status = "Accelerometer NOT present.";
            acc_tv_info = "No info available.";
        }
        if (light_sensor != null) {
            light_tv_status = "Light Sensor is present.";
            light_tv_info = "Range: "+light_sensor.getMaximumRange()+", Resolution: "+light_sensor.getResolution()+", Delay: "+light_sensor.getMinDelay()+"";
            sm.registerListener(this, light_sensor, 1000000);
        } else {
            light_tv_status = "Light Sensor NOT present.";
            light_tv_info = "No info available.";
        }
        if (prox_sensor != null) {
            prox_tv_status = "Proximity is present.";
            prox_tv_info = "Range: "+prox_sensor.getMaximumRange()+" Resolution: "+prox_sensor.getResolution()+" Delay: "+prox_sensor.getMinDelay()+"";
            sm.registerListener(this, prox_sensor, 1000000);
        } else {
            prox_tv_status = "Proximity NOT present.";
            prox_tv_info = "No info available.";
        }
        if (pres_sensor != null) {
            pres_tv_status = "Pressure Sensor is present.";
            pres_tv_info = "Range: "+pres_sensor.getMaximumRange()+" Resolution: "+pres_sensor.getResolution()+" Delay: "+pres_sensor.getMinDelay()+"";
            sm.registerListener(this, pres_sensor, 1000000);
        } else {
            pres_tv_status = "Pressure Sensor NOT present.";
            pres_tv_info = "No info available.";
        }



        /* Set up TextViews for status & info on each sensor. */
        TextView acc_textview = (TextView) findViewById(R.id.acc_text);
        TextView light_textview = (TextView) findViewById(R.id.light_text);
        TextView prox_textview = (TextView) findViewById(R.id.prox_text);
        TextView pres_textview = (TextView) findViewById(R.id.pres_text);

        acc_textview.setText("Status: "+acc_tv_status+"\nInfo: "+acc_tv_info+"");
        light_textview.setText("Status: "+light_tv_status+"\nInfo: "+light_tv_info+"");
        prox_textview.setText("Status: "+prox_tv_status+"\nInfo: "+prox_tv_info+"");
        pres_textview.setText("Status: "+pres_tv_status+"\nInfo: "+pres_tv_info+"");





        /* Set button listeners. */
        Button acc_button = (Button) findViewById(R.id.acc_button);
        Button light_button = (Button) findViewById(R.id.light_button);
        Button prox_button = (Button) findViewById(R.id.prox_button);
        Button pres_button = (Button) findViewById(R.id.pres_button);

        acc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accButtonPushed(v);
            }
        });
        light_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightButtonPushed(v);
            }
        });
        prox_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proxButtonPushed(v);

            }
        });
        pres_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presButtonPushed(v);

            }
        });

    }

    public void accButtonPushed(View v) {
        acc_intent = new Intent (this,AcceleromaterActivity.class);
        startActivity(acc_intent);
    }

    public void lightButtonPushed(View v) {
        light_intent = new Intent (this,LightActivity.class);
        startActivity(light_intent);
    }

    public void proxButtonPushed(View v) {
        prox_intent = new Intent (this,ProximityActivity.class);
        startActivity(prox_intent);
    }
    public void presButtonPushed(View v) {
        pres_intent = new Intent (this,PressureActivity.class);
        startActivity(pres_intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
