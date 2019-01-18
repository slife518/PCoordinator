package com.company.jk.pcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("password", loginInfo.getPassword());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                success_login(result);
            }
        };
        dataTransaction.queryExecute(1, params, "Pc_login/signin", callback);
    }


    private void success_login(String response) {

        String name = "", babyID = "", babyBirthday = "", babyName = "";

        //
        JSONObject memberinfo = JsonParse.getJsonObecjtFromString(response, "memberinfo");
        JSONObject babyinfo = JsonParse.getJsonObecjtFromString(response, "babyinfo");

        try {
            name = memberinfo.getString("nickname");
            babyID = memberinfo.getString("baby_id");
            babyName = babyinfo.getString("babyname");
            babyBirthday = babyinfo.getString("birthday");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  String birthday = jsonObject.getString("birthday");
        if (name != "") {
            loginInfo.setName(name);
            loginInfo.setBabyID(babyID);
            loginInfo.setBabybirthday(babyBirthday);
            loginInfo.setBabyname(babyName);
            //  loginInfo.setLevel(level);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("jsReserved", String.valueOf(sb));
            startActivity(intent);
            showToast(getResources().getString( R.string.Welcome));
            finish();
        }else{
            showToast(getResources().getString( R.string.Warnning));
        }

    }
}
