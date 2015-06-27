package ro.multiplesingleton.hearthy;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Claudiu on 27.06.2015.
 */

public class HeartBeatMonitor extends Service implements SensorEventListener {

    private static final String LOG_TAG = "HeartBeatMonitor";

    private SensorManager mSensorManager;
    private int currentValue = 0;
    private IBinder binder = new ServiceBinder();
    private OnChangeListener onChangeListener;

    // ehh.. sometimes python multiple inheritance would be nice..
    class ServiceBinder extends Binder {
        public void setChangeListener(OnChangeListener listener) {
            Log.i(LOG_TAG, "Binding listener...");
            onChangeListener = listener;
            // return currently known value
            listener.onValueChanged(currentValue);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // care only for heartbeats.
        if (! isHeartBeatSensor(sensorEvent.sensor))
            return;

        if (sensorEvent.values.length == 0)
            return;

        int newValue = Math.round(sensorEvent.values[0]);
        if (currentValue == newValue || newValue == 0)
            // nothing to do.
            return;

        currentValue = newValue;
        if (this.onChangeListener != null) {
            Log.i(LOG_TAG, "sending new value to listener: " + newValue);
            onChangeListener.onValueChanged(newValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        if (isHeartBeatSensor(sensor))
            Log.d(LOG_TAG, "Heartbeat Accuracy changed: " + i);
    }

    private boolean isHeartBeatSensor(Sensor sensor) {
        return sensor.getType() == Sensor.TYPE_HEART_RATE;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // register us as a sensor listener
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        boolean res = mSensorManager.registerListener(this, mHeartRateSensor,
                                                      SensorManager.SENSOR_DELAY_UI);
        Log.i(LOG_TAG, "Heartbeat sensor registered: " + (res ? "yes" : "no"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, " sensor unregistered");
    }
}
