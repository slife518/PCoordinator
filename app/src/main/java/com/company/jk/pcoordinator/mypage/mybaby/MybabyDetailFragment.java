package com.company.jk.pcoordinator.mypage.mybaby;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MybabyDetailFragment extends Fragment implements View.OnClickListener {


    static final String TAG = "MybabyDetailFragment";
    ImageView _btn_back, _profile;
    Button _btn_save;
    RadioButton _boy, _girl;
    View v;
    EditText _name, _sex, _father, _mother, _owner;
    TextView _birthday;
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
        _birthday.setOnClickListener(this);


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
        }){
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
//            _birthday.setText(birthday.substring(0, 4)+"년"+birthday.substring(4, 6)+"월"+birthday.substring(6, 8)+"일");
            _birthday.setText(birthday);

            String sex = rs.getString("sex");

            String imgUrl = urlPath.getUrlBabyImg() + id + ".JPG";  //확장자 대소문자 구별함.
            Log.i(TAG, imgUrl);
            Picasso.with(mContext).load(imgUrl).into(_profile);
            _name.setText(name);
            _birthday.setText(birthday);
            if (sex.equals("1")) {
                _boy.setChecked(true);
            } else {
                _girl.setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findViewsById(View v) {  // 위젯 세팅

        _btn_save = v.findViewById(R.id.btn_save);
        _profile = v.findViewById(R.id.iv_profile);
        _name = v.findViewById(R.id.et_name);
        _boy = v.findViewById(R.id.rd_boy);
        _girl = v.findViewById(R.id.rd_girl);
        _birthday = v.findViewById(R.id.tv_birthday);
        _btn_back = v.findViewById(R.id.btback);
    }

        @Override
    public void onClick(View v) {
        if(v==_btn_back){
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            MybabyFragment myFragment = new MybabyFragment();
            activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
        }else if(v==_btn_save){
            save_data();
        }else if(v==_birthday){
            Calendar c=Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog=new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    _birthday.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                }
            },year, month, day);

            datePickerDialog.show();
        }
    }

    private  void save_data(){

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/update";
        //request parameters
        Map<String, String> params = new HashMap<>();
        params.put("owner", email);
        params.put("baby_id", baby_id);
        params.put("babyname", _name.getText().toString());
//        Log.i(TAG, _birthday.getText().toString());
//        Log.i(TAG, _birthday.getText().toString().substring(0, 4)+_birthday.getText().toString().substring(5, 7)+_birthday.getText().toString().substring(8, 10));
        params.put("birthday", _birthday.getText().toString());
        if(_boy.isChecked()){
            params.put("sex", "1");
        }else if(_girl.isChecked()){
            params.put("sex", "2");
        }


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
                Boolean result = null;
                try {
                    result = response.getBoolean("result");
                    if(result){
                        showToast( getString(R.string.save));
                    }else {
                        showToast( getString(R.string.savefail));
                    }
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
                showToast("Network Error");
            }
        };
    }



    private void showToast(String message){
        Toast toast=Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}
