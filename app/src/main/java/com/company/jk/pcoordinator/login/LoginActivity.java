package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.MainActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class LoginActivity extends MyActivity {
    private static final String TAG = "Login";

    StringBuffer sb = new StringBuffer();
    SharedPreferences mPreference;
    Button btn_login;
    Button btn_signup;
    TextView txt_find_pw;
    EditText et_email;
    EditText et_pw;

    CheckBox cb_auto;
    Intent intent;
    LoginInfo loginInfo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        findViewsById();

        loginInfo = LoginInfo.getInstance(this);
        mPreference = getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);
        String id = mPreference.getString(EMAIL, "");
        String pass = mPreference.getString(PASSWORD, "");
        //cb_auto.setChecked(true);
        cb_auto.setChecked(mPreference.getBoolean(ISAUTO_LOGIN, false));

        Log.d(TAG, "아이디는 " + id);
        et_email.setText(id);
        et_pw.setText(pass);
//        if (!mPreference.getBoolean("AutoChecked", false)) {
//            cb_auto.toggle();
//        }

        if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {

            if (cb_auto.isChecked()) {   //자동로그인일 경우
                btn_login.callOnClick();
            }else{
                cb_auto.setChecked(true);
            }
        }

//        printKeyHash();  // 키 해쉬 찾기

//        callbackManager = CallbackManager.Factory.create();
//        loginButton =  findViewById(R.id.login_button);
////        loginButton.setReadPermissions((Arrays.asList(EMAIL, USER_POSTS, USER_BIRTHDAY)));
//        loginButton.setReadPermissions("email");
//
//
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                showToast(loginResult.getAccessToken().getUserId());
//                Log.d(TAG, "로그인 성공 " + loginResult.getAccessToken().getUserId());
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                showToast("취소하였습니다. ");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.d(TAG,exception.toString());
//                // App code
//            }
//        });

    }

    private void printKeyHash() {

            try {
                PackageInfo info = getPackageManager().getPackageInfo("com.company.jk.pcoordinator", PackageManager.GET_SIGNATURES);
                for (Signature signature: info.signatures) {

                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }  catch (NoSuchAlgorithmException e ){
                e.printStackTrace();
        }
    }


//    public static String getKeyHash(final Context context) {
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

    public void OnClickMethod(View v) {      // TODO Auto-generated method stub

        Log.d(TAG, "버튼클릭" + v.getId());
        switch (v.getId()) {
            case R.id.btn_login:
                loginInfo.setEmail(et_email.getText().toString());
                loginInfo.setPassword(et_pw.getText().toString());

                //ID 체크 후 회원이면 예약현황화면으로 이동
                if (NetworkUtil.getConnectivityStatusBoolean(getApplicationContext())) {
                    MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
                    Map<String, String> params = new HashMap<>();
                    params.put("email", et_email.getText().toString());
                    params.put("password", et_pw.getText().toString());

                    VolleyCallback callback = new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result, int method) {
                            if (result == null || result.isEmpty()){
                                showToast(getResources().getString( R.string.message_confirm_id_password));
                            }else {
                                success_login(result);
                            }

                        }
                        @Override
                        public void onFailResponse(VolleyError error) {
                            showToast(getResources().getString(R.string.message_network_problem));
                            Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
                        }
                    };
                    dataTransaction.queryExecute(1, params, "Pc_login/signin", callback);

//                    new HttpTaskSignIn().execute("signin");
                }
                break;

            case R.id.btn_signup:
                intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, 1000);
                break;

            case R.id.find_password:
                Log.d(TAG, "버튼클릭 find_password" + v.getId());
                intent = new Intent(LoginActivity.this, FindPwActivity.class);
                startActivityForResult(intent, 1001);
                break;
        }
    }

    private void findViewsById() {  // 위젯 세팅

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
//        txt_find_id = findViewById(R.id.find_id);
        txt_find_pw = findViewById(R.id.find_password);
        et_email = findViewById(R.id.input_email);
        et_pw = findViewById(R.id.input_password);

        cb_auto = findViewById(R.id.cb_Auto);
    }

    private void success_login(String result) {
        Log.d(TAG, "로그인 결과 : " + result);
        String name = null, babyBirthday = null, babyName = null;
        int babyID = 0;
            //
            JSONObject memberinfo = JsonParse.getJsonObecjtFromString(result, "memberinfo");
            JSONObject babyinfo = JsonParse.getJsonObecjtFromString(result, "babyinfo");

            try {
                name = memberinfo.getString("nickname");
                babyID = memberinfo.getInt("baby_id");
                babyName = babyinfo.getString("babyname");
                babyBirthday = babyinfo.getString("birthday");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //  String birthday = jsonObject.getString("birthday");
            if (name !=  null) {
                SharedPreferences.Editor editor = mPreference.edit();
                editor.putString(EMAIL, et_email.getText().toString());
                editor.putString(PASSWORD, et_pw.getText().toString());
                editor.putString(NAME, name);
                editor.putInt(BABYID, babyID);
                editor.putString(BABYBIRTHDAY, babyBirthday);
                editor.putString(BABYNAME, babyName);
                if (cb_auto.isChecked()) {
                    editor.putBoolean(ISAUTO_LOGIN, cb_auto.isChecked());
                }
                editor.commit();


                //  loginInfo.setLevel(level);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("jsReserved", String.valueOf(sb));
                startActivity(intent);
//                showToast(getResources().getString( R.string.message_Welcome));
                finish();
            }else{
                showToast(getResources().getString( R.string.message_confirm_id_password));
            }
    }

    //  DB 쓰레드 작업
//    class HttpTaskSignIn extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... args) {
//            String name = "";
//            String babyID = "";
//            String babyBirthday = "";
//            String babyName = "";
//            LoginService httpHandler = new LoginService.Builder(Controller, "signin").email(loginInfo.getEmail()).password(loginInfo.getPassword()).build();
//
//            sb = httpHandler.getData();
//
//            try {
////                결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//                JSONObject memberinfo = httpHandler.getNeedJSONObject(sb, "memberinfo");
//                JSONObject babyinfo = httpHandler.getNeedJSONObject(sb, "babyinfo");
//
////                //결과값이 한건의 json인 경우
////                JSONObject jsonObject = new JSONObject(sb.toString());
//
//                name = memberinfo.getString("nickname");
//                babyID = memberinfo.getString("baby_id");
//
//                babyBirthday = babyinfo.getString("birthday");
//                babyName = babyinfo.getString("babyname");
//                //  String level = jsonObject.getString("level");
//                //  String birthday = jsonObject.getString("birthday");
//                if (name != "") {
//                    loginInfo.setName(name);
//                    loginInfo.setBabyID(babyID);
//                    loginInfo.setBabybirthday(babyBirthday);
//                    loginInfo.setBabyname(babyName);
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
//            String toastMessage;
//            if (value != "") {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                toastMessage = getString((R.string.Welcome));
//                intent.putExtra("jsReserved", String.valueOf(sb));
//                startActivity(intent);
//                finish();
//            } else {
//                toastMessage = getString(R.string.Warnning);
//            }
////            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
//            showToast(toastMessage);
//        }
//
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}