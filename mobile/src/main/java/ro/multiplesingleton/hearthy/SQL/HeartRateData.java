package ro.multiplesingleton.hearthy.SQL;

import com.orm.SugarRecord;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Win81 on 6/27/2015.
 */
public class HeartRateData extends SugarRecord<HeartRateData>{

    private int heartRateID;
    private int rateValue;
    private Date date;
    private Timestamp timestamp;

    //foreign key for WearDevice table
    private WearDevice deviceID;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        if (this.date == null) this.date = date;
    }

    public WearDevice getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(WearDevice deviceID) {
        if (this.deviceID == null) this.deviceID = deviceID;
    }

    public int getHeartRateID() {
        return heartRateID;
    }

    public void setHeartRateID(int heartRateID) {
       this.heartRateID = heartRateID;

    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        if  (this.rateValue == 0) this.rateValue = rateValue;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        if (this.timestamp == null )this.timestamp = timestamp;
    }


}
