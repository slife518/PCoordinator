package com.company.jk.pcoordinator.chart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.EntryComparator;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChartFragment extends MyFragment implements OnChartValueSelectedListener {

    View v;
    Context mContext;
    private BarChart mChart;
    private final String TAG = "ChartFragment";
    private LoginInfo loginInfo ;
    MyDataTransaction transaction;

    JSONArray jsonArray;
    final ArrayList<String> xVals = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_chart, container, false);
        mContext = v.getContext();

        // layout_toolbar 설정1
//        setHasOptionsMenu(true);   // layout_toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.chartTitle));
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);
        activity.setSupportActionBar(myToolbar);
        setHasOptionsMenu(true);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                                 @Override
                                                 public boolean onMenuItemClick(MenuItem item) {
                                                     showToast("준비 중에 있습니다.");
                                                     return false;
                                                 }
                                             }

        );

        loginInfo = LoginInfo.getInstance(mContext);
        transaction = new MyDataTransaction(mContext);

        mChart = v.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        setingChart();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chart, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void setingChart(){   //차트 기본 세팅

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
//        mSeekBarX.setProgress(7);
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
                Map<String, String> params = new HashMap<>();
                params.put("email", loginInfo.getEmail());
                VolleyCallback callback = new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1
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

    private void responseSuccess(String response) {
        Log.d(TAG, "결과값은 " + response);
        // setting barData
        jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        float val1, val2, val3;

        for (int i = 1; i < jsonArray.length() + 1; i++) {
            int k = jsonArray.length() - i;
            try {
                JSONObject rs = (JSONObject) jsonArray.get(k);

                val1 = (float) rs.getInt("mothermilk");
                val2 = (float) rs.getInt("milk");
                val3 = (float) rs.getInt("rice");

                Log.d(TAG, "모유는 " + val1 + " 분유는 " + val2 + " 이유식은 " + val3);
                yVals.add(new BarEntry(jsonArray.length() - i , new float[]{val1, val2, val3}));

                // 월일만 표현.. 만일 dday 로 표현하려면 이부분을 수정하면 됨.
                xVals.add( rs.getString("record_date").substring(5,10));   //x축의 날짜표시

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(xVals);

        BarDataSet barDataSet;
        Collections.sort(yVals, new EntryComparator());  // 이거를 안해주면 줌인할 때 그래프 사라짐.

        //차트전체의 설정
        barDataSet = new BarDataSet(yVals, "");  //타이틀 및 데이터 셋
//            barDataSet.setDrawIcons(false);  //아이콘
        barDataSet.setColors(getColors());  //바차트 색상 설정
        barDataSet.setStackLabels(new String[]{getResources().getString(R.string.mothermilk), getResources().getString(R.string.milk), getResources().getString(R.string.rice)});   //라벨명

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);

        //차트안의 데이타표현관련 설정
        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new MyValueFormatter()); //차트 안의 값의 표현형식
        barData.setHighlightEnabled(true);
        barData.setValueTextColor(getResources().getColor(R.color.primaryColor )); //글자색
        barData.setValueTextSize(11f);  //차트 안의 값 글씨크기

        // x축 label
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);  // x레이블의 중복 방지
        xAxis.setGridColor(ContextCompat.getColor(getContext(), R.color.transparent)); // X축 줄의 컬러 설정

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (xVals.size() > (int) value) {
                    return xVals.get((int) value);
                } else return null;
            }
        });

        mChart.setData(barData);

        // now modify viewport
        mChart.setVisibleXRangeMaximum(7); // allow 20 values to be displayed at once on the x-axis, not more
        mChart.moveViewToX(jsonArray.length()-6); // set the left edge of the chart to x-index 10
        // moveViewToX(...) also calls invalidate()

        barDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(0);
        barDataSet.setValues(yVals);

        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.setScaleEnabled(true);  //확대
        mChart.setFitBars(false);
        mChart.invalidate();

    }



