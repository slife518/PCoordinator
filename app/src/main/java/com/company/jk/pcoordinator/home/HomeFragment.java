package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.common.MyValueFormatter;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.record.RecordActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.company.jk.pcoordinator.MainActivity.fab;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnChartValueSelectedListener{

    private Context mContext;
    private final String TAG = "HomeFragment";
    private LoginInfo loginInfo ;

    private ArrayList<RecordChart1Info> chart1Items = new ArrayList();
    private ArrayList<RecordChart2Info> chart2Items = new ArrayList();
    private ArrayList<RecordHistoryinfo> listItems = new ArrayList();
    private ArrayList<RecordHistoryinfo> items = new ArrayList();
    private MilkRiceListViewAdapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Integer max_milk, max_mothermilk, min_milk,  min_mothermilk;
    private LineChart mChart1, mChart2;
    private ImageView iv_sample1, iv_sample2;
    private Boolean isChart1 = false, isChart2 = false;  // 차트를 보여줄 지 말지
    MyDataTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView mListView;

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = v.getContext();
        transaction = new MyDataTransaction(mContext);
        loginInfo = LoginInfo.getInstance(mContext);
        // layout_toolbar 설정 시작
//        setHasOptionsMenu(true);   // layout_toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        if(loginInfo.getBabyID() == 0 ){   //매핑된 아이가 없을 경우
            myToolbar.setTitle(getResources().getString(R.string.app_name));
        }else{   //매핑된 아이가 있을 경우
            myToolbar.setTitle(loginInfo.getBabyname());
        }
        if(loginInfo.getBabyBirthday() != null){
            myToolbar.setSubtitle(make_subtitle());
        }
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);
        activity.setSupportActionBar(myToolbar);
        // layout_toolbar 설정 끝

        //listview layout
        mListView = v.findViewById(R.id.listView_main);
        iv_sample1 = v.findViewById(R.id.sample1);
        iv_sample2 = v.findViewById(R.id.sample2);

        mChart1 = v.findViewById(R.id.chart1);
        mChart2 = v.findViewById(R.id.chart2);

        mAdapter = new MilkRiceListViewAdapter(mContext, R.layout.layout_milk_rice_card, items);
        mListView.setAdapter(mAdapter);
