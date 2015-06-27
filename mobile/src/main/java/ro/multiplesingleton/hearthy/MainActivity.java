package ro.multiplesingleton.hearthy;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.LineGraph;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private TextView mTextView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(mTextView!=null){
                mTextView.setText(msg.getData().getCharSequence("message"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView)findViewById(R.id.text);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        DataLayerListenerService.setHandler(handler);
    }

    @Override
    protected void onPause(){
        DataLayerListenerService.setHandler(null);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
