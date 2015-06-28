package ro.multiplesingleton.hearthy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Claudiu on 28.06.2015.
 */
public class PhoneMessenger extends Service {

    private static final String LOG_TAG = "PhoneMessenger";

    private final int CONNECTION_TIME_OUT_MS = 100;

    private GoogleApiClient client;
    private String nodeId;

    @Override
    public void onCreate() {
        super.onCreate();

        this.client = this.getGoogleApiClient();
        this.retrieveDeviceNode();

        Log.i(LOG_TAG, "Phone Messenger registered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.client.disconnect();
        Log.d(LOG_TAG, "Phone messenger unregistered");
    }

    // ehh.. sometimes python multiple inheritance would be nice..
    class ServiceBinder extends Binder {
        public PhoneMessenger getInstance() {
            return PhoneMessenger.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    private GoogleApiClient getGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                                  .addApi(Wearable.API)
                                  .build();
    }

    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _retrieveDeviceNode();
            }
        }).start();
    }

    private void _retrieveDeviceNode() {
        client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        NodeApi.GetConnectedNodesResult result =
                Wearable.NodeApi.getConnectedNodes(client).await();
        List<Node> nodes = result.getNodes();
        if (nodes.size() > 0) {
            this.nodeId = nodes.get(0).getId();
        }
    }

    public void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                _sendMessage(message);
            }
        }).start();
    }

    private void _sendMessage(String message) {
        Log.d(LOG_TAG, "Message to be sent: " + message);
        if (nodeId == null)
            return;

        client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        Wearable.MessageApi.sendMessage(client, nodeId, message, null);
    }

    public void sendMessage(String sensorType, int sensorValue) {
        long millis = System.currentTimeMillis();
        StringBuilder str = new StringBuilder();
        str.append(millis).append("#")
           .append(sensorType).append("#")
           .append(sensorValue);

        this.sendMessage(str.toString());
    }

    public boolean isUsable() {
        return this.client != null && this.nodeId != null;
    }
}
