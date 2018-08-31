package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonUtil;
import com.company.jk.pcoordinator.http.MyVolley;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MybabyFragment extends Fragment {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    View v;
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby, container, false);
        mContext = v.getContext();

        // Inflate the layout for this fragment
        final RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mybaby, container, false);

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", loginInfo.getEmail());

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, server_url,JsonUtil.getJsonStringFromMap(params), networkSuccessListener(), networkErrorListener());
        queue.add(myReq);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_main);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);



        return v;
    }


    private Response.Listener<JSONObject> networkSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String id = null;

                JSONArray jsonArray = JsonUtil.getJsonArrayFromJsonObject(response);

                try {
                    id = response.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // ArrayList 에 Item 객체(데이터) 넣기
                ArrayList<Mybabyinfo> items = new ArrayList();
                items.add(new Mybabyinfo("1","조민준", "2017년8월1일", "남자", "조정국", "배윤지"));
                items.add(new Mybabyinfo("2","조민준", "2017년8월1일", "남자", "조정국", "배윤지"));
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
                // Adapter 생성
                mAdapter = new RecyclerViewAdapter(items);
                mRecyclerView.setAdapter(mAdapter);

            }
        };
    }
    private Response.ErrorListener networkErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        };
    }




}
