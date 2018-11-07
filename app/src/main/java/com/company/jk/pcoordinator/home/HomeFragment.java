package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyAxisValueFormatter;
import com.company.jk.pcoordinator.common.MyValueFormatter;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.record.RecordFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnSeekBarChangeListener, OnChartValueSelectedListener {

    View v;
    Context mContext;
    private BarChart mChart;
    private SeekBar mSeekBarX;
    private TextView tvX;
    final String TAG = "HomeFragment";
    LoginInfo loginInfo = LoginInfo.getInstance();

    ListView mListView;
    ArrayList<RecordHistoryinfo> items = new ArrayList();
    ArrayList<RecordHistoryinfo> chartData = new ArrayList();
//    LinearLayoutManager mLayoutManager;
    MilkRiceListViewAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = v.getContext();

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //listview layout
        mListView = (ListView) v.findViewById(R.id.listView_main);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

//                HomeFragment myFragment = new HomeFragment();
                //왼쪽에서 오른쪽 슬라이드
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail(), mItems.get(position).date , mItems.get(position).time , mItems.get(position).id, mItems.get(position).milk, mItems.get(position).rice,  mItems.get(position).comments)).addToBackStack(null).commit();
                activity.getSupportFragmentManager().popBackStack();
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(items.get(position))).addToBackStack(null).commit();

            }
        });

        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_record/record_list";
        RequestQueue postReqeustQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", loginInfo.getEmail());
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end

        tvX = v.findViewById(R.id.tvXMax);

        mSeekBarX = v.findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mChart = v.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);

        // setting data
        mSeekBarX.setProgress(12);
//        mSeekBarY.setProgress(100);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(!chartData.isEmpty()) {
            changChart(mSeekBarX.getProgress());
        }

    }

    private  void changChart(int seekBar){
        tvX.setText("" + seekBar);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < seekBar; i++) {
//            float mult = (100 + 1);
//            float val1 = (float) (Math.random() * mult) + mult / 3;
//            float val2 = (float) (Math.random() * mult) + mult / 3;

            float val1 = Float.parseFloat(chartData.get(i).milk);
            float val2 = Float.parseFloat(chartData.get(i).rice);

//            Log.i(TAG,chartData.get(i).milk + "쌀은 " + chartData.get(i).rice );

            yVals1.add(new BarEntry(
                    i,
                    new float[]{val1, val2}));

        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "우리아기 식사량");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"분유", "이유식"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
//            Log.i("SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
//            Log.i("SELECTED", "Value: " + entry.getX());
            showToast(chartData.get(Math.round(entry.getX())).date);
        else
            Log.i("null SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        String id = null, date = null, time = null, milk = null, rice = null, author = null, comments = null;
        items.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getString("id");
                date = rs.getString("record_date");
                time = rs.getString("record_time");
                milk = rs.getString("milk");
                rice = rs.getString("rice");
                author = rs.getString("author");
                comments = rs.getString("description");
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, time);
            items.add(new RecordHistoryinfo(id, date, time, milk, rice, author, comments));
        }
        mAdapter = new MilkRiceListViewAdapter(mContext, R.layout.layout_milk_rice_card, items);
        mListView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();


        // 차트데이터 가져오기
        jsonArray = null;
        jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                date = rs.getString("record_date");
                milk = rs.getString("milk");
                rice = rs.getString("rice");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chartData.add(new RecordHistoryinfo(null, date, "12:00:00", milk, rice, null, null));
        }

        mSeekBarX.setMax(chartData.size());
        if(chartData.size() < 7) {
            changChart(chartData.size());
        }else{
            changChart(7);
        }



    }


    public static Fragment newInstance(RecordHistoryinfo info){
        RecordFragment fragment = new RecordFragment();
        Bundle bundle  = new Bundle();
        bundle .putSerializable("RecordHistoryinfo", info);
        fragment.setArguments(bundle );
        fragment.setArguments(bundle );
        return fragment;
    }

}