//    private void responseSuccess(String response) {
//        Log.d(TAG, "결과값은 " + response);
//        // setting data
//        jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");
//
//        // Progress Bar 의 기본세팅   7일이상의 데이터가 있으면 기본 7일 표현
//        mSeekBarX.setMax(jsonArray.length());
//        if(mSeekBarX.getProgress() == 0){
////            if(jsonArray.length() > 7) {
////                mSeekBarX.setProgress(7);
////            }else {
//            mSeekBarX.setProgress(jsonArray.length());
////            }
//        }
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        if (mSeekBarX.getProgress() ==0){
//            return;
//        }
//        // progress bar 의 최대 표현가능 일
//        tvXmax.setText(getResources().getString(R.string.lastest) + " " + (mSeekBarX.getProgress()) + "일");
//
//        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
//
//        float val1, val2, val3;
//
//        for (int i = 1; i < mSeekBarX.getProgress() + 1; i++) {
//            int k = jsonArray.length() - i;
//            try {
//                JSONObject rs = (JSONObject) jsonArray.get(k);
//
//                val1 = (float) rs.getInt("mothermilk");
//                val2 = (float) rs.getInt("milk");
//                val3 = (float) rs.getInt("rice");
//
//                Log.d(TAG, "모유는 " + val1 + " 분유는 " + val2 + " 이유식은 " + val3);
//                yVals.add(new BarEntry(mSeekBarX.getProgress() - i , new float[]{val1, val2, val3}));
//
//                // 월일만 표현.. 만일 dday 로 표현하려면 이부분을 수정하면 됨.
//                xVals.add( rs.getString("record_date").substring(5,10));   //x축의 날짜표시
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        Collections.reverse(xVals);
//
//        BarDataSet set1;
//        Collections.sort(yVals, new EntryComparator());  // 이거를 안해주면 줌인할 때 그래프 사라짐.
//        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals);
//
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//            mChart.setScaleEnabled(true);  //확대
//
//
//        } else {  //차트 데이터관련 설정
//
//
//            //차트전체의 설정
//            set1 = new BarDataSet(yVals, "");  //타이틀 및 데이터 셋
////            set1.setDrawIcons(false);  //아이콘
//            set1.setColors(getColors());  //바차트 색상 설정
//            set1.setStackLabels(new String[]{getResources().getString(R.string.mothermilk), getResources().getString(R.string.milk), getResources().getString(R.string.rice)});   //라벨명
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            //차트안의 데이타표현관련 설정
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new MyValueFormatter()); //차트 안의 값의 표현형식
//            data.setHighlightEnabled(true);
//            data.setValueTextColor(getResources().getColor(R.color.primaryColor )); //글자색
//            data.setValueTextSize(11f);  //차트 안의 값 글씨크기
//
//            // x축 label
//            XAxis xAxis1 = mChart.getXAxis();
//            xAxis1.setGranularity(1f);  // x레이블의 중복 방지
//            xAxis1.setGridColor(ContextCompat.getColor(getContext(), R.color.transparent)); // X축 줄의 컬러 설정
//
//            xAxis1.setValueFormatter(new ValueFormatter() {
//                @Override
//                public String getFormattedValue(float value) {
//                    if (xVals.size() > (int) value) {
//                        return xVals.get((int) value);
//                    } else return null;
//                }
//            });
//
//            mChart.setData(data);
//
//            // now modify viewport
//            mChart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
//            mChart.moveViewToX(50); // set the left edge of the chart to x-index 10
//            // moveViewToX(...) also calls invalidate()
//
//        }
//        mChart.setFitBars(false);
//        mChart.invalidate();
//    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null) {
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
//            showToast(xVals.get(jsonArray.length()-(int)h.getX()-1) + " " + entry.getYVals()[h.getStackIndex()]);

            try {
                JSONObject rs = (JSONObject) jsonArray.get((int)h.getX());
                showToast(calculateDay(rs.getString("record_date")));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
//            showToast(xVals.get((int)h.getX()));

        }
    }

    public String calculateDay(String today){

        //String babybirthday = format.format(loginInfo.getBabyBirthday());
        int diffmonth = 0, diffday = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date beginDate = formatter.parse(loginInfo.getBabyBirthday());

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = formatter2.parse(today);
            long diff = endDate.getTime() - beginDate.getTime();
            diffmonth = (int)( diff / (24 * 60 * 60 * 1000) ) / 30;
            diffday = (int)( diff / (24 * 60 * 60 * 1000) );

        } catch (Exception e) {
            e.printStackTrace();
        }
        String message;
        if(diffday <= 100 ){
            message = getResources().getString(R.string.subtitle) + " "+ diffday + getResources().getString(R.string.subtitle1);

        }else {
            message = getResources().getString(R.string.subtitle) + " " + diffmonth + getResources().getString(R.string.subtitle2)
                    + diffday + getResources().getString(R.string.day) ;
        }

        return message;

    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub
    }

    private int[] getColors() {
        int[] colors = new int[]{ getResources().getColor(R.color.mothermilkcolor),  getResources().getColor(R.color.milkcolor),  getResources().getColor(R.color.ricecolor)};
        return colors;
    }



}

