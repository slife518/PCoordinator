package com.company.jk.pcoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.UrlPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

public class ParentsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRecyclerView;
    ArrayList<ParentsInfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    ParentsRecyclerViewAdapter mAdapter;
    ImageView _back;
    ImageButton _btn_addParents;
    String baby_id = null;
    String TAG = "ParentsActivity";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
        intent = getIntent(); //getIntent()로 받을준비
        baby_id = intent.getStringExtra("baby_id");

        _back = (ImageView) findViewById(R.id.btback);
        _btn_addParents = findViewById(R.id.btn_addParents);
        _back.setOnClickListener(this);
        _btn_addParents.setOnClickListener(this);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new ParentsRecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);



        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_parents_info";
        RequestQueue postReqeustQueue = Volley.newRequestQueue(getApplicationContext());
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
                params.put("baby_id", baby_id);
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end

    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        String id = null;
        String name = null;
        String birthday = null;
        String relationship = null;

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getString("email");
                name = rs.getString("nickname");
                birthday = rs.getString("birthday");
                relationship = rs.getString("relationship");
                if (relationship.equals("1")) {
                    relationship = "엄마";
                }else if(relationship.equals("2")) {
                    relationship = "아빠";
                }else if(relationship.equals("3")) {
                    relationship = "친할머니";
                }else if(relationship.equals("4")) {
                    relationship = "친할아버지";
                }else if(relationship.equals("5")) {
                    relationship = "외할머니";
                }else if(relationship.equals("6")) {
                    relationship = "외할아버지";
                }else if(relationship.equals("7")) {
                    relationship = "이모";
                }else if(relationship.equals("8")) {
                    relationship = "고모";
                };
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(new ParentsInfo(id, name, birthday, relationship, baby_id));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        switch (view.getId()) {
            case R.id.btback:
                onBackPressed();
                break;
            case R.id.btn_addParents:
                Intent intent = new Intent(getApplicationContext(), InvitationActivity.class);
                intent.putExtra("baby_id", baby_id);
                startActivityForResult(intent, 2000);
        }
    }

}
