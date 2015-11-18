package com.example.lordzsolt.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Lord Zsolt on 11/18/2015.
 */
public class AccelerometerListener implements SensorEventListener
{
    private static float ACCELEROMETER_TILT_TRESHOLD = 2.0f;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    public AccelerometerListener(Context context) {
        senSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void resume() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void pause() {
        senSensorManager.unregisterListener(this);
    }

    public void onTiltLeft(){}
    public void onTiltRight(){};
    public void onTiltUp(){};
    public void onTiltDown(){};

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            //X positive big = left
            //X negative big = right
            //Y positive big = down
            //Y negative big = up
            if (Math.abs(x) > Math.abs(y)) {
                if (Math.abs(x) > ACCELEROMETER_TILT_TRESHOLD) {
                    if (x < 0) {
                        this.onTiltRight();
                    }
                    else {
                        this.onTiltLeft();
                    }
                }
            }
            else {
                if (Math.abs(y) > ACCELEROMETER_TILT_TRESHOLD) {
                    if (y < 0) {
                        this.onTiltUp();
                    }
                    else {
                        this.onTiltDown();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
