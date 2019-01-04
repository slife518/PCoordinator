package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.http.UrlPath;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnChartValueSelectedListener {

    private Context mContext;
    private final String TAG = "HomeFragment";
    private LoginInfo loginInfo = LoginInfo.getInstance();
    private ListView mListView;
    private ArrayList<RecordHistoryinfo> items = new ArrayList();
    private SwipeRefreshLayout mSwipeRefreshLayout;


    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = v.getContext();

        // toolbar 설정1
//        setHasOptionsMenu(true);   // toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);

        //listview layout
        mListView = v.findViewById(R.id.listView_main);
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

        mChart = (LineChart)v.findViewById(R.id.chart1);

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

    private void drawLineChart(String[] xVals, float yMotherMilk[], float yMilk[], float yRice[]){

        // 그래프보여주기 시작
        Description desc = new Description();
        desc.setText("우리아기 식사량");
        mChart.setDescription(desc);
        mChart.setOnChartValueSelectedListener(this);

//        // no description text
//        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
        setData(xVals,yMotherMilk, yMilk, yRice);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        //그래프보여주기 끝

    }
    @Override
    public void onStart() {
        super.onStart();
        //data binding start
        get_data();
    }

    private  void get_data(){
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
    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        items.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                items.add(new RecordHistoryinfo(
                                                rs.getString("id"),
                                                rs.getString("record_date"),
                                                rs.getString("record_time"),
                                                rs.getString("milk"),
                                                rs.getString("mothermilk"),
                                                rs.getString("rice"),
                                                rs.getString("author"),
                                                rs.getString("description")));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MilkRiceListViewAdapter mAdapter = new MilkRiceListViewAdapter(mContext, R.layout.layout_milk_rice_card, items);
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);


        //차트 데이터 가져오기

        jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");

        String xVals[] = {};
        float yMilk[] = {};
        float yMotherMilk[] = {};
        float yRice[] = {};

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                xVals[i]= rs.getString("record_date");
                yMotherMilk[i] = Float.parseFloat(rs.getString("mothermilk"));
                yMilk[i] = Float.parseFloat(rs.getString("milk"));
                yRice[i] = Float.parseFloat(rs.getString("rice"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        drawLineChart(xVals,yMotherMilk, yMilk, yRice);
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

//    // toolbar 설정2
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//       super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_home, menu);
//    }
//
//    // toolbar 설정3 ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        Log.i(TAG, "그래프 클릭 ");
//        switch (item.getItemId()) {
//            case R.id.action_chart:
//                // User chose the "Settings" item, show the app settings UI...
//                showToast("통계기능이 업데이트 될 예정입니다.");
////                Toast.makeText(mContext.getApplicationContext(),"통계기능이 업데이트 될 예정입니다.",Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                showToast("나머지 버튼 클릭됨");
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    public void onRefresh() {
        get_data();
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setData(String[] xVals, float yMotherMilk[], float yMilk[], float yRice[]) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < yMotherMilk.length; i++) {
            yVals1.add(new Entry(i, yMotherMilk[i]));
        }

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        for (int i = 0; i < yMilk.length; i++) {
            yVals2.add(new Entry(i, yMotherMilk[i]));
        }

        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        for (int i = 0; i < yRice.length; i++) {
            yVals3.add(new Entry(i, yMotherMilk[i]));
        }

        LineDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, "DataSet 2");
            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            set3 = new LineDataSet(yVals3, "DataSet 3");
            set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
            set3.setColor(Color.YELLOW);
            set3.setCircleColor(Color.WHITE);
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
            set3.setDrawCircleHole(false);
            set3.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
        //mChart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //mChart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}
