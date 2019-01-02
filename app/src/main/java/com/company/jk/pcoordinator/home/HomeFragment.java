package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

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
import com.company.jk.pcoordinator.record.RecordFragment;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener {


    View v;
    Context mContext;
    final String TAG = "HomeFragment";
    LoginInfo loginInfo = LoginInfo.getInstance();

    ListView mListView;
    ArrayList<RecordHistoryinfo> items = new ArrayList();
    MilkRiceListViewAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = v.getContext();

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Toolbar myToolbar = (Toolbar)v.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);

        //listview layout
        mListView = (ListView) v.findViewById(R.id.listView_main);
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

        //리스트 새로고침
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        //data binding start

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
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
        mAdapter = new MilkRiceListViewAdapter(mContext, R.layout.layout_milk_rice_card, items);
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);

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

    public static Fragment newInstance(RecordHistoryinfo info){
        RecordFragment fragment = new RecordFragment();
        Bundle bundle  = new Bundle();
        bundle .putSerializable("RecordHistoryinfo", info);
        fragment.setArguments(bundle );
        fragment.setArguments(bundle );
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    //ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        Log.i(TAG, "그래프 클릭 ");


        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_chart:
                // User chose the "Settings" item, show the app settings UI...
                showToast("통계기능이 업데이트 될 예정입니다.");
//                Toast.makeText(mContext.getApplicationContext(),"통계기능이 업데이트 될 예정입니다.",Toast.LENGTH_LONG).show();
                return true;

            default:
                showToast("나머지 버튼 클릭됨");
                return super.onOptionsItemSelected(item);


        }
    }

    @Override
    public void onRefresh() {
        get_data();
        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
