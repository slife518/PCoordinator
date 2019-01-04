package com.company.jk.pcoordinator.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.http.UrlPath;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends MyActivity {
    final static String TAG = "Regist";
//    final static String Controller = "Pc_Login";
    private static final int ADDRESS_REQUEST = 1777;
//    private static String tcode;
    Intent intent;                    //Activity
    EditText _nameText, _emailText, _mobileText, _passwordText; //, _addressDetailText,_reEnterPasswordText;
//    TextInputLayout _address_detail;
    Button _signupButton;                    //activity handler
    TextView _loginLink, _addressText;
//    private JSONObject jObject = null; //group들로 구성된 json
//    private JSONObject jsonObject1;
//    private StringBuffer sb = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = findViewById(R.id.input_name);
//        _addressText = (TextView) findViewById(R.id.tv_address);
//        _addressDetailText = (EditText) findViewById(R.id.et_address_detail);
//        _address_detail = (TextInputLayout) findViewById(R.id.input_address_detail);
        _emailText = findViewById(R.id.input_email);
        _mobileText = findViewById(R.id.input_mobile);
        _passwordText = findViewById(R.id.input_password);
//        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _signupButton = findViewById(R.id.btn_signup);
        _loginLink =  findViewById(R.id.link_login);



        //  주소
//        _addressText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!_addressText.getText().toString().isEmpty()) {   //주소에 값이 있으면 상세주소칸 표시
//                    _address_detail.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                _address_detail.setVisibility(View.VISIBLE);
//            }
//        });
    }

    public void OnClickMethod(View v) {
        switch (v.getId()) {
            case R.id.btn_findAddress:
                intent = new Intent(getApplicationContext(), AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_signup:
                if (check_validation()) {
                    register_member();
                }
                break;
            case R.id.link_login:
                onBackPressed();
                break;
        }
    }


    private boolean check_validation(){

        if (_emailText.getText().toString().isEmpty()
                || _nameText.getText().toString().isEmpty()
                || _passwordText.getText().toString().isEmpty()) {
                showToast(getString(R.string.CheckUnit));

            return false;
//        } else if (!(_passwordText.getText().toString().equals(_reEnterPasswordText
//                .getText().toString()))) {
//            showToast(getString(R.string.CheckRepass));
//            return false;
        }
        return true;
    }

    private void register_member(){

        String server_url = new UrlPath().getUrlPath() + "Pc_login/register_Member";
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
                params.put("email", _emailText.getText().toString());
                params.put("name", _nameText.getText().toString());
                params.put("password",  _passwordText.getText().toString());
//                params.put("repassword",  _reEnterPasswordText.getText().toString());
                params.put("mobile", _mobileText.getText().toString());
//                params.put("address", _addressText.getText().toString());
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
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

    private void saveSuccess(String response){
        Log.i(TAG, "결과값은 " + response);
        if("1".equals(response)){
            String toastMessage = getString(R.string.RegisterNewMember);
            showToast(toastMessage);
            onBackPressed();
        }else {
            showToast(response);
        }
    }






    /////// 여기서 부터 DB 쓰레드 작업
//    //  DB 쓰레드 작업
//    class HttpTaskSignIn extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... args) {
//            String result = "";
//
//            LoginService loginservice = null;
//            switch (tcode) {
//                case "newMember":
//                    loginservice = new LoginService.Builder(Controller, tcode).email(args[0]).name(args[1]).password(args[2]).repassword(args[3]).tel(args[4]).address1(args[5]).build();
////					httpHandler.setValue(tcode, args[0],args[1], args[2], args[3]);
//                    break;
//                case "chkId":
//                    loginservice = new LoginService.Builder(Controller, tcode).email(args[0]).build();
////					httpHandler.setValue(tcode, args[0]);
//                    break;
//            }
//            sb = loginservice.getData();
//            try {
//                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
////				JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
////				result = jsonObject.getString("name");
//                jObject = new JSONObject(sb.toString());
//                result = jObject.getString("result");
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String value) {
//            Log.i(TAG, "onPostExecute: tcode) " + tcode);
//            Log.i(TAG, "onPostExecute: value " + value);
//            super.onPostExecute(value);
//            try {
//                switch (tcode) {
//                    case ("newMember"): //최초
//                        if (value.equals("true")) {
//                            toastMessage = getString(R.string.RegisterNewMember);
//                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                            finish();
//                            break;
//                        }
//
//                    case ("chkId"):
//                        if (value.equals("true")) {  //가입가능한 id
//                            toastMessage = getString(R.string.NewPossible);
//                        } else {  //동일한 id 존재
//                            toastMessage = getString(R.string.NewImpossible);
//                        }
//                        break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
//            tcode = "";   //초기화
//        }
//    }
}

