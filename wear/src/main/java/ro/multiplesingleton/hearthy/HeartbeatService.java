package ro.multiplesingleton.hearthy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by robert on 27/06/2015.
 */
public class HeartbeatService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private int currentValue = 0;
    private static final String LOG_TAG = "MyHeart";
    private IBinder binder = new HeartbeatServiceBinder();
    private OnChangeListener onChangeListener;
    private GoogleApiClient mGoogleApiClient;

    public interface OnChangeListener {

        void onValueChanged(String value);
    }

    public class HeartbeatServiceBinder extends Binder {
        public void setChangeListener(OnChangeListener listener) {
            onChangeListener = listener;
            String message = currentValue + "--" + Calendar.getInstance().getTime().toString();
            listener.onValueChanged(message);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        boolean res = mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(LOG_TAG, "sensor registered:" + (res ? "yes" : "no"));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Log.d(LOG_TAG, "sensor unregistered");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE && sensorEvent.values.length > 0) {
            int newValue = Math.round(sensorEvent.values[0]);
            int toSend = 0;
            if (currentValue == newValue) {
                Random rand = new Random();
                int i1 = rand.nextInt(100 - 65) + 65;
                toSend = i1;
            }
            currentValue = newValue;
            if (onChangeListener != null) {
                if (toSend != 0) {
                    newValue = toSend;
                }
                Log.d(LOG_TAG, "sending new value to listener:" + newValue);
                String message = newValue + "--" + Calendar.getInstance().getTime().toString();
                onChangeListener.onValueChanged(message);
                sendMessageToHandheld(message);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void sendMessageToHandheld(final String message) {
        if (mGoogleApiClient == null) {
            return;
        }

        Log.d(LOG_TAG, "sending message to handheld: " + message);
        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                final List<Node> nodes = result.getNodes();

                if (nodes != null) {
                    for (int i = 0; i < nodes.size(); i++) {
                        final Node node = nodes.get(i);
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), message, null);
                    }
                }
            }
        });
    }
}
