package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.MypageFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MybabyFragment extends Fragment implements View.OnClickListener {
    RecyclerView mRecyclerView;
    ArrayList<Mybabyinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    ImageView _back;
    View v;
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    String TAG = "MybabyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby, container, false);
        mContext = v.getContext();

        _back = (ImageView) v.findViewById(R.id.btback);
        _back.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);


        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info";
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", loginInfo.getEmail());
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end


        return v;
    }


    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        String id = null;
        String name = null;
        String birthday = null;
        String sex = null;
        String father = null;
        String mother = null;

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getString("baby_id");
                name = rs.getString("babyname");
                birthday = rs.getString("birthday");
                if (rs.getString("sex").equals("1")) {
                    sex = "남자";
                } else {
                    sex = "여자";
                }
                ;
                father = rs.getString("father");
                mother = rs.getString("mother");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(new Mybabyinfo(id, name, birthday, sex, father, mother));
        }

        mAdapter.notifyDataSetChanged();
    }

    ;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btback:

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
        }
    }
}
