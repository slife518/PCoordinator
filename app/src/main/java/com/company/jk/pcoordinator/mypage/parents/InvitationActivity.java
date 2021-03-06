package com.company.jk.pcoordinator.mypage.parents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InvitationActivity extends MyActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    RadioGroup _radioGroup;
    RadioButton _btn_email, _btn_phone;
    Button _btn_invite;
//    ImageButton _btn_search_person;
    ImageView _iv_profile;
    EditText _find_user;
    TextView _name;
    final String TAG = "InvatationActivity";
    Intent intent;
    int _baby_id;
    String _email;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.invite);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        intent = getIntent(); //getIntent()로 받을준비
//        _baby_id = Integer.parseInt(intent.getStringExtra("baby_id"));
        if(intent.getStringExtra("baby_id") != null){
            _baby_id = Integer.parseInt(intent.getStringExtra("baby_id"));
        }else {
            LoginInfo loginInfo = LoginInfo.getInstance(this);
            _baby_id = loginInfo.getBabyID();
        }

        _radioGroup = findViewById(R.id.radioGroup);
        _btn_email =  findViewById(R.id.btn_email);
        _btn_phone =  findViewById(R.id.btn_tel);


//        _btn_search_person = (ImageButton) findViewById(R.id.search_person);

        _btn_invite =  findViewById(R.id.btn_invite);
        _iv_profile =  findViewById(R.id.iv_profile);
        _name = findViewById(R.id.tv_name);
        _find_user = findViewById(R.id.et_find_user);
        _find_user.setOnEditorActionListener(this);
        PhoneNumberUtils.formatNumber(_find_user.getText().toString());
        _find_user.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

//        _btn_search_person.setOnClickListener(this);
        _btn_invite.setOnClickListener(this);
        _radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        Log.d(TAG, String.valueOf(i));
        _find_user.setText("");
        if(_btn_email.isChecked()){
            _find_user.setHint(R.string.letsEmail );
            _find_user.setInputType(1);   // TEXT 타입
//            _find_user.addTextChangedListener(this);
        }else {
            _find_user.setHint(R.string.letsTel );
            _find_user.setInputType(3);  //PHONE 타입
        }
    }

    @Override
    public void onClick(View view) {
//        if(view ==_btn_search_person) {
//            get_person_info();
//            closeKeyboard();
//        }else
            if(view == _btn_invite){
            invite_person();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            get_person_info();
            closeKeyboard();
            return true;
        }
        return false;
    }

    private void get_person_info(){
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_login/find_user";
        RequestQueue postRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                    Log.d(TAG,  "email은 "+_find_user.getText().toString());
                }else {
                    params.put("tel", _find_user.getText().toString());
                    Log.d(TAG,  "연락처는 "+_find_user.getText().toString());
                }
                return params;
            }
        };
        Log.d(TAG, "1");
        postRequestQueue.add(postStringRequest);

        //data binding end

    }

    private void responseReceiveData(String response) {
        Log.d(TAG, "결과값은 " + response);

        try {
            JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");

            String email = rs.getString("email");
            String name = rs.getString("nickname");
            if(email != null){
                String imgUrl = new UrlPath().getUrlMemberImg() + email + ".jpg";  //확장자 대소문자 구별함.
                Log.d(TAG, imgUrl);
                Picasso.with(getApplicationContext()).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(_iv_profile);  //image가 reload 되도록 하기 위하여 필요함

                _iv_profile.setVisibility(View.VISIBLE);
            }else {
                _iv_profile.setVisibility(View.INVISIBLE);
            }

            _name.setText(name);
            _email = email;
            _btn_invite.setVisibility(View.VISIBLE);


        } catch (JSONException e) {
//            _name.setVisibility(View.VISIBLE);
            _name.setText("해당회원을 찾을 수 없습니다.");
            _btn_invite.setVisibility(View.INVISIBLE);

            e.printStackTrace();
        }
    }

    private void invite_person(){
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/invite_user";
        RequestQueue postRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                response_invite_person(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", _email);
                params.put("baby_id",String.valueOf(_baby_id));

                Log.d(TAG, "email는 " + _email);
                Log.d(TAG, "baby_id는 " +_baby_id);
                return params;
            }
        };
        Log.d(TAG, "1");
        postRequestQueue.add(postStringRequest);
    }

    private void  response_invite_person(String response){
        Log.d(TAG, "결과값은 " + response);
        if(response.equals("1")){
//            onBackPressed();
//            finish();
//            Intent intent = new Intent(getApplicationContext() ,ParentsActivity.class);
//            intent.putExtra("baby_id", _baby_id);
//            startActivityForResult(intent, 1000);

            Intent returnIntent =  new Intent();
            returnIntent.putExtra("email",_email);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        }else{
            _name.setText(R.string.message_duplicate_user);
        }
    }
    private  void  closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
