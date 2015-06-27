package ro.multiplesingleton.hearthy;

import android.os.Handler;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by robert on 27/06/2015.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static final String LOG_TAG = "WearableListener";
    private static final String HEARTBEAT = "HEARTBEAT";

    private static Handler handler;
    private static int currentValue = 0;

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        DataLayerListenerService.handler = handler;

        if (handler != null) {
            handler.sendEmptyMessage(currentValue);
        }
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d(LOG_TAG, "Connected peer name & ID:" + name + "|" + id);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent){
        super.onMessageReceived(messageEvent);

        Log.d(LOG_TAG,"received a message form wear:"+messageEvent.getPath());

        currentValue = Integer.parseInt(messageEvent.getPath());
        if(handler!=null){
            handler.sendEmptyMessage(currentValue);
        }
    }
}
