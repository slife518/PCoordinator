package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.HttpHandler2;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginActivity;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.login.SignupActivity;

import org.json.JSONObject;


public class MyinfoFragment extends Fragment implements View.OnClickListener {

    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";

    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    private static String tcode;
    private JSONObject jObject = null; //group들로 구성된 json
    private StringBuffer sb = new StringBuffer();
    final static String Controller = "Login";
    private String toastMessage = " ";


    TextView _addressText;
    EditText _addressDetailText, _email, _name, _tel, _birthday, _password, _repassword;
    TextInputLayout _addressDetail;
    ImageView _back;
    AppCompatImageButton _btn_findaddress;
    Button _btn_save, _btn_save_pw;
    Intent intent;
    View v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_myinfo, container, false);
//        mContext = getActivity();
        mContext = v.getContext();


        _email = (EditText) v.findViewById(R.id.et_email);
        _birthday = (EditText) v.findViewById(R.id.et_birthday);
        _name = (EditText) v.findViewById(R.id.et_name);
        _tel = (EditText) v.findViewById(R.id.et_tel);
        _addressText              = (TextView) v.findViewById(R.id.tv_address);
        _addressDetailText      = (EditText) v.findViewById(R.id.et_address_detail) ;
        _addressDetail = (TextInputLayout) v.findViewById(R.id.input_address_detail);
        _back                  = (ImageView) v.findViewById(R.id.btback);
        _btn_findaddress = (AppCompatImageButton) v.findViewById(R.id.btn_findAddress);
        _btn_save = (Button) v.findViewById(R.id.btn_save);
        _btn_save_pw = (Button) v.findViewById(R.id.btn_password_save);


        _back.setOnClickListener(this);
        _btn_save.setOnClickListener(this);
        _btn_save_pw.setOnClickListener(this);
        _btn_findaddress.setOnClickListener(this);
        _addressText.setOnClickListener(this);

        _addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!_addressText.getText().toString().isEmpty()){   //주소에 값이 있으면 상세주소칸 표시
                    _addressDetail.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                _addressDetailText.setVisibility(View.VISIBLE);
            }
        });

        return  v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tcode = "select_customer_info";
        new HttpTaskSignIn().execute(loginInfo.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btback:
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
                break;
            case R.id.btn_findAddress : case R.id.tv_address:
                intent = new Intent(mContext, AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_save:  //고객정보 저장
                if(check_validation_info()){
                    tcode = "save_customer_info";
                    new HttpTaskSignIn().execute(_email.getText().toString(), _name.getText().toString(),_tel.getText().toString(), _addressText.getText().toString(), _addressDetailText.getText().toString());
                }
                break;
            case R.id.btn_password_save:  //비밀번호 변경 저장
                if(check_validation_pw()){
                    tcode = "save_customer_pw";
                    new HttpTaskSignIn().execute(_password.getText().toString(), _repassword.getText().toString());
                }
                break;
        }
    }

    public boolean check_validation_info(){
        return  true;
    }

    public boolean check_validation_pw(){
        return  true;
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
                case "save_customer_info":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).email(args[0]).name(args[1]).mobile(args[2]).address(args[3]).address_detail(args[4]).build();
                    break;
                case "save_customer_pw":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).password(args[0]).repassword(args[1]).build();
                    break;
                case "select_customer_info":
                    httpHandler = new HttpHandler2.Builder(Controller, tcode).email(args[0]).build();
                    break;
            }
            sb = httpHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//				JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
//				result = jsonObject.getString("name");
                jObject = new JSONObject(sb.toString());


                switch (tcode) {
                    case "save_customer_info": case "save_customer_pw":
                        result = jObject.getString("result");
                        break;
                    case "select_customer_info":
                        _email.setText(jObject.getString("email"));
                        _name.setText(jObject.getString("nickname"));
                        _tel.setText(jObject.getString("mobile"));
                        _addressText.setText(jObject.getString("address1"));
                        _addressDetailText.setText(jObject.getString("address2"));
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String value) {
            Log.i(TAG, "onPostExecute: value "+ value + "onPostExecute: tcode) " + tcode);
            super.onPostExecute(value);
            try {
                switch (tcode) {
                    case("save_customer_info"): //최초
                        if (value.equals("true")){
                            toastMessage = "저장되었습니다.";
                            break;
                        }
                    case("select_customer_info"):

                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
