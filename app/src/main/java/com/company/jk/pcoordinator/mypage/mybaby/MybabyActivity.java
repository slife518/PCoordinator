package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybabyActivity extends MyActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    RecyclerView mRecyclerView;
    ArrayList<Mybabyinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    Spinner mSpinner;
    Button _btn_add;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    LoginInfo loginInfo = LoginInfo.getInstance();

    String TAG = "MybabyFragment";

    List<String> target_baby_list_value = new ArrayList<String>() ;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybaby);

        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        _btn_add = findViewById(R.id.btn_add);
        _btn_add.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
}
    public void get_baby_data(){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        transaction.queryExecute(2, params, "Pc_baby/get_baby_info", callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "재시작 onResume");
        get_baby_data();

        //스피너 설정 시작
        mSpinner = (Spinner)findViewById(R.id.spinner_main_target);

    }

    VolleyCallback callback = new VolleyCallback() {
        @Override
        public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

            Log.i(TAG, "onSuccessResponse 결과값은" + result + method);

            switch (method){
                case 1 :  //onItemSelected
//                    String baby_id = JsonParse.getResultFromJsonString(result);
//                    loginInfo.setBabyID(baby_id);
//                    try {
////                        loginInfo.setBabyname(JsonParse.getJsonObecjtFromString(result, "result").getString("babyname"));
////                        loginInfo.setBabyID(JsonParse.getJsonObecjtFromString(result, "result").getString("baby_id"));
////                        loginInfo.setBabybirthday(JsonParse.getJsonObecjtFromString(result, "result").getString("birthday"));
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    Mybabyinfo mybabyinfo = items.get(mSpinner.getSelectedItemPosition());
                    loginInfo.setBabyname(mybabyinfo.getName());
                    loginInfo.setBabyID(mybabyinfo.getId());
                    loginInfo.setBabybirthday(mybabyinfo.getBirthday());

                    break;
                case 2:  //get_baby_data
                    responseSuccess(result);
                    break;
            }

        }

        @Override
        public void onFailResponse(VolleyError error) {
            Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final String selectedVal = target_baby_list_value.get(mSpinner.getSelectedItemPosition());
        Log.i(TAG, "선택된 아기의 아이디는 " + selectedVal);

        //data binding start
        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("baby_id", selectedVal);
        transaction.queryExecute(1, params, "Pc_baby/set_main_baby", callback);  //결과값에 상관없이 진행하는 게 좀 문제 있어 보인다.

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Log.i(TAG, "선택된 아기의 아이디는 없습니다. ");
    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        String id = null;
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
                id = rs.getString("baby_id");
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
            String baby_id = rs.getString("baby_id");
            mSpinner.setSelection(target_baby_list_value.indexOf(baby_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        arrayAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();



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
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        switch (view.getId()) {
//            case R.id.btn_exit:
//                MypageFragment myFragment = new MypageFragment();
//                //왼쪽에서 오른쪽 슬라이드
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
//                break;
            case R.id.btn_add:
//                MybabyDetailFragment mybabyDetailFragment = new MybabyDetailFragment();
                //왼쪽에서 오른쪽 슬라이드
                Intent intent = new Intent(this, MybabyDetailActivity.class);
                intent.putExtra("email",loginInfo.getEmail() );
                startActivityForResult(intent, 300);


//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, mybabyDetailFragment).addToBackStack(null).commit();
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail())).addToBackStack(null).commit();

        }
    }

}
