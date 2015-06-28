package ro.multiplesingleton.hearthy;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import ro.multiplesingleton.hearthy.SQL.HeartRateData;


public class MainActivity extends Activity implements OnChartValueSelectedListener {

    private TextView mTextHeart;
    private TextView mTextDate;
    private LineChart mLineGraph;
    private LineDataSet mLine;
    private List<Entry> mLinePoints;
    private ArrayList<String> mVals;
    private int currentValue;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.getData().getCharSequence("message").toString();
            if (message.contains("--")) {
                String[] splits = message.split("--");
                if (splits.length > 0) {
                    int value = Integer.parseInt(splits[0]);
                    if (value > 0 && value != currentValue) {
                        currentValue = value;
                        addToPoint(value, null);
                        saveValueToDb(value);
                        if (mTextHeart != null) {
                            mTextHeart.setText(Integer.toString(currentValue) + "bpm");
                            mTextDate.setText(message.substring(0, message.indexOf("GMT")));
                        }
                    }
                }
            }
        }
    };

    private void addToPoint(int value, Date date) {
        boolean hasEntry = false;
        boolean hasDate = false;
        for (int i = 0; i < mLinePoints.size(); i++) {
            Entry entry = mLinePoints.get(i);
            String mVal = mVals.get(i);
            if (entry.getVal() == (float) value)
                hasEntry = true;
            if (date != null) {
                if (mVal == date.toString()) {
                    hasDate = true;
                }
            }
            if (hasEntry && hasDate) {
                return;
            }
        }

        Entry entry = new Entry((float) value, mLinePoints.size() - 1);
        mLinePoints.add(entry);
        mLine.setDrawCubic(true);
        mLine.setFillColor(Color.parseColor("#008800"));
        mLine.setDrawFilled(true);
        mLine.setCircleSize(2f);
        mLine.setCircleColor(Color.parseColor("#002200"));
        ValueFormatter formater = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value);
            }
        };
        mLine.setValueFormatter(formater);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(mLine);
        String dateToAdd;
        if (date == null) {
            String dt = Calendar.getInstance().getTime().toString();
            dateToAdd = dt.substring(0, dt.indexOf("GMT"));
        } else {
            String dt = date.toString();
            dateToAdd = dt.substring(0, dt.indexOf("GMT"));
        }
        mVals.add(mLinePoints.size() - 1, dateToAdd);

        LineData data = new LineData(mVals, dataSets);
        mLineGraph.setData(data);
        mLineGraph.animateX(3000);
        mLineGraph.invalidate(); // refresh
    }

    private void saveValueToDb(int value) {
        HeartRateData data = new HeartRateData(value, Calendar.getInstance().getTime());
        data.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextHeart = (TextView) findViewById(R.id.heart);
        mTextDate = (TextView) findViewById(R.id.date);
        mLineGraph = (LineChart) findViewById(R.id.chart);
        mLineGraph.setDescription("");
        mLineGraph.setHighlightEnabled(true);
        mLineGraph.setTouchEnabled(true);
        mLineGraph.setDragEnabled((true));
        mLineGraph.setSaveEnabled(true);
        mLineGraph.setPinchZoom(true);
        mLineGraph.setDrawGridBackground(false);
        mLineGraph.setOnChartValueSelectedListener(this);
        mVals = new ArrayList<String>();
        mLinePoints = new ArrayList<>();
        mLine = new LineDataSet(mLinePoints, "Heart rate");
        mLine.setColor(Color.parseColor("#008800"));
        mLine.setAxisDependency(YAxis.AxisDependency.LEFT);
        YAxis leftAxis = mLineGraph.getAxisLeft();
        leftAxis.setAxisMaxValue(200);
        leftAxis.setAxisMinValue(40);
        YAxis rightAxis = mLineGraph.getAxisRight();
        rightAxis.setEnabled(false);
        getValuesFromDb();
    }

    private void getValuesFromDb() {
        List<HeartRateData> heartRates = HeartRateData.listAll(HeartRateData.class);
        if (heartRates != null && heartRates.size() > 0) {
            for (int i = 0; i < heartRates.size(); i++) {
                HeartRateData heartRateData = heartRates.get(i);
                int value = heartRateData.value;
                java.util.Date date = heartRateData.date;
                addToPoint(value, date);
            }
        }
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

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String date = mVals.get(e.getXIndex());
        mTextHeart.setText(Integer.toString((int) e.getVal()) + "bpm");
        mTextDate.setText(date);
    }

    @Override
    public void onNothingSelected() {

    }
}
