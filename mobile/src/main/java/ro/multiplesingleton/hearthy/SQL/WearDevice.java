package ro.multiplesingleton.hearthy.SQL;

import com.orm.SugarRecord;

import java.util.UUID;

/**
 * Created by Win81 on 6/27/2015.
 */
public class WearDevice extends SugarRecord<WearDevice> {

    public WearDevice(){

    }

    private int deviceID;
    private UUID deviceGUID;

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        if (this.deviceID == 0) this.deviceID = deviceID;

    }

    public UUID getDeviceGUID() {
        return deviceGUID;
    }

    public void setDeviceGUID(UUID deviceGUID) {
        if (this.deviceGUID == null) this.deviceGUID = deviceGUID;

    }


}
