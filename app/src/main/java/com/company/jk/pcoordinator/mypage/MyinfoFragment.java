package com.company.jk.pcoordinator.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.LoginService;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class MyinfoFragment extends Fragment implements View.OnClickListener {

    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";

    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    SharedPreferences mPreference;
    private static String tcode;
    private JSONObject jObject = null; //group들로 구성된 json
    private StringBuffer sb = new StringBuffer();
    private static final String Controller = "Pc_Login";
    private String toastMessage = " ";


    TextView _address1;
    EditText _address2, _email, _name, _tel, _birthday;
    ImageView _back;
    AppCompatImageButton _btn_findaddress;
    Button _btn_save;
    Intent intent;
    CheckBox _cb_auto;
    View v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tcode = "select_customer_info";
        new HttpTaskSignIn().execute(loginInfo.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_myinfo, container, false);
//        mContext = getActivity();
        mContext = v.getContext();
        mPreference = getContext().getSharedPreferences("pcoordinator", MODE_PRIVATE);

        _email = (EditText) v.findViewById(R.id.et_email);
        _birthday = (EditText) v.findViewById(R.id.et_birthday);
        _name = (EditText) v.findViewById(R.id.et_name);
        _tel = (EditText) v.findViewById(R.id.et_tel);
        _address1              = (TextView) v.findViewById(R.id.tv_address);
        _address2      = (EditText) v.findViewById(R.id.et_address_detail) ;
        _back                  = (ImageView) v.findViewById(R.id.btback);
        _btn_findaddress = (AppCompatImageButton) v.findViewById(R.id.btn_findAddress);
        _btn_save = (Button) v.findViewById(R.id.btn_save);
        _cb_auto = (CheckBox)v.findViewById(R.id.cb_Auto);

        _back.setOnClickListener(this);
        _btn_save.setOnClickListener(this);
        _btn_findaddress.setOnClickListener(this);
        _address1.setOnClickListener(this);
        _cb_auto.setChecked(mPreference.getBoolean("AutoChecked", true));
        _cb_auto.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mPreference = getContext().getSharedPreferences("pcoordinator", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.putBoolean("AutoChecked", _cb_auto.isChecked());
                        editor.commit();
                    }
                }
        );

        _address1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!_address1.getText().toString().isEmpty()){   //주소에 값이 있으면 상세주소칸 표시
                    _address2.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                _address2.setVisibility(View.VISIBLE);
            }
        });

        return  v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                    new HttpTaskSignIn().execute(_email.getText().toString(), _name.getText().toString(),_birthday.getText().toString(), _tel.getText().toString(), _address1.getText().toString(), _address2.getText().toString());
                }
                break;
        }
    }

    public boolean check_validation_info(){
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
                    _address1.setText(result);
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
            int i = 0;
            LoginService serviceHandler = null;
            switch (tcode) {
                case "save_customer_info":
                    serviceHandler = new LoginService.Builder(Controller, tcode).email(args[i++]).name(args[i++]).birthday(args[i++]).tel(args[i++]).address1(args[i++]).address2(args[i++]).build();
//                    serviceHandler = new HttpHandler2.Builder(Controller, tcode).email(args[0]).name(args[1]).birthday(args[2]).tel(args[3]).address1(args[4]).address2(args[5]).build();
                    break;
                case "select_customer_info":
                    serviceHandler = new LoginService.Builder(Controller, tcode).email(args[i++]).build();
                    break;
            }
            sb = serviceHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//				JSONObject jsonObject = serviceHandler.getNeedJSONObject(sb, "result");
//				result = jsonObject.getString("name");
                jObject = new JSONObject(sb.toString());


                switch (tcode) {
                    case "save_customer_info":
                        result = jObject.getString("result");
                        break;
                    case "select_customer_info":
                        _email.setText(jObject.getString("email"));
                        _name.setText(jObject.getString("nickname"));
                        _birthday.setText(jObject.getString("birthday"));
                        _tel.setText(jObject.getString("tel"));
                        _address1.setText(jObject.getString("address1"));
                        _address2.setText(jObject.getString("address2"));
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute: result "+ result + " ,tcode) " + tcode);
            super.onPostExecute(result);
            try {
                switch (tcode) {
                    case("save_customer_info"): case "save_customer_pw":
                        if (result.equals("true")){
                            toastMessage = "저장되었습니다.";
                            break;
                        }else{
                            toastMessage = result;
                            break;
                        }
                    case("select_customer_info"):

                        break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(v.getContext(), toastMessage, Toast.LENGTH_SHORT).show();
            tcode = "";   //초기화
        }
    }
}
