package ro.multiplesingleton.hearthy;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;
import android.util.Log;


public class MainActivity extends Activity implements OnChangeListener, ServiceConnection {

    private static final String LOG_TAG = "MainActivity";

    private PhoneMessenger phoneMessenger;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG_TAG, "Starting...");
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

                mTextView.setText("Updated Hello world!");

                Log.i(LOG_TAG, "Binding  PhoneMessenger service...");
                Intent intent = new Intent(MainActivity.this, PhoneMessenger.class);
                bindService(intent, MainActivity.this, Service.BIND_AUTO_CREATE);
                startService(intent);

                if (phoneMessenger != null) {
                    Log.i(LOG_TAG, "Sending through Phone messenger..");
                    phoneMessenger.sendMessage("Your watch says hello!");
                }

                Log.i(LOG_TAG, "Binding  HeartBeat service...");
                intent = new Intent(MainActivity.this, HeartBeatMonitor.class);
                bindService(intent, MainActivity.this, Service.BIND_AUTO_CREATE);
                startService(intent);

                Log.i(LOG_TAG, "Binding  Step service...");
                intent = new Intent(MainActivity.this, StepMonitor.class);
                bindService(intent, MainActivity.this, Service.BIND_AUTO_CREATE);
                startService(intent);

            }

        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        // set our change listener to get change events
        String className = componentName.getShortClassName();
        if (className == "HeartBeatMonitor" || className == "StepMonitor")
            ((HeartBeatMonitor.ServiceBinder) binder).setChangeListener(this);
        else if (className == "PhoneMessenger")
            this.phoneMessenger = ((PhoneMessenger.ServiceBinder) binder).getInstance();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onValueChanged(String sensorName, int newValue) {
        String message = "New " + sensorName + ": " + newValue;
        mTextView.setText(message);

        if (this.phoneMessenger != null) {
            this.phoneMessenger.sendMessage(sensorName, newValue);
        }
    }
}
