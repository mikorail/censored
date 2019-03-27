package com.example.tiltapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //sensor
    private SensorManager sensorManager;
    private Sensor sensorAccel;
    private Sensor sensorMagnet;

    //data dari sensor
    private float[] accelData=new float[3];
    private float[] magnetData=new float[3];

    //textview
    private TextView tvLabelAzymuth;
    private TextView tvLabelPitch;
    private TextView tvLabelRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textview
        tvLabelAzymuth=(TextView)findViewById(R.id.label_azymuth);
        tvLabelPitch=(TextView)findViewById(R.id.label_pitch);
        tvLabelRoll=(TextView)findViewById(R.id.label_roll);

        //sensor
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorAccel=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnet=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sensorAccel!=null){
            sensorManager.registerListener(this,sensorAccel,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorMagnet!=null){
            sensorManager.registerListener(this,sensorMagnet,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType=sensorEvent.sensor.getType();
        switch (sensorType){
            case Sensor.TYPE_ACCELEROMETER:
                accelData=sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetData=sensorEvent.values.clone();
                break;
        }

        float[] rotationMatrix=new float[9];
        boolean rotationOK=SensorManager.getRotationMatrix(rotationMatrix,
                null,accelData,magnetData);
        float [] orientationVal=new float[3];
        if(rotationOK){
            SensorManager.getOrientation(rotationMatrix,
                    orientationVal);
        }
        float azymuth=orientationVal[0];
        float pitch=orientationVal[1];
        float roll=orientationVal[2];

        //first try

//        tvLabelAzymuth.setText("Azymuth (z) : "+azymuth);
//        tvLabelPitch.setText("Pitch (y) : "+pitch);
//        tvLabelRoll.setText("Roll (x) : "+roll);

        //second try
        tvLabelAzymuth.setText("Azymuth (z) : "+String.format("%.2f",azymuth));
        tvLabelPitch.setText("Pitch (y) : "+String.format("%.2f",pitch));
        tvLabelRoll.setText("Roll (x) : "+String.format("%.2f",roll));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
