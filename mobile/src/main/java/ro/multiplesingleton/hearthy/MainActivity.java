package ro.multiplesingleton.hearthy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {

    private TextView mTextView;
    private LineChart mLineGraph;
    private LineDataSet mLine;
    private List<Entry> mLinePoints;
    private ArrayList<String> mVals;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.getData().getCharSequence("message").toString();
            if (message.contains("--")) {
                String[] splits = message.split("--");
                if (splits.length > 0) {
                    int value = Integer.parseInt(splits[0]);
                    SimpleDateFormat dateFormat = new SimpleDateFormat();
                    try {
                        //Date date = (Date) dateFormat.parse(splits[1]);
                        addToPoint(value, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mTextView != null) {
                        mTextView.setText(msg.getData().getCharSequence("message"));
                    }

                }
            }
        }
    };

    private void addToPoint(int value, Date date) {
        Entry entry = new Entry((float)value, mLinePoints.size() - 1);
        mLinePoints.add(entry);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(mLine);

        mVals.add(mLinePoints.size() - 1, Calendar.getInstance().getTime().toString());

        LineData data = new LineData(mVals, dataSets);
        mLineGraph.setData(data);
        mLineGraph.invalidate(); // refresh
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mLineGraph = (LineChart) findViewById(R.id.chart);
        mVals = new ArrayList<String>();
        mLinePoints = new ArrayList<>();
        mLine = new LineDataSet(mLinePoints, "Heart rate");
        mLine.setAxisDependency(YAxis.AxisDependency.LEFT);
        mLineGraph.setY(100);
        YAxis leftAxis = mLineGraph.getAxisLeft();
        leftAxis.setAxisMaxValue(150 );
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataLayerListenerService.setHandler(handler);
    }

    @Override
    protected void onPause() {
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
