package ro.multiplesingleton.hearthy;

import android.hardware.Sensor;

/**
 * Created by Claudiu on 27.06.2015.
 */

public class HeartBeatMonitor extends BaseMonitor {

    private static final String LOG_TAG = "HeartSensorMonitor";

    protected String sensorName = "HeartBeat";
    protected int sensorType = Sensor.TYPE_HEART_RATE;
}
