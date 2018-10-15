package com.company.jk.pcoordinator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.UrlPath;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InvitationActivity extends AppCompatActivity implements View.OnClickListener{

    RadioButton _btn_email, _btn_phone;
    ImageButton _btn_search_person;
    ImageView _iv_profile;
    EditText _find_user;
    TextView _name;
    final String TAG = "InvatationActivity";
    Intent intent;
    String baby_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);


        intent = getIntent(); //getIntent()로 받을준비
        baby_id = intent.getStringExtra("baby_id");

        _btn_email = (RadioButton) findViewById(R.id.radioButton);
        _btn_phone = (RadioButton) findViewById(R.id.radioButton2);
        _btn_search_person = (ImageButton) findViewById(R.id.search_person);
        _iv_profile = (ImageView) findViewById(R.id.iv_profile);
        _name = (TextView)findViewById(R.id.tv_name);
        _find_user = (EditText)findViewById(R.id.et_find_user);
        _btn_search_person.setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, String.valueOf(checkedId));
                if(_btn_email.isChecked()){
                    _find_user.setText(R.string.letsEmail );
                }else {
                    _find_user.setText(R.string.letsTel );
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view ==_btn_search_person){
            get_person_info();
        }
    }

    private void get_person_info(){
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_Login/find_user";
        RequestQueue postRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "2");
                responseReceiveData(response);    // 결과값 받아와서 처리하는 부분
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
                if(_btn_email.isChecked()){
                    params.put("email", _find_user.getText().toString());
                }else {
                    params.put("tel", _find_user.getText().toString());
                }


                return params;
            }
        };
        Log.i(TAG, "1");
        postRequestQueue.add(postStringRequest);

        //data binding end

    }

    private void responseReceiveData(String response) {
        Log.i(TAG, "결과값은 " + response);

        JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");

        try {
            String name = rs.getString("babyname");
            String id = rs.getString("email");
         //   String birthday = rs.getString("birthday");
//            _birthday.setText(birthday.substring(0, 4)+"년"+birthday.substring(4, 6)+"월"+birthday.substring(6, 8)+"일");
          //  _birthday.setText(birthday);

          //  String sex = rs.getString("sex");

            String imgUrl = new UrlPath().getUrlBabyImg() + id + ".jpg";  //확장자 대소문자 구별함.
            Log.i(TAG, imgUrl);
            Picasso.with(getApplicationContext()).load(imgUrl).into(_iv_profile);
            _name.setText(name);
           // _birthday.setText(birthday);
          //  if (sex.equals("1")) {
//                _boy.setChecked(true);
//            } else {
//                _girl.setChecked(true);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