//        setListViewHeightBasedOnChildren(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                Bundle loadInfo = new Bundle();
                loadInfo.putSerializable("RecordHistoryinfo", items.get(position));
                intent.putExtras(loadInfo);
                startActivityForResult(intent, 2300);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int newState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if(i > 3){
                    fab.hide();
                }else{
                    fab.show();
                }
            }
        });


        //리스트 새로고침
        mSwipeRefreshLayout = v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //data binding start
        if(loginInfo.getBabyID() != 0) {   //매핑된 아이가 있을 경우
            get_data_async();
        }else{   // 매핑된 아기가 없을 경우
            mChart1.setVisibility(GONE);
            mChart2.setVisibility(GONE);
            iv_sample1.setVisibility(VISIBLE);
            iv_sample2.setVisibility(VISIBLE);
        }
    }


    private String make_subtitle(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String today = format.format(new Date());
        //String babybirthday = format.format(loginInfo.getBabyBirthday());
        int diffmonth = 0, diffday = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date beginDate = formatter.parse(loginInfo.getBabyBirthday());
            Date endDate = formatter.parse(today);
            long diff = endDate.getTime() - beginDate.getTime();
            diffmonth = (int)( diff / (24 * 60 * 60 * 1000) ) / 30;
            diffday = (int)( diff / (24 * 60 * 60 * 1000) );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getResources().getString(R.string.subtitle) + " " + diffmonth + getResources().getString(R.string.subtitle2)
                + diffday + getResources().getString(R.string.day) ;


    }


    private void get_data_async(){
        Log.d(TAG, "get_data_async 시작");
        if(loginInfo.getBabyID() != 0) {
            if (NetworkUtil.getConnectivityStatusBoolean(mContext.getApplicationContext())) {
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
        mAdapter.notifyDataSetChanged();

        // 수유차트
        if(isChart1) {
            mChart1.setVisibility(VISIBLE);
            setChart1Data(chart1Items);   // add data
//                drawLineChart(mChart1, max_mothermilk, min_mothermilk);  //draw LineChart
            drawLineChart(mChart1);  //draw LineChart
        }else{  // 차트 안보이게 처리
            mChart1.setVisibility(GONE);
            mChart2.setPadding(20, 0, 0, 0);
        }


        //분유/이유식차트
        if(isChart2) {
            mChart2.setVisibility(VISIBLE);
            setChart2Data(chart2Items);  // add data
//                drawLineChart(mChart2, max_milk, min_milk);  //draw LineChart
            drawLineChart(mChart2);  //draw LineChart
        }else{  // 차트 안보이게 처리
            mChart2.setVisibility(GONE);
            mChart1.setPadding(20, 0, 0, 0);
        }

        if(!isChart1 && !isChart2){
            iv_sample1.setVisibility(VISIBLE);
            iv_sample2.setVisibility(VISIBLE);
        }else{
            iv_sample1.setVisibility(GONE);
            iv_sample2.setVisibility(GONE);
        }

    }

    private void drawLineChart(LineChart chart){

        Description desc = new Description();
        if(chart == mChart1) {
            desc.setText(getResources().getString(R.string.chartTitle1));
        }else{
            desc.setText(getResources().getString(R.string.chartTitle2));
        }
        chart.setDescription(desc);
        chart.setOnChartValueSelectedListener(this);

//        // no description text
//        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.TRANSPARENT);


//        chart.animateX(500);


        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);


        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();   // color & label setting

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(15f);

//        // X축
//            XAxis xAxis = chart.getXAxis();
//            xAxis.setXOffset(11f);
//            xAxis.setGranularityEnabled(true);
//
//    //        xAxis.setTypeface(mTfLight);
//            xAxis.setTextSize(11f);
//            xAxis.setTextColor(Color.BLACK);
//            xAxis.setDrawGridLines(false);
//            xAxis.setDrawAxisLine(false);


//        //Y축
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setYOffset(11f);
//        leftAxis.setGranularityEnabled(true);

//        //        leftAxis.setTypeface(mTfLight);
//        leftAxis.setTextColor(Color.BLACK);
//        leftAxis.setAxisMaximum(max);
//        leftAxis.setAxisMinimum(min);
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setGranularityEnabled(true);

        //그래프보여주기 끝

    }

    private void itemAppend(String response) {
        Log.d(TAG, "결과값은 " + response);
        listItems.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                listItems.add(new RecordHistoryinfo(
                        rs.getString("id"),
                        rs.getString("record_date"),
                        rs.getString("record_time"),
                        rs.getInt("milk"),
                        rs.getInt("mothermilk"),
                        rs.getInt("rice"),
                        rs.getString("author"),
                        rs.getString("description")));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(loginInfo.getBabyID() != 0) {
            //차트 데이터 가져오기
            jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");
            chart1Items.clear();
            chart2Items.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject rs = (JSONObject) jsonArray.get(i);
                    chart1Items.add(new RecordChart1Info(
                            rs.getString("record_date"),
                            rs.getInt("mothermilk")
//                            Integer.parseInt(rs.getString("mothermilk"))
                    ));

                    chart2Items.add(new RecordChart2Info(
                            rs.getString("record_date"),
                            rs.getInt("milk"),
                            rs.getInt("rice")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //최대값 최소값 가져오기
            //차트 데이터 가져오기
            jsonArray = JsonParse.getJsonArrayFromString(response, "max_value");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject rs = (JSONObject) jsonArray.get(i);

                    max_mothermilk = rs.getInt("max_mothermilk");
                    min_mothermilk = 0;
                    max_milk = rs.getInt("max_milk");
                    min_milk = 0;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 최대값 0 이상이면 차트를 보여준다.
            isChart1  = (max_mothermilk != null && max_mothermilk > 0) ? true : false;
            isChart2  = (max_milk != null && max_milk > 0) ? true : false;
        }
    }


    @Override
    public void onRefresh() {
        get_data_async();
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setChart1Data(ArrayList<RecordChart1Info> chartItems) {  //날짜, 모유수유, 분유, 이유식
        Log.d(TAG, "setData 시작 " + chartItems.size());
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<String>();
        final int maxDay = 15;  // 해당 일 수 만큼만 보여줌
        final int visuableSize = (chartItems.size() >= maxDay) ? maxDay : chartItems.size();  // 데이터가 mayDay 개 보다 적으면 maxDay

        for (int i = 0; i < visuableSize; i++) {
            int n =  chartItems.size() - visuableSize + i ;
            yVals1.add(new Entry(i, chartItems.get(n).getMothermilk()));
            xVals.add( chartItems.get(n).getDate());
        }

        LineDataSet set1;

        if (mChart1.getData() != null &&
                mChart1.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart1.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart1.getData().notifyDataChanged();
            mChart1.notifyDataSetChanged();
        } else {

            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "수유");

            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set1.setColor(getResources().getColor(R.color.mothermilkcolor));
            set1.setCircleColor(ColorTemplate.getHoloBlue());
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(getResources().getColor(R.color.mothermilkcolor));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
//                set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a data object with the datasets
            LineData data1 = new LineData(set1);  // 수유


            data1.setValueTextColor(Color.BLACK);
            data1.setValueTextSize(9f);
            data1.setValueFormatter(new MyValueFormatter());   //차트 내의 데이터 표현형식
//
//                // x축 label
            XAxis xAxis1 = mChart1.getXAxis();
            xAxis1.setValueFormatter(new ValueFormatter() {

                @Override
                public String getFormattedValue(float value) {
                    if (xVals.size() > (int) value) {
                        return xVals.get((int) value);
                    } else return null;
                }

            });
            // set data
            mChart1.setData(data1);
        }
    }

    private void setChart2Data(ArrayList<RecordChart2Info> chartItems) {  //날짜, 모유수유, 분유, 이유식
        Log.d(TAG, "setData 시작 " + chartItems.size());
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<String>();


        final int maxDay = 15;  // 해당 일 수 만큼만 보여줌
        final int visuableSize = (chartItems.size() >= maxDay) ? maxDay : chartItems.size();  // 데이터가 mayDay 개 보다 적으면 maxDay

        for (int i = 0; i < visuableSize; i++) {
            int n =  chartItems.size() - visuableSize + i ;
            yVals2.add(new Entry(i, chartItems.get(n).getMilk()));
            yVals3.add(new Entry(i, chartItems.get(n).getRice()));
            xVals.add( chartItems.get(n).getDate());
        }


        LineDataSet set2, set3;

        if (mChart2.getData() != null &&
                mChart2.getData().getDataSetCount() > 0) {

            set2 = (LineDataSet) mChart2.getData().getDataSetByIndex(0);
            set3 = (LineDataSet) mChart2.getData().getDataSetByIndex(1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart2.getData().notifyDataChanged();
            mChart2.notifyDataSetChanged();
        } else {

            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, "분유");

            set2.setAxisDependency(YAxis.AxisDependency.LEFT);  //지표 참조
            set2.setColor(getResources().getColor(R.color.milkcolor));
            set2.setCircleColor(getResources().getColor(R.color.milkcolor));
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(getResources().getColor(R.color.milkcolor));
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));


            set3 = new LineDataSet(yVals3, "이유식");

            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(getResources().getColor(R.color.ricecolor));
            set3.setCircleColor(getResources().getColor(R.color.ricecolor));
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(ColorTemplate.colorWithAlpha(getResources().getColor(R.color.ricecolor), 200));
            set3.setDrawCircleHole(false);
            set3.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data2 = new LineData(set2, set3);  // 분유/이유식

            data2.setValueTextColor(Color.GRAY);
            data2.setValueTextSize(9f);
            data2.setValueFormatter(new MyValueFormatter());

            // x축 label
            XAxis xAxis1 = mChart2.getXAxis();
            xAxis1.setValueFormatter(new ValueFormatter() {

                @Override
                public String getFormattedValue(float value ){
                    if (xVals.size() > (int) value) {
                        return xVals.get((int) value);
                    } else return null;
                }

            });

//            // x축 label
//            XAxis xAxis2 = mChart2.getXAxis();
//            xAxis2.setValueFormatter(new IAxisValueFormatter() {
//
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//
//                    return xVals.get((int)value);
//                }
//
//            });

            // set data
            mChart2.setData(data2);

        }
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d("Entry selected", e.toString());

//        mChart1.centerViewToAnimated(e.getX(), e.getY(), mChart1.getData().getDataSetByIndex(h.getDataSetIndex())
//                .getAxisDependency(), 500);
        //mChart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //mChart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.d("Nothing selected", "Nothing selected.");
    }

}
