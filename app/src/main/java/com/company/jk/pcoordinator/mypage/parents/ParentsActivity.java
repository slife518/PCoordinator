package com.company.jk.pcoordinator.mypage.parents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentsActivity extends MyActivity{

    RecyclerView mRecyclerView;
    ArrayList<ParentsInfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    ParentsRecyclerViewAdapter mAdapter;
    //Button _btn_addParents;

//    int baby_id = 0;
    Toolbar myToolbar;
    String TAG = "ParentsActivity";
    Intent intent;
    LoginInfo loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
        loginInfo = LoginInfo.getInstance(this);
        intent = getIntent(); //getIntent()로 받을준비
//        if(intent.getStringExtra("baby_id") != null){
//            baby_id = Integer.parseInt(intent.getStringExtra("baby_id"));
//        }

        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(R.string.parentlist);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

//        if(baby_id == 0){    //나의 페이지에서 바로 들어온것이면
//            baby_id = loginInfo.getBabyID();
//        }


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView =  findViewById(R.id.listView_main);
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
                params.put("baby_id", String.valueOf(loginInfo.getBabyID()));
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);
        //data binding end

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_parents, menu);
        return true;
    }

    private void responseSuccess(String response) {
        Log.d(TAG, "결과값은 " + response);
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
                relationship = rs.getString("relation");
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
                }else if(relationship.equals("")) {
                relationship = "미상";
            };
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(new ParentsInfo(id, name, birthday, relationship, loginInfo.getBabyID()));
        }

        mAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onClick(View view) {
////        AppCompatActivity activity = (AppCompatActivity) view.getContext();
//
//        switch (view.getId()) {
//            case R.id.action_add:
//                Intent intent = new Intent(getApplicationContext(), InvitationActivity.class);
//                intent.putExtra("baby_id", loginInfo.getBabyID());
//                startActivityForResult(intent, 2000);
//        }
//    }


    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getApplicationContext(), InvitationActivity.class);
                intent.putExtra("baby_id", loginInfo.getBabyID());
                startActivityForResult(intent, 2000);
                return true;

            default:
                onBackPressed();

        }
        return true;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "재시작합니다."+ requestCode);
        if (requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK){
//                String result=data.getStringExtra("email");
                Log.d(TAG, "재시작합니다." + requestCode);
               recreate();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                Log.d(TAG, "취소되었습니다.");
            }
        }
    }

}
