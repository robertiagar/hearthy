package ro.multiplesingleton.hearthy;

import android.hardware.Sensor;

/**
 * Created by Claudiu on 28.06.2015.
 */
public class StepMonitor extends BaseMonitor {

    private static final String LOG_TAG = "StepSensorMonitor";

    protected String sensorName = "StepCounter";
    protected int sensorType = Sensor.TYPE_STEP_COUNTER;
}
