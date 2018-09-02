package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.MyVolley;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MybabyFragment extends Fragment {
    RecyclerView mRecyclerView;
    ArrayList<Mybabyinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    View v;
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    String TAG = "MybabyFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby, container, false);
        mContext = v.getContext();

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_main);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        // Adapter 생성
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);


        // Inflate the layout for this fragment
        final RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info/" +loginInfo.getEmail();
        JSONObject params = new JSONObject();
        try {
            params.put("email", loginInfo.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, server_url, null, networkSuccessListener(), networkErrorListener());
        queue.add(myReq);

        return v;
    }


    private Response.Listener<JSONObject> networkSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "결과값은 " + response);
                String id = null;
                String name = null;
                String birthday = null;
                String sex = null;
                String father = null;
                String mother = null;

                JSONArray jsonArray = JsonParse.getJsonArrayFromJsonObject(response, "result");

                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        id = jsonObject.getString("baby_id");
                        name = jsonObject.getString("babyname");
                        birthday = jsonObject.getString("birthday");
                        sex = jsonObject.getString("sex");
                        father = jsonObject.getString("father");
                        mother = jsonObject.getString("mother");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    items.add(new Mybabyinfo(id, name, birthday, sex, father, mother));
                }

                mAdapter.notifyDataSetChanged();
            }
        };
    }
    private Response.ErrorListener networkErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}
