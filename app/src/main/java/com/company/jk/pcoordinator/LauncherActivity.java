package com.company.jk.pcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LauncherActivity extends MyActivity {

    final static String TAG = "LauncherActivity";
    StringBuffer sb = new StringBuffer();
    SharedPreferences mPreference;
    Boolean auto_boolean;
    LoginInfo loginInfo = LoginInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        String id = mPreference.getString("Email", "");
        String pass = mPreference.getString("Password", "");

        auto_boolean = mPreference.getBoolean("AutoChecked", false);

        if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {

            if (auto_boolean) {   //자동로그인일 경우
                loginInfo.setEmail(id);
                loginInfo.setPassword(pass);
                setAutoLogin();   //자동로그인
                //ID 체크 후 회원이면 예약현황화면으로 이동
                if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {
                    auto_login();
                   // new LauncherActivity.HttpTaskSignIn().execute("signin");
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
        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString("Email", loginInfo.getEmail());
        editor.putString("Password", loginInfo.getPassword());
        editor.commit();
    }

    private void auto_login(){

        String server_url = new UrlPath().getUrlPath() + "Pc_login/signin";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveSuccess(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " "+ error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", loginInfo.getEmail());
                params.put("password", loginInfo.getPassword());
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }


    private void saveSuccess(String response) {
        String toastMessage;
        String name;
        String babyID;
        if (!response.isEmpty()) {

            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
                //결과값이 한건의 json인 경우
                JSONObject jsonObject = new JSONObject(response);

                name = jsonObject.getString("nickname");
                babyID = jsonObject.getString("baby_id");

                if (name != "") {
                    loginInfo.setName(name);
                    loginInfo.setBabyID(babyID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            toastMessage = getString((R.string.Welcome));
            intent.putExtra("jsReserved", String.valueOf(sb));
            startActivity(intent);
            finish();
        } else {
            toastMessage = getString(R.string.Warnning);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        showToast(toastMessage);
    }
//    //  DB 쓰레드 작업
//    class HttpTaskSignIn extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... args) {
//            String name = "";
//            String babyID = "";
//            LoginService httpHandler = new LoginService.Builder(Controller, "signin").email(loginInfo.getEmail()).password(loginInfo.getPassword()).build();
//
//            sb = httpHandler.getData();
//
//            try {
//                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
////                JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
//
//                //결과값이 한건의 json인 경우
//                JSONObject jsonObject = new JSONObject(sb.toString());
//
//                name = jsonObject.getString("nickname");
//                babyID = jsonObject.getString("baby_id");
//
//                Log.i(TAG, name);
//                //  String level = jsonObject.getString("level");
//                //  String birthday = jsonObject.getString("birthday");
//                if (name != "") {
//                    loginInfo.setName(name);
//                    loginInfo.setBabyID(babyID);
//                    //  loginInfo.setBirthday(birthday);
//                    //  loginInfo.setLevel(level);
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return name;
//        }
//
//        @Override
//        protected void onPostExecute(String value) {
//            super.onPostExecute(value);
//
//            String toastMessage;
//            if (value != "") {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                toastMessage = getString((R.string.Welcome));
//                intent.putExtra("jsReserved", String.valueOf(sb));
//                startActivity(intent);
//                finish();
//            } else {
//                toastMessage = getString(R.string.Warnning);
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
//        }
//    }

}
