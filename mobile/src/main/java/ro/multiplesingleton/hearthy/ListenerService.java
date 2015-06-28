package ro.multiplesingleton.hearthy;

import android.os.Binder;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Claudiu on 28.06.2015.
 */

public class ListenerService extends WearableListenerService {

    private OnWatchMessageListener messageListener;
    private String nodeId;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        nodeId = messageEvent.getSourceNodeId();
        showToast(messageEvent.getPath());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // ehh.. sometimes python multiple inheritance would be nice..
    class ServiceBinder extends Binder {
        public void setMessageListener(OnWatchMessageListener listener) {
            messageListener = listener;
        }
    }

}
