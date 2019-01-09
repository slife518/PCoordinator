package com.company.jk.pcoordinator.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.MainActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.home.HomeFragment;
import com.company.jk.pcoordinator.http.NetworkUtil;
import com.company.jk.pcoordinator.http.UrlPath;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FindPwActivity extends MyActivity implements View.OnClickListener{
    final static String TAG = "FindPwActivity";
    final static String Controller = "Pc_login";
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
                send_password();
                break;
        }
    }

    private void send_password(){
//        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext(), "Pc_login/send_mail_pw");
//        Map<String, String> params = new HashMap<>();
//        params.put("email", et_email.getText().toString());
//       if(dataTransaction.modify(params)){
//           showToast("초기화된 비밀번호가 메일로 전송되었습니다.");
//           finish();
//       }
//
        String server_url = new UrlPath().getUrlPath() + "Pc_login/send_mail_pw";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                sendPWSuccess(response);    // 결과값 받아와서 처리하는 부분
                Toast.makeText(getApplicationContext(), "초기화된 비밀번호가 메일로 전송되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG , "메일전송시 에러발생.. 원인은 " + error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", et_email.getText().toString());
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void sendPWSuccess(String response){  //성공하면 결과메세지 보이기

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.confirm)
                .setMessage(R.string.confirm)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}