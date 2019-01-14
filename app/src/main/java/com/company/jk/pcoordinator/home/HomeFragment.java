package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.record.RecordActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Vector;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnChartValueSelectedListener {

    private Context mContext;
    private final String TAG = "HomeFragment";
    private LoginInfo loginInfo = LoginInfo.getInstance();

    private ArrayList<RecordChartInfo> chartItems = new ArrayList();
    private ArrayList<RecordHistoryinfo> listItems = new ArrayList();
    private ArrayList<RecordHistoryinfo> items = new ArrayList();
    private MilkRiceListViewAdapter mAdapter = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Integer max_milk, min_milk, max_mothermilk, min_mothermilk;


    private LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView mListView;

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = v.getContext();

        // toolbar 설정1
//        setHasOptionsMenu(true);   // toolbar 의 추가 메뉴
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = v.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        myToolbar.setTitle(R.string.app_name);


        //listview layout
        mListView = v.findViewById(R.id.listView_main);

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


        mChart = v.findViewById(R.id.chart1);

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

    private void drawLineChart(ArrayList<RecordChartInfo> chartItems){

        // 그래프보여주기 시작
        Description desc = new Description();
        desc.setText(""); // 오른쪽 아래에 작게 보여줘서 TextView 로 대체 처리 함
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
        mChart.setBackgroundColor(Color.TRANSPARENT);

        // add data
        setData(chartItems);

        mChart.animateX(500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        // X축
//            XAxis xAxis = mChart.getXAxis();
//    //        xAxis.setTypeface(mTfLight);
//            xAxis.setTextSize(11f);
//            xAxis.setTextColor(Color.BLACK);
//            xAxis.setDrawGridLines(false);
//            xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mChart.getAxisLeft();
    //        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(((max_milk + 100) > 1000)? 1000 : max_milk + 100);
        leftAxis.setAxisMinimum(((min_milk - 30) < 0)? 0 : min_milk - 30);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setTextColor(ColorTemplate.getHoloBlue());
        rightAxis.setAxisMaximum(((max_mothermilk + 20) > 100)? 100 : max_mothermilk + 20);
        rightAxis.setAxisMinimum(((min_mothermilk - 10) < 0)? 0 : min_mothermilk - 10);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
        //그래프보여주기 끝

    }
    @Override
    public void onStart() {
        super.onStart();
        //data binding start
        get_data_async();

    }

    private void get_data_async(){  //데이터를 비 동기로 가져오기
        Log.i(TAG, "get_data_async 시작");
        if (NetworkUtil.getConnectivityStatusBoolean(mContext.getApplicationContext())) {
            new HttpTaskRecordList().execute();
        }
    }
    class HttpTaskRecordList extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //사용자가 다운로드 중 파워 버튼을 누르더라도 CPU가 잠들지 않도록 해서
            //다시 파워버튼 누르면 그동안 다운로드가 진행되고 있게 됩니다.
//            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
//            mWakeLock.acquire();

            //파일 다운로드를 시작하기 전에 프로그레스바를 화면에 보여줍니다.
//            progressBar.show();

        }

        @Override
        protected String doInBackground(String... args) {

            String server_url = new UrlPath().getUrlPath() + "Pc_record/record_list";
            Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
            nameValue.add(new BasicNameValuePair("email", loginInfo.getEmail()));//

            try {
                HttpPost request = new HttpPost(server_url);   //request객체에 URL SET
                //웹 접속 - utf-8 방식으로
                HttpEntity entity = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8); //Vector 값을 HttpEntity 객체로 만든다.
                request.setEntity(entity);  //HttpEntity 객체를 인자값으로 하는 HttpPost 객체 request에 담는다.
                //웹 서버에서 값을 받기 위한 객체 생성
                //HttpClient client = HttpClientBuilder.create().build();
                HttpClient client = new DefaultHttpClient();
                HttpResponse res = client.execute(request);   //URL 에 접속하고 넘겨준 인자로 실행
                //웹 서버에서 값받기******************************
                HttpEntity entityResponse = res.getEntity();   //전달받은 vector(?) 값 HttpEntity 객체에 담는다.
                InputStream im = entityResponse.getContent();  // vector 값을 InputStream 에 담고 BufferedReader 객체에 담는다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(im, HTTP.UTF_8));
                String line = null;
                //readLine -> 파일내용을 줄 단위로 읽기
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Log.i(TAG, "sb : " + sb.toString());
                itemAppend(sb.toString());
                im.close();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            mAdapter.notifyDataSetChanged();
            mAdapter.refreshAdapter(listItems);
            if(!chartItems.isEmpty()) {
                drawLineChart(chartItems);
            }

        }
    }

    private void itemAppend(String response) {
        Log.i(TAG, "결과값은 " + response);
        listItems.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                listItems.add(new RecordHistoryinfo(
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

        //차트 데이터 가져오기
        jsonArray = JsonParse.getJsonArrayFromString(response, "chartData");
        chartItems.clear();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                chartItems.add(new RecordChartInfo(
                        rs.getString("record_date"),
                        (rs.getString("mothermilk")==null||rs.getString("mothermilk").isEmpty()?0:Integer.parseInt(rs.getString("mothermilk"))),
                        (rs.getString("milk")==null||rs.getString("milk").isEmpty()?0 :Integer.parseInt(rs.getString("milk"))),
                        (rs.getString("rice")==null||rs.getString("rice").isEmpty()?0 :Integer.parseInt(rs.getString("rice")))
                ));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //최대값 최소값 가져오기
        //차트 데이터 가져오기
        jsonArray = JsonParse.getJsonArrayFromString(response, "max_value");


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);

                max_mothermilk =        Integer.parseInt(rs.getString("max_mothermilk"));
                min_mothermilk =        Integer.parseInt(rs.getString("min_mothermilk"));
                max_milk =        Integer.parseInt(rs.getString("max_milk"));
                min_milk =        Integer.parseInt(rs.getString("min_milk"));

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//        tvX.setText("" + (mSeekBarX.getProgress() + 1));
//        tvY.setText("" + (mSeekBarY.getProgress()));
//
//        setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());
//
//        // redraw
//        mChart.invalidate();
//    }

    @Override
    public void onRefresh() {
        get_data_async();
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setData(ArrayList<RecordChartInfo> chartItems) {  //날짜, 모유수유, 분유, 이유식
        Log.i(TAG, "setData 시작 " + chartItems.size());
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<Entry> yVals3 = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<String>();


//        for (int i = 0; i < chartItems.size(); i++) {
//            yVals1.add(new Entry(i, chartItems.get(i).getMothermilk()));
//            yVals2.add(new Entry(i, chartItems.get(i).getMilk()));
//            yVals3.add(new Entry(i, chartItems.get(i).getRice()));
//            xVals.add( chartItems.get(n).getDate());
//        }


        final int maxDay = 15;  // 해당 일 수 만큼만 보여줌
        final int visuableSize = (chartItems.size() >= maxDay) ? maxDay : chartItems.size();  // 데이터가 mayDay 개 보다 적으면 maxDay

        for (int i = 0; i < visuableSize; i++) {
            int n =  chartItems.size() - visuableSize + i ;
            yVals1.add(new Entry(i, chartItems.get(n).getMothermilk()));
            yVals2.add(new Entry(i, chartItems.get(n).getMilk()));
            yVals3.add(new Entry(i, chartItems.get(n).getRice()));
            xVals.add( chartItems.get(n).getDate());
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
                set1 = new LineDataSet(yVals1, "수유");

                set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
                set1.setColor(ColorTemplate.getHoloBlue());
                set1.setCircleColor(ColorTemplate.getHoloBlue());
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
                set2 = new LineDataSet(yVals2, "분유");

                set2.setAxisDependency(YAxis.AxisDependency.LEFT);  //지표 참조
                set2.setColor(Color.RED);
                set2.setCircleColor(Color.RED);
                set2.setLineWidth(2f);
                set2.setCircleRadius(3f);
                set2.setFillAlpha(65);
                set2.setFillColor(Color.RED);
                set2.setDrawCircleHole(false);
                set2.setHighLightColor(Color.rgb(244, 117, 117));
                //set2.setFillFormatter(new MyFillFormatter(900f));


                set3 = new LineDataSet(yVals3, "이유식");

                set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3.setColor(Color.YELLOW);
                set3.setCircleColor(Color.YELLOW);
                set3.setLineWidth(2f);
                set3.setCircleRadius(3f);
                set3.setFillAlpha(65);
                set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
                set3.setDrawCircleHole(false);
                set3.setHighLightColor(Color.rgb(244, 117, 117));

                // create a data object with the datasets
                LineData data = new LineData(set1, set2, set3);

                data.setValueTextColor(Color.GRAY);
                data.setValueTextSize(9f);

                // x축 label
                XAxis xAxis = mChart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        return xVals.get((int)value);
                    }

                });

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
