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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.Spinner;

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
import java.util.List;
import java.util.Map;


public class MybabyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    RecyclerView mRecyclerView;
    ArrayList<Mybabyinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    Spinner mSpinner;
    ImageView _back;
    Button _btn_add;
    View v;
    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    String TAG = "MybabyFragment";

    List<String> target_baby_list_text = new ArrayList<String>();
    List<String> target_baby_list_value = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby, container, false);
        mContext = v.getContext();

        _back = (ImageView) v.findViewById(R.id.btn_exit);
        _back.setOnClickListener(this);

        _btn_add = v.findViewById(R.id.btn_add);
        _btn_add.setOnClickListener(this);



//        Spinner spinner=(Spinner)findViewById(R.id.spinner);
//        String[] datas=getResources().getStringArray(R.array.spinner_array);
//        ArrayAdapter<String> aa=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, datas);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(aa);


//        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                Log.i(TAG, "선택된 아기의 아이디는~ " + position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.listView_main);
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




        //스피너 설정 시작
        mSpinner = (Spinner)v.findViewById(R.id.spinner_main_target);
        String[] datas=getResources().getStringArray(R.array.spinner_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, target_baby_list_text);

//        ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, datas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(this);




        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        String item = mSpinner.getSelectedItem().toString();
//        String selectedVal = target_baby_list_value.get(mSpinner.getSelectedItemPosition());

        Log.i(TAG, "선택된 아기의 아이디는 ");
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

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getString("baby_id");
                name = rs.getString("babyname");
                target_baby_list_text.add(name);   // 타겟 아기 리스트
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

        mAdapter.notifyDataSetChanged();



    }

    ;

    private static Fragment newInstance(String param1){
        MybabyDetailFragment fragment = new MybabyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("email", param1);
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public void onClick(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        switch (view.getId()) {
            case R.id.btn_exit:
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.btn_add:
//                MybabyDetailFragment mybabyDetailFragment = new MybabyDetailFragment();
                //왼쪽에서 오른쪽 슬라이드
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, mybabyDetailFragment).addToBackStack(null).commit();
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail())).addToBackStack(null).commit();

        }
    }


    private  void getBabyList(){

    }
}
