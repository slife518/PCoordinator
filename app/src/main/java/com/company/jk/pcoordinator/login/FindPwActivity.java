package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindPwActivity extends MyActivity implements View.OnClickListener {
    final static String TAG = "FindPwActivity";
    Button btn_find_pw;
    EditText et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_pw);
        btn_find_pw = findViewById(R.id.btn_find_pw);
        et_email = findViewById(R.id.input_email);

        btn_find_pw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "버튼클릭" + v.getId());
        switch (v.getId()) {
            case R.id.btn_find_pw:
                confirm_email_send_pw();
                break;
        }
    }

    private void send_password() {
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("email", et_email.getText().toString());

       VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                Log.i(TAG, "패스워드 조회 메일 발송 결과 : " + result);
            }
           @Override
           public void onFailResponse(VolleyError error) {
               Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
           }
        };
        dataTransaction.queryExecute(1, params, "Pc_login/sendMailPw", callback);

        //메일 발송 후 약간이 딜레이가 걸려서 그냥 바로 메세지 보내기 처리
        showToast("초기화된 비밀번호가 메일로 전송되었습니다.");  // 메일이 두번 발송되는 문제가 있어서 일단 메세지를 두번 보여주기로..
        showToast("초기화된 비밀번호가 메일로 전송되었습니다.");
        finish();
    }


    private void confirm_email_send_pw(){
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("email",  et_email.getText().toString());

        VolleyCallback callback_mailid = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                Log.i(TAG, "메일 id 확인 결과 : " + result);

                try {
                    JSONObject jObject = JsonParse.getJsonObjectSingleFromString(result);
                    if(jObject.getString("email") != null) {
                        send_password();
                    }else{
                        showToast(getResources().getString(R.string.nomember));
                    }
                } catch (JSONException e) {
                    showToast(getResources().getString(R.string.nomember));
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };

        dataTransaction.queryExecute(1, params, "Pc_login/confirm_email", callback_mailid);
    }

}