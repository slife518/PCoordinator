package com.company.jk.pcoordinator.mypage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.company.jk.pcoordinator.login.LoginInfo.APPLICATIONNAME;
import static com.company.jk.pcoordinator.login.LoginInfo.ISAUTO_LOGIN;


//고객정보는 fragment 도 사용 가능하고 actvity 로도 사용 가능하여 둘 다 만들어 놓음.
public class MyinfoActivity extends MyActivity implements View.OnClickListener {

    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";
    private final  int CODE_IMG_GALLERY = 1;
    Context mContext;
    LoginInfo loginInfo;
    SharedPreferences mPreference;
    private String toastMessage = " ";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView _address1, _birthday;

    MyDataTransaction transaction;
    ImageView _profile;
    UrlPath urlPath = new UrlPath();
    Upload upload = new Upload();
    EditText _address2, _name, _tel;
    //    AppCompatImageButton _btn_findaddress;
    Button _btn_save;
    Intent intent;
    CheckBox _cb_auto;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        loginInfo = LoginInfo.getInstance(this);
        transaction = new MyDataTransaction(this);
//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(loginInfo.getEmail());
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);


        mContext = getApplicationContext();
        mPreference = getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);

        _profile = findViewById(R.id.iv_profile);
        _birthday = findViewById(R.id.et_birthday);
        _name = findViewById(R.id.et_name);
        _tel = findViewById(R.id.et_tel);
        PhoneNumberUtils.formatNumber(_tel.getText().toString());
        _tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        _address1 = findViewById(R.id.tv_address);
        _address2 = findViewById(R.id.et_address_detail);
//        _layout_address_detail = (View) findViewById(R.id.layout_address_detail);
//        _btn_findaddress = findViewById(R.id.btn_findAddress);
        _btn_save = findViewById(R.id.btn_save);
        _cb_auto = findViewById(R.id.cb_Auto);
        _btn_save.setOnClickListener(this);
//        _btn_findaddress.setOnClickListener(this);
        _profile.setOnClickListener(this);
        _address1.setOnClickListener(this);
        _cb_auto.setChecked(mPreference.getBoolean(ISAUTO_LOGIN, true));
        _cb_auto.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        mPreference = getSharedPreferences(APPLICATIONNAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPreference.edit();
                        editor.putBoolean(ISAUTO_LOGIN, _cb_auto.isChecked());
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
                        MyinfoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

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

        getCustomerInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_exit:
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                MypageFragment myFragment = new MypageFragment();
//                //왼쪽에서 오른쪽 슬라이드
//                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.frame, myFragment).addToBackStack(null).commit();
//                break;
            case R.id.tv_address:
                intent = new Intent(mContext, AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_save:  //고객정보 저장
                if (check_validation_info()) {
                    save_customer_info();
                }
                break;

            case R.id.iv_profile:
                insert_picture();
                break;
        }
    }

    private void save_customer_info() {

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("name", _name.getText().toString());
        params.put("birthday", _birthday.getText().toString());
        params.put("tel", _tel.getText().toString());
        params.put("address1", _address1.getText().toString());
        params.put("address2", _address2.getText().toString());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

             Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
             reponseSave(result);
            }

            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        transaction.queryExecute(2, params, "Pc_login/save_customer_info", callback);
    }


    private void reponseSave(String response) {
            String result = JsonParse.getResultFromJsonString(response);

            if (result.equals("true")) {
                toastMessage = "저장되었습니다.";
            } else {
                toastMessage = result;
            }

        showToast(toastMessage);

    }


    private void insert_picture() {
        Log.d(TAG, "이미지클릭");

        final AlertDialog.Builder build = new AlertDialog.Builder( // 다이얼로그
                this);
        build.setTitle("프로필 사진 등록")
                .setMessage("프로필 사진을 등록을 원하시면 \n\n'등록'을 눌러주시기 바랍니다. ")
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                                        .setType("image/*"), CODE_IMG_GALLERY);
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
    }


    public boolean check_validation_info() {
        return true;
    }

    /////////////////////////////////////사진업로드 시작 //////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_IMG_GALLERY){ //   && resultCode == Activity.RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == UCrop.REQUEST_CROP) {
            handleCrop(resultCode, data, this);
        }
    }


    private void beginCrop(Uri uri) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        UCrop uCrop = UCrop.of(uri, destination);
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(this);
    }

    private  UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        //CompressType
        //options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors
        options.setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        options.setToolbarColor(getResources().getColor(R.color.primaryColor));

        options.setToolbarTitle(getResources().getString(R.string.choicebabypicture));  //title

        return options;
    }

    private void handleCrop(int resultCode, Intent result, Context ct) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용
//            Log.d("handleCrop", "RESULT_OK" + (Crop.getOutput(result).toString()));
            _profile.setImageDrawable(null);
            _profile.setImageURI(UCrop.getOutput(result));
            _profile.invalidate();

            final String absolutePath = UCrop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
//                    Log.d(TAG, "파일명은 " + loginInfo.getEmail() + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, loginInfo.getEmail(), "memberprofile");
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();

        } else if (resultCode == UCrop.RESULT_ERROR) {
//            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(this, UCrop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }
    }

    /////////////////////////////////////사진업로드 끝 //////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getCustomerInfo() {

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);

                switch (method) {
                    case 2:  //get_data
                        reponseCustomerInfo(result);
                        break;
                }
            }

            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        transaction.queryExecute(2, params, "Pc_login/select_customer_info", callback);
    }

    private void reponseCustomerInfo(String response) {
            try {
                JSONObject rs = JsonParse.getJsonObjectSingleFromString(response);

                String imgUrl = urlPath.getUrlMemberImg() + loginInfo.getEmail() + ".jpg";  //확장자 대소문자 구별함.
                Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(_profile);  //image가 reload 되도록 하기 위하여 필요함
                _name.setText(rs.getString("nickname"));
                _birthday.setText(rs.getString("birthday"));
                _tel.setText(rs.getString("tel"));
                Log.d(TAG, "주소는 ??" + (rs.getString("address1")));
                _address1.setText(rs.getString("address1"));
                _address2.setText(rs.getString("address2"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


