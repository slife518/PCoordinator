package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.HttpHandler2;

public class SignupActivity extends AppCompatActivity {
    private static final int ADDRESS_REQUEST = 1777;
    final static String TAG = "Regist";
    final static String Controller = "Pc_Login";
    private static String tcode;

    private JSONObject jObject = null; //group들로 구성된 json
    private JSONObject jsonObject1;
    private StringBuffer sb = new StringBuffer();
    private String toastMessage = " ";

    Intent intent;					//Activity
//    EditText id, pass, rePass, nickname;
    EditText _nameText, _emailText, _mobileText, _passwordText,_reEnterPasswordText, _addressDetailText;
    TextInputLayout _address_detail;
    Button _signupButton;					//activity handler
    TextView _loginLink, _addressText;
    String sitecd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = (EditText) findViewById(R.id.input_name);
        _addressText = (TextView) findViewById(R.id.tv_address);
        _addressDetailText = (EditText) findViewById(R.id.et_address_detail);
        _address_detail = (TextInputLayout) findViewById(R.id.input_address_detail);
        _emailText = (EditText) findViewById(R.id.input_email);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);


        _addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!_addressText.getText().toString().isEmpty()){   //주소에 값이 있으면 상세주소칸 표시
                    _address_detail.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                _address_detail.setVisibility(View.VISIBLE);
            }
        });
    }

    public void OnClickMethod(View v) {
        switch (v.getId()){
            case R.id.btn_findAddress:
                intent = new Intent(getApplicationContext(), AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_signup:
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

                break;

            case R.id.link_login :
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class );
                getApplicationContext().startActivity(intent);
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST) {
            switch (resultCode) {
                case 1:
                    String result = data.getStringExtra("result");
                    Log.i(TAG, "result는 " + result);
                    _addressText.setText(result);
                    break;
                default:
                    break;
            }
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

