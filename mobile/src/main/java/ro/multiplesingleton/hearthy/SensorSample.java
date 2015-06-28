package ro.multiplesingleton.hearthy;

import java.util.Date;

/**
 * Created by Claudiu on 28.06.2015.
 */
public class SensorSample {
    private Date timestamp;
    private String sensorType;
    private int value;

    public SensorSample(Date timestamp, String sensorType, int value) {
        this.timestamp = timestamp;
        this.sensorType = sensorType;
        this.value = value;
    }

    public static SensorSample toSample(String timestamp, String sensorType, String value) {
        long millis = Long.parseLong(timestamp);
        return new SensorSample(
                new Date(millis), sensorType, Integer.parseInt(value));
    }
}
