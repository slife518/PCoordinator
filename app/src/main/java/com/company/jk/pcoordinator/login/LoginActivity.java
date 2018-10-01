package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.MainActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.http.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    final static String TAG = "Login";
    final static String Controller = "Pc_Login";
    StringBuffer sb = new StringBuffer();
    SharedPreferences mPreference;
    Button btn;
    EditText et_email;
    EditText et_pw;
    TextView tv_signupLink;
    CheckBox cb_auto;
    Intent intent;
    LoginInfo loginInfo = LoginInfo.getInstance();
    private String toastMessage = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        findViewsById();

        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        String id = mPreference.getString("Email", "");
        String pass = mPreference.getString("Password", "");
        //cb_auto.setChecked(true);
        cb_auto.setChecked(mPreference.getBoolean("AutoChecked", true));

        Log.i(TAG, "아이디는 " + id);
        et_email.setText(id);
        et_pw.setText(pass);
        if (!mPreference.getBoolean("AutoChecked", false)) {
            cb_auto.toggle();
        }

        if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {

            if (cb_auto.isChecked()) {   //자동로그인일 경우
                btn.callOnClick();
            }
        }

    }

    public void OnClickMethod(View v) {      // TODO Auto-generated method stub

        Log.i(TAG, "버튼클릭" + v.getId());
        switch (v.getId()) {
            case R.id.btn_login:
                loginInfo.setEmail(et_email.getText().toString());
                loginInfo.setPassword(et_pw.getText().toString());
                setAutoLogin();   //자동로그인
                //ID 체크 후 회원이면 예약현황화면으로 이동
                if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {
                    new HttpTaskSignIn().execute("signin");
                }
                break;

            case R.id.link_signup:
                intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void findViewsById() {  // 위젯 세팅

        btn = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.input_email);
        et_pw = findViewById(R.id.input_password);
        tv_signupLink = findViewById(R.id.link_signup);
        cb_auto = findViewById(R.id.cb_Auto);
    }

    //자동로그인 구현
    private void setAutoLogin() {
        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreference.edit();
        if (cb_auto.isChecked()) {
            editor.putString("Email", loginInfo.getEmail());
            editor.putString("Password", loginInfo.getPassword());
//            editor.putString("SiteCd", loginInfo.getSite());
        }
        editor.putBoolean("AutoChecked", cb_auto.isChecked());
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
            }
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }


    }
}