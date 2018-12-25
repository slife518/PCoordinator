package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.company.jk.pcoordinator.record.RecordFragment;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends MyFragment{


    View v;
    Context mContext;
    final String TAG = "HomeFragment";
    LoginInfo loginInfo = LoginInfo.getInstance();

    ListView mListView;
    ArrayList<RecordHistoryinfo> items = new ArrayList();
    MilkRiceListViewAdapter mAdapter;

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
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
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
        return v;
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

                Toast.makeText(getContext().getApplicationContext(),"통계기능이 업데이트 될 예정입니다.",Toast.LENGTH_LONG).show();
                return true;

            default:
                Toast.makeText(mContext.getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);


        }
    }
}
