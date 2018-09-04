package com.company.jk.pcoordinator.mypage.mybaby;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyVolley;
import com.company.jk.pcoordinator.http.UrlPath;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MybabyDetailFragment extends Fragment implements View.OnClickListener {


    static final String TAG = "MybabyDetailFragment";
    ImageView _btn_back, _profile;
    Button _btn_save;
    View v;
    EditText _name, _sex, _birthday, _father, _mother, _owner;
    Context mContext;
    String email, baby_id;
    UrlPath urlPath = new UrlPath();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString("email");
            baby_id = getArguments().getString("baby_id");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby_detail, container, false);
        mContext = v.getContext();

        findViewsById(v);
        _btn_back.setOnClickListener(this);
        _btn_save.setOnClickListener(this);

        Log.i(TAG, "이메일은 " + email + " id는 " + baby_id);
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info_detail";
        RequestQueue postReqeustQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("baby_id", baby_id);
                return params;
            }
        };
        postReqeustQueue.add(postStringRequest);

        return v;
    }


    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);

        JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");

        try {
            String name = rs.getString("babyname");
            String id = rs.getString("baby_id");
            String birthday = rs.getString("birthday");
            String sex = rs.getString("sex");


            String imgUrl = urlPath.getUrlBabyImg() + id + ".JPG";  //확장자 대소문자 구별함.
            Log.i(TAG, imgUrl);
            Picasso.with(mContext).load(imgUrl).into(_profile);
            _name.setText(name);
            _birthday.setText(birthday);
            if (sex.equals("1")) {
                _sex.setText("남자");
            } else {
                _sex.setText("여자");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById(View v) {  // 위젯 세팅

        _btn_save = v.findViewById(R.id.btn_save);
        _profile = v.findViewById(R.id.iv_profile);
        _name = v.findViewById(R.id.et_name);
        _sex = v.findViewById(R.id.et_sex);
        _birthday = v.findViewById(R.id.et_birthday);
        _btn_back = v.findViewById(R.id.btback);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btback:
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                MybabyFragment myFragment = new MybabyFragment();
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.btn_save:
                    save_data();
                break;
        }
    }

    private  void save_data(){

//        String server_url = new UrlPath().getUrlPath() + "Pc_baby/update";
//        RequestQueue postReqeustQueue = Volley.newRequestQueue(mContext);
//        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                responseSuccess(response);    // 결과값 받아와서 처리하는 부분
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.getLocalizedMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("email", email);
//                params.put("baby_id", baby_id);
//                return params;
//            }
//        };
//        postReqeustQueue.add(postStringRequest);



        String server_url = new UrlPath().getUrlPath() + "Pc_baby/update";
        //request parameters
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("baby_id", baby_id);
        params.put("babyname", _name.toString());
        params.put("birthday", _birthday.toString());
        params.put("sex", _sex.toString());


        // Inflate the layout for this fragment
        final RequestQueue queue = MyVolley.getInstance(getActivity()).getRequestQueue();
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST, server_url, new JSONObject(params), networkSuccessListener(), networkErrorListener());
        queue.add(myReq);
    }

    private Response.Listener<JSONObject> networkSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "결과값은 " + response);
                String from_server = null;
                try {
                    from_server = response.getString("test");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    private Response.ErrorListener networkErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage());
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        };
    }


}
