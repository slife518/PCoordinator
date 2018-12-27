package com.company.jk.pcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.login.LoginService;

import org.json.JSONObject;

public class LauncherActivity extends AppCompatActivity {

    final static String TAG = "LauncherActivity";
    final static String Controller = "Pc_login";
    StringBuffer sb = new StringBuffer();
    SharedPreferences mPreference;
    Boolean auto_boolean;
    LoginInfo loginInfo = LoginInfo.getInstance();
    private String toastMessage = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        String id = mPreference.getString("Email", "");
        String pass = mPreference.getString("Password", "");

        auto_boolean = mPreference.getBoolean("AutoChecked", true);

        if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {

            if (auto_boolean) {   //자동로그인일 경우
                loginInfo.setEmail(id);
                loginInfo.setPassword(pass);
                setAutoLogin();   //자동로그인
                //ID 체크 후 회원이면 예약현황화면으로 이동
                if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {
                    new LauncherActivity.HttpTaskSignIn().execute("signin");
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


    //  DB 쓰레드 작업
    class HttpTaskSignIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            String name = "";
            String babyID = "";
            LoginService httpHandler = new LoginService.Builder(Controller, "signin").email(loginInfo.getEmail()).password(loginInfo.getPassword()).build();

            sb = httpHandler.getData();

            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//                JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");

                //결과값이 한건의 json인 경우
                JSONObject jsonObject = new JSONObject(sb.toString());

                name = jsonObject.getString("nickname");
                babyID = jsonObject.getString("baby_id");

                Log.i(TAG, name);
                //  String level = jsonObject.getString("level");
                //  String birthday = jsonObject.getString("birthday");
                if (name != "") {
                    loginInfo.setName(name);
                    loginInfo.setBabyID(babyID);
                    //  loginInfo.setBirthday(birthday);
                    //  loginInfo.setLevel(level);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return name;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            if (value != "") {
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
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }


    }

}
