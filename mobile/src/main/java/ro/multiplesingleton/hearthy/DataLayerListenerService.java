package ro.multiplesingleton.hearthy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Calendar;

/**
 * Created by robert on 27/06/2015.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static final String LOG_TAG = "WearableListener";
    private static final String HEARTBEAT = "HEARTBEAT";

    private static Handler handler;
    private static String currentValue = "0";

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        DataLayerListenerService.handler = handler;

        if (handler != null) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            String msg = currentValue + "--" + Calendar.getInstance().getTime().toString();
            bundle.putCharSequence("message", msg);
            message.setData(bundle);
            handler.sendMessage(message);
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

        currentValue =messageEvent.getPath();
        if(handler!=null){
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putCharSequence("message",currentValue);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }
}
