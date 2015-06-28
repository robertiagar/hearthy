package ro.multiplesingleton.hearthy.SQL;

import com.orm.SugarRecord;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by Win81 on 6/27/2015.
 */
public class HeartRateData extends SugarRecord<HeartRateData>{

    public HeartRateData(){

    }

    public HeartRateData(int value, Date date){
        this.value = value;
        this.date = date;
    }

    public int value;
    public Date date;

}
