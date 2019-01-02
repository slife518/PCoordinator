package com.company.jk.pcoordinator.mypage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.login.LoginService;

import org.json.JSONObject;

import java.util.Calendar;


//고객정보는 fragment 도 사용 가능하고 actvity 로도 사용 가능하여 둘 다 만들어 놓음.
public class MyinfoActivity extends MyActivity implements View.OnClickListener {


    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";

    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    SharedPreferences mPreference;
    private static String tcode;

    private StringBuffer sb = new StringBuffer();
    private static final String Controller = "Pc_login";
    private String toastMessage = " ";
    private DatePickerDialog.OnDateSetListener mDateSetListener ;

    TextView _address1, _birthday;
    EditText _address2, _name, _tel;
    View _layout_address_detail;
    AppCompatImageButton _btn_findaddress;
    Button _btn_save;
    Intent intent;
    CheckBox _cb_auto;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tcode = "select_customer_info";
        new HttpTaskSignIn().execute(loginInfo.getEmail());


        // Toolbar를 생성한다.
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(loginInfo.getEmail());


        mContext = getApplicationContext();
        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);

        _birthday = (TextView) findViewById(R.id.et_birthday);
        _name = (EditText) findViewById(R.id.et_name);
        _tel = (EditText) findViewById(R.id.et_tel);
        _address1 = (TextView) findViewById(R.id.tv_address);
        _address2 = (EditText) findViewById(R.id.et_address_detail);
//        _layout_address_detail = (View) findViewById(R.id.layout_address_detail);
        _btn_findaddress = (AppCompatImageButton) findViewById(R.id.btn_findAddress);
        _btn_save = (Button) findViewById(R.id.btn_save);
        _cb_auto = (CheckBox) findViewById(R.id.cb_Auto);
        _btn_save.setOnClickListener(this);
        _btn_findaddress.setOnClickListener(this);
        _address1.setOnClickListener(this);
        _cb_auto.setChecked(mPreference.getBoolean("AutoChecked", true));
        _cb_auto.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.putBoolean("AutoChecked", _cb_auto.isChecked());
                        editor.commit();
                    }
                }
        );

        _birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = 1985;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MyinfoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "on Date set " + year + "/" + month + 1 + "/" + day);

                _birthday.setText(year + "/" + month + "/" + day);
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.btn_exit:
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                MypageFragment myFragment = new MypageFragment();
//                //왼쪽에서 오른쪽 슬라이드
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
//                break;
            case R.id.btn_findAddress : case R.id.tv_address:
                intent = new Intent(mContext, AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_save:  //고객정보 저장
                if(check_validation_info()){
                    tcode = "save_customer_info";
                    new HttpTaskSignIn().execute(loginInfo.getEmail(), _name.getText().toString(),_birthday.getText().toString(), _tel.getText().toString(), _address1.getText().toString(), _address2.getText().toString());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /////// 여기서 부터 DB 쓰레드 작업
    //  DB 쓰레드 작업
    class HttpTaskSignIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            int i = 0;
            LoginService serviceHandler = null;
            switch (tcode) {
                case "save_customer_info":
                    serviceHandler = new LoginService.Builder(Controller, tcode).email(args[i++]).name(args[i++]).birthday(args[i++]).tel(args[i++]).address1(args[i++]).address2(args[i++]).build();
                    break;
                case "select_customer_info":
                    serviceHandler = new LoginService.Builder(Controller, tcode).email(args[i++]).build();
                    break;
            }
            return serviceHandler.getData().toString();
        }



        @Override
        protected void onPostExecute(String sb) {
            super.onPostExecute(sb);
            JSONObject jObject = null; //group들로 구성된 json
            try {
                jObject = new JSONObject(sb);
                switch (tcode) {
                    case("save_customer_info"):
                        String result = jObject.getString("result");
                        if (result.equals("true")){
                            toastMessage = "저장되었습니다.";


                            break;
                        }else{
                            toastMessage = result;
                            break;
                        }

                    case("select_customer_info"):
                        _name.setText(jObject.getString("nickname"));
                        _birthday.setText(jObject.getString("birthday"));
                        _tel.setText(jObject.getString("tel"));
                        _address1.setText(jObject.getString("address1"));
                        _address2.setText(jObject.getString("address2"));
                        toastMessage = "";
                        break;
                }

                if(!toastMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            tcode = "";   //초기화
            toastMessage = "";   //초기화
        }
    }

}
