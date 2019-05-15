package com.company.jk.pcoordinator.chart;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyAxisValueFormatter;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.common.MyValueFormatter;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChartFragment extends MyFragment implements SeekBar.OnSeekBarChangeListener, OnChartValueSelectedListener {

    View v;
    Context mContext;

    private BarChart mChart;
    private SeekBar mSeekBarY;
    private TextView tvY;
    private final String TAG = "ChartFragment";
    private LoginInfo loginInfo ;
    MyDataTransaction transaction;
//    private Integer max_milk, max_mothermilk, min_milk,  min_mothermilk;
    JSONArray jsonArray;
    final ArrayList<String> xVals = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chart, container, false);
        mContext = v.getContext();

        // toolbar 설정1
//        setHasOptionsMenu(true);   // toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.chartTitle));
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);
        activity.setSupportActionBar(myToolbar);


        loginInfo = LoginInfo.getInstance(mContext);
        transaction = new MyDataTransaction(mContext);

        tvY = v.findViewById(R.id.tvYMax);
        mSeekBarY = v.findViewById(R.id.seekBar1);
        mSeekBarY.setOnSeekBarChangeListener(this);

        mChart = v.findViewById(R.id.chart1);
//        mChart.setOnChartValueSelectedListener(this);
        setingChart();

        return v;
    }

    private void setingChart(){

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
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        // mChart.setDrawXLabels(false);
        // mChart.setDrawYLabels(false);
//
//        // setting data
//        mSeekBarX.setProgress(12);
//        mSeekBarY.setProgress(7);
//
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

    }


    @Override
    public void onStart() {
        super.onStart();
        //data binding start
        if(loginInfo.getBabyID() != 0) {   //매핑된 아이가 있을 경우
            get_data_async();
        }
    }

    private void get_data_async() {
        Log.d(TAG, "get_data_async 시작");
        if(loginInfo.getBabyID() != 0) {
            if (NetworkUtil.getConnectivityStatusBoolean(mContext.getApplicationContext())) {
//                new HttpTaskRecordList().execute();

                Map<String, String> params = new HashMap<>();
                params.put("email", loginInfo.getEmail());

                VolleyCallback callback = new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                        Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
                        responseSuccess(result);

                    }
                    @Override
                    public void onFailResponse(VolleyError error) {
                        Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
                    }
                };

                transaction.queryExecute(2, params, "Pc_record/record_list", callback);

            }
        }
    }

    private void responseSuccess(String result) {


        itemAppend(result);  // 조회 된 데이터 표현하기

    }


    private void itemAppend(String response) {
        Log.d(TAG, "결과값은 " + response);

            //최대값 최소값 가져오기
            //차트 데이터 가져오기
//            jsonArray = JsonParse.getJsonArrayFromString(response, "max_value");
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                try {
//                    JSONObject rs = (JSONObject) jsonArray.get(i);
//
//                    max_mothermilk = rs.getInt("max_mothermilk");
//                    min_mothermilk = 0;
//                    max_milk = rs.getInt("max_milk");
//                    min_milk = 0;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            // setting data
            jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");

            mSeekBarY.setMax(jsonArray.length());
            if(mSeekBarY.getProgress() == 0){
                mSeekBarY.setProgress(7);
            }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mSeekBarY.getProgress() ==0){
            return;
        }

        tvY.setText(getResources().getString(R.string.lastest) + " " + (mSeekBarY.getProgress()) + "일");

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < mSeekBarY.getProgress() + 1; i++) {
            int k = jsonArray.length() - i - 1;
            try {
                JSONObject rs = (JSONObject) jsonArray.get(k);

                float val1 = (float) rs.getInt("mothermilk");
                float val2 = (float) rs.getInt("milk");
                float val3 = (float) rs.getInt("rice");

                Log.d(TAG, "모유는 " + val1 + " 분유는 " + val2 + " 이유식은 " + val3);
                yVals1.add(new BarEntry(
                        mSeekBarY.getProgress() - i ,
                        new float[]{val1, val2, val3}));
                xVals.add( rs.getString("record_date"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(xVals);

        BarDataSet set1;


        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");  //타이틀 표시
//            set1.setDrawIcons(false);  //아이콘
            set1.setColors(getColors());  //바차트 색상 설정
            set1.setStackLabels(new String[]{getResources().getString(R.string.mothermilk), getResources().getString(R.string.milk), getResources().getString(R.string.rice)});   //라벨명


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter()); //차트 안의 값의 표현형식
            data.setHighlightEnabled(true);
            data.setValueTextColor(Color.GRAY); //글자색
            data.setValueTextSize(11f);  //차트 안의 값 글씨크기

            // x축 label
            XAxis xAxis1 = mChart.getXAxis();
            xAxis1.setValueFormatter(new ValueFormatter() {

                @Override
                public String getFormattedValue(float value) {
                    if (xVals.size() > (int) value) {

                        return xVals.get((int) value).substring(5,10);
                    } else return null;
                }

            });

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

        if (entry.getYVals() != null) {
            Log.d("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
            showToast(xVals.get(jsonArray.length()-(int)h.getX()-1) + " " + entry.getYVals()[h.getStackIndex()]);
        }else {
            Log.d("VAL SELECTED", "Value: " + entry.getY());
        }
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    private int[] getColors() {
//        int stacksize = 3;
////         have as many colors as stack-values per entry
//        int[] colors = new int[stacksize];
//        for (int i = 0; i < colors.length; i++) {
////            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
//            colors[i] = getResources().getColor(R.color.mothermilkcolor);
//        }

        int[] colors = new int[]{ getResources().getColor(R.color.mothermilkcolor),  getResources().getColor(R.color.milkcolor),  getResources().getColor(R.color.ricecolor)};

        return colors;
    }

}
