package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.record.RecordActivity;

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

public class MainFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener{

    private Context mContext;
    private final String TAG = "HomeFragment";
    private LoginInfo loginInfo ;

    private ArrayList<RecordHistoryinfo> items = new ArrayList();
    private MilkRiceListViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private Integer max_milk, max_mothermilk, min_milk,  min_mothermilk;
//    private ImageView iv_sample1, iv_sample2;
    private Boolean isValue = false;  // 차트를 보여줄 지 말지
    MyDataTransaction transaction;
    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_main, container, false);
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
            myToolbar.setSubtitleTextAppearance(activity.getApplicationContext(), R.style.toolbarsubTitle);
            myToolbar.setSubtitle(make_subtitle());
        }
        myToolbar.setTitleTextAppearance(activity.getApplicationContext(), R.style.toolbarTitle);
        activity.setSupportActionBar(myToolbar);
        // layout_toolbar 설정 끝


//        iv_sample1 = v.findViewById(R.id.sample1);
//        iv_sample2 = v.findViewById(R.id.sample2);

        //listview layout
        mListView = v.findViewById(R.id.listView_main);

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

        Log.d(TAG, "onStart 시작");
        //data binding start
        if(loginInfo.getBabyID() != 0) {   //매핑된 아이가 있을 경우
            get_data_async();
        }else{   // 매핑된 아기가 없을 경우

//            iv_sample1.setVisibility(VISIBLE);
//            iv_sample2.setVisibility(VISIBLE);
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
                        itemAppend(result);
                        afterResponse(result);

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

    private void afterResponse(String result) {

        if(!isValue){
//            iv_sample1.setVisibility(VISIBLE);
//            iv_sample2.setVisibility(VISIBLE);
        }else{
//            iv_sample1.setVisibility(GONE);
//            iv_sample2.setVisibility(GONE);

            mAdapter = new MilkRiceListViewAdapter(mContext, R.layout.layout_milk_rice_card, items);
            mListView.setAdapter(mAdapter);

//            mAdapter.notifyDataSetChanged();
        }

    }

    private void itemAppend(String response) {
        Log.d(TAG, "결과값은 " + response);
        items.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                items.add(new RecordHistoryinfo(
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
        if(jsonArray.length() > 0){
            isValue = true;
        }
    }


    @Override
    public void onRefresh() {
        get_data_async();
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }



}
