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


class MainServiceConnection implements ServiceConnection {
    private OnChangeListener listener;

    public MainServiceConnection(OnChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        // set our change listener to get change events
        ((HeartBeatMonitor.ServiceBinder) binder).setChangeListener(this.listener);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}

public class MainActivity extends Activity implements OnChangeListener {

    private static final String LOG_TAG = "MainActivity";

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

                Log.i(LOG_TAG, "Binding service...");
                bindService(new Intent(MainActivity.this, HeartBeatMonitor.class),
                            new MainServiceConnection(MainActivity.this), Service.BIND_AUTO_CREATE);

                mTextView.setText("binded");
            }

        });
    }

    @Override
    public void onValueChanged(int newValue) {
        String message = "New heartbeat: " + newValue;
        mTextView.setText(message);
    }
}
