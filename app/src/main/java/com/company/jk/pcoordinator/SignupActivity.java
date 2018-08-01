package com.company.jk.pcoordinator;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;


import com.company.jk.pcoordinator.http.HttpHandler2;
import com.company.jk.pcoordinator.http.NetworkUtil;

public class SignupActivity extends AppCompatActivity {
    final static String TAG = "Regist";
    final static String Controller = "Login";
    private static String tcode;

    private JSONObject jObject = null; //group들로 구성된 json
    private JSONObject jsonObject1;
    private StringBuffer sb = new StringBuffer();
    private String toastMessage = " ";

    Intent intent;					//Activity
//    EditText id, pass, rePass, nickname;
    EditText _nameText, _addressText, _emailText, _mobileText, _passwordText,_reEnterPasswordText;
    Button _signupButton;					//activity handler
    TextView _loginLink;
    String sitecd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = (EditText) findViewById(R.id.input_name);
        _addressText = (EditText) findViewById(R.id.input_address);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);

    }

    public void OnClickMethod(View v) {
        try {
            String a = "";
            if (v.getId() == R.id.btn_signup) {
                // 유효성체크
                if (_emailText.getText().toString() == ""
                        || _nameText.getText().toString().equals("")
                        || _passwordText.getText().toString().equals("")
                        || _reEnterPasswordText.getText().toString().equals("")) {
                    Toast.makeText(SignupActivity.this, getString(R.string.CheckUnit),
                            Toast.LENGTH_LONG).show();
                } else if (!(_passwordText.getText().toString().equals(_reEnterPasswordText
                        .getText().toString()))) {
                    Toast.makeText(SignupActivity.this,
                            getString(R.string.CheckRepass), Toast.LENGTH_LONG)
                            .show();
                    //이상없으면
                } else {
                    tcode = "newMember";
                    Log.i(TAG, "OnClickMethod: id.getText().toString  " +_emailText.getText().toString());
                    new HttpTaskSignIn().execute(_emailText.getText().toString(), _nameText.getText().toString(), _passwordText.getText().toString(), _reEnterPasswordText.getText().toString(), _mobileText.getText().toString(), _addressText.getText().toString());
                }

//            }else if(v.getId() == R.id.btDoubleChk){
//                tcode = "chkId";
//                new HttpTaskSignIn().execute(id.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////// 여기서 부터 DB 쓰레드 작업
    //  DB 쓰레드 작업
    class HttpTaskSignIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            String result = "";

            HttpHandler2 httpHandler = null;
            switch (tcode) {
                case "newMember":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).email(args[0]).name(args[1]).password(args[2]).repassword(args[3]).mobile(args[4]).address(args[5]).build();
//					httpHandler.setValue(tcode, args[0],args[1], args[2], args[3]);
                    break;
                case "chkId":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).email(args[0]).build();
//					httpHandler.setValue(tcode, args[0]);
                    break;
            }
            sb = httpHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//				JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
//				result = jsonObject.getString("name");
                jObject = new JSONObject(sb.toString());
                result = jObject.getString("result");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String value) {
            Log.i(TAG, "onPostExecute: tcode) " + tcode);
            Log.i(TAG, "onPostExecute: value "+ value);
            super.onPostExecute(value);
            try {
                switch (tcode) {
                    case("newMember"): //최초
                        if (value.equals("true")){
                            toastMessage = getString(R.string.Welcome);
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                            break;
                        }

                    case("chkId"):
                        if (value.equals("true")){  //가입가능한 id
                            toastMessage = getString(R.string.NewPossible);
                        }else{  //동일한 id 존재
                            toastMessage = getString(R.string.NewImpossible);
                        }
                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            tcode = "";   //초기화
        }
    }
}

