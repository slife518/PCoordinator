package com.company.jk.pcoordinator.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyActivity extends AppCompatActivity  implements EditText.OnEditorActionListener{


    LoginInfo loginInfo = LoginInfo.getInstance();
    SharedPreferences mPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtil.getConnectivityStatusBoolean(this);
        if(loginInfo.getEmail()==null || loginInfo.equals("")){
            getLoginInfo();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                super.onBackPressed();
                return true;
        }


    }

    protected void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if(actionId== EditorInfo.IME_ACTION_DONE){
            //Clear focus here from edittext
            textView.clearFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
        return false;
    }


    private void getLoginInfo(){

        showToast("세션 종료로 재로그인 됨.");
        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
        String id = mPreference.getString("Email", "");
        String pass = mPreference.getString("Password", "");

        loginInfo.setEmail(id);
        loginInfo.setPassword(pass);

        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("password", loginInfo.getPassword());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                if (result == null || result.isEmpty()){

                }else {
                    success_login(result);
                }
            }

            @Override
            public void onFailResponse(VolleyError error) {

            }
        };
        dataTransaction.queryExecute(1, params, "Pc_login/signin", callback);
    }



    private void success_login(String response){


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
        loginInfo.setName(name);
        loginInfo.setBabyID(babyID);
        loginInfo.setBabybirthday(babyBirthday);
        loginInfo.setBabyname(babyName);
    }

}
