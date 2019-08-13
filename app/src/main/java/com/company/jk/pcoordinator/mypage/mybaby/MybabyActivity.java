package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybabyActivity extends MyActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    RecyclerView mRecyclerView;
    ArrayList<Mybabyinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    Spinner mSpinner;
    TextView tv_requestbaby;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    LoginInfo loginInfo;

    String TAG = "MybabyFragment";

    List<Integer> target_baby_list_value = new ArrayList<Integer>() ;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybaby);

        loginInfo = LoginInfo.getInstance(this);
        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        tv_requestbaby = findViewById(R.id.tv_requestbaby);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.mybabyinfo));
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
        tv_requestbaby.setOnClickListener(this);
    }
    public void get_baby_data(){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);

                switch (method){
                    case 2:  //get_baby_data
                        responseSuccess(result);
                        break;
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        transaction.queryExecute(2, params, "Pc_baby/get_baby_info", callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_baby_data();

        //스피너 설정 시작
        mSpinner = (Spinner)findViewById(R.id.spinner_main_target);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_mybaby, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.iconColorDarkBackground), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final int selectedVal = target_baby_list_value.get(mSpinner.getSelectedItemPosition());
        Log.d(TAG, "선택된 아기의 아이디는 " + selectedVal);

        //data binding start
        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("baby_id", String.valueOf(selectedVal));

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);

                switch (method){
                    case 1 :
                        Mybabyinfo mybabyinfo = items.get(mSpinner.getSelectedItemPosition());
                        loginInfo.setBabyname(mybabyinfo.getName());
                        loginInfo.setBabyID(mybabyinfo.getId());
                        loginInfo.setBabybirthday(mybabyinfo.getBirthday());
                }
            }

            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };

        transaction.queryExecute(1, params, "Pc_baby/set_main_baby", callback);  //결과값에 상관없이 진행하는 게 좀 문제 있어 보인다.

    }




    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.d(TAG, "선택된 아기의 아이디는 없습니다. ");
    }

    private void responseSuccess(String response) {
        Log.d(TAG, "결과값은 " + response);
        int id = 0;
        String name = null;
        String birthday = null;
        String sex = null;
        String father = null;
        String mother = null;
        target_baby_list_value.clear();
        items.clear();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(this);

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getInt("baby_id");
                name = rs.getString("babyname");
                arrayAdapter.add(name);   // 타겟 아기 리스트
                target_baby_list_value.add(id);   // 타겟 아기 리스트

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

//        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        JSONObject rs = JsonParse.getJsonObjectFromString(response, "main_baby");
        try {
            int baby_id = Integer.parseInt(rs.getString("baby_id"));
            mSpinner.setSelection(target_baby_list_value.indexOf(baby_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();



    }


    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, MybabyDetailActivity.class);
                intent.putExtra("email",loginInfo.getEmail() );
                startActivityForResult(intent, 300);

                return true;

            default:
                onBackPressed();

        }
        return true;

    }


    ;
//
//    private static Fragment newInstance(String param1){
//        MybabyDetailFragment fragment = new MybabyDetailFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("email", param1);
//        fragment.setArguments(bundle);
//        return  fragment;
//    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "클릭 요청 ");
        switch (view.getId()) {
            case R.id.tv_requestbaby:
                Log.d(TAG, "클릭 요청2");
                Intent intent = new Intent(this, RequestBabyActivity.class);
                startActivityForResult(intent, 300);
                break;
        }
    }



}
