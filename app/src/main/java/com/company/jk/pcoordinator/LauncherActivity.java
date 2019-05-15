package com.company.jk.pcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.company.jk.pcoordinator.login.LoginInfo.APPLICATIONNAME;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYBIRTHDAY;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYID;
import static com.company.jk.pcoordinator.login.LoginInfo.BABYNAME;
import static com.company.jk.pcoordinator.login.LoginInfo.EMAIL;
import static com.company.jk.pcoordinator.login.LoginInfo.ISAUTO_LOGIN;
import static com.company.jk.pcoordinator.login.LoginInfo.NAME;
import static com.company.jk.pcoordinator.login.LoginInfo.PASSWORD;

public class LauncherActivity extends MyActivity {

    final static String TAG = "LauncherActivity";
    StringBuffer sb = new StringBuffer();
    SharedPreferences mPreference;
    Boolean auto_boolean;
    LoginInfo loginInfo;

    private WebView mWebView;
    private WebSettings mWebSettings;

    private  String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        loginInfo = LoginInfo.getInstance(this);
//        mWebView = findViewById(R.id.webview);  //레이어와 연결
//        mWebView.setWebViewClient(new WebViewClient());  // 클릭시 새창 안뜨게
//        mWebSettings = mWebView.getSettings();   //세부세팅등록
//
//        mWebSettings.setJavaScriptEnabled(true);  //자바스크립트
//
//        url = new UrlPath().getUrlPath()  + "Pc_login/preview";
//        Log.d(TAG, url);
//
//        mWebView.loadUrl(url);   //원하는 url 입력
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        mPreference = getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);
        String id = mPreference.getString(EMAIL, "");
        String pass = mPreference.getString(PASSWORD, "");

        auto_boolean = mPreference.getBoolean(ISAUTO_LOGIN, false);

        if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {

            if (auto_boolean) {   //자동로그인일 경우
                loginInfo.setEmail(id);
                loginInfo.setPassword(pass);
                setAutoLogin();   //자동로그인
                //ID 체크 후 회원이면 예약현황화면으로 이동
                if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {
                    auto_login();
                }
            }else{
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }else{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    //자동로그인 구현
    private void setAutoLogin() {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(EMAIL, loginInfo.getEmail());
        editor.putString(PASSWORD, loginInfo.getPassword());
        editor.commit();
    }

    private void auto_login(){
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("password", loginInfo.getPassword());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                if (result == null || result.isEmpty()){
                    fail_login();
                    showToast(getResources().getString( R.string.message_confirm_id_password));
                }else {
                    success_login(result);
                }
            }

            @Override
            public void onFailResponse(VolleyError error) {
                fail_login();
            }
        };
        dataTransaction.queryExecute(1, params, "Pc_login/signin", callback);
    }

    private void fail_login(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void success_login(String response) {

        String name = null,  babyBirthday  = null, babyName = null;
        int babyID = 0;

        //
        JSONObject memberinfo = JsonParse.getJsonObecjtFromString(response, "memberinfo");
        JSONObject babyinfo = JsonParse.getJsonObecjtFromString(response, "babyinfo");

        try {
            name = memberinfo.getString("nickname");
            babyID = memberinfo.getInt("baby_id");

            if(babyID != 0) {   // 매핑된 아기정보가 있으면
                babyName = babyinfo.getString("babyname");
                babyBirthday = babyinfo.getString("birthday");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  String birthday = jsonObject.getString("birthday");
        if (name != null) {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putString(NAME, name);
            editor.putInt(BABYID, babyID);
            editor.putString(BABYBIRTHDAY, babyBirthday);
            editor.putString(BABYNAME, babyName);
            editor.commit();

            //  loginInfo.setLevel(level);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("jsReserved", String.valueOf(sb));
            startActivity(intent);
//            showToast(getResources().getString( R.string.message_Welcome));
            finish();
        }else{
            showToast(getResources().getString( R.string.message_confirm_id_password));
            fail_login();
        }

    }
}
