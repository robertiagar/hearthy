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
 * Created by Claudiu on 28.06.2015.
 */
public abstract class BaseMonitor  extends Service implements SensorEventListener {

    protected static final String LOG_TAG = "SensorMonitor";

    private SensorManager mSensorManager;
    private int currentValue = 0;
    private IBinder binder = new ServiceBinder();
    private OnChangeListener onChangeListener;

    protected String sensorName;
    protected int sensorType;

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
        if (! isForThisSensor(sensorEvent.sensor))
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
        if (isForThisSensor(sensor))
            Log.d(LOG_TAG, this.sensorName + " Accuracy changed: " + i);
    }

    private boolean isForThisSensor(Sensor sensor) {
        return sensor.getType() == this.sensorType;
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
        Sensor mSensor = mSensorManager.getDefaultSensor(this.sensorType);

        boolean res = mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_UI);
        Log.i(LOG_TAG, this.sensorName + " sensor registered: " + (res ? "yes" : "no"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, this.sensorName + " sensor unregistered");
    }
}
