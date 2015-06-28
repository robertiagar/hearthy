package ro.multiplesingleton.hearthy;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by robert on 27/06/2015.
 */
public class MainActivity extends WearableActivity implements HeartbeatService.OnChangeListener {
    private static final String LOG_TAG = "MyHeart";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

                bindService(new Intent(MainActivity.this, HeartbeatService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder binder) {
                        Log.d(LOG_TAG, "connected to service");

                        ((HeartbeatService.HeartbeatServiceBinder) binder).setChangeListener(MainActivity.this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Service.BIND_AUTO_CREATE);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
    }

    @Override
    public void onValueChanged(String value) {
        mTextView.setText(value);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this).extend(new Notification.WearableExtender()
                .setDisplayIntent(notificationPendingIntent)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_MEDIUM)).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("notification",0,notification);
    }
}
