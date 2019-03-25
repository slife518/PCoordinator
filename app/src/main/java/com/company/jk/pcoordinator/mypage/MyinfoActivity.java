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
import android.os.AsyncTask;
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

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.AddressPostActivity;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.login.LoginService;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;


//고객정보는 fragment 도 사용 가능하고 actvity 로도 사용 가능하여 둘 다 만들어 놓음.
public class MyinfoActivity extends MyActivity implements View.OnClickListener {


    private static final int ADDRESS_REQUEST = 1888;
    private static final String TAG = "MyinfoFragment";

    Context mContext;
    LoginInfo loginInfo = LoginInfo.getInstance();
    SharedPreferences mPreference;
    private static String tcode;

    private static final String Controller = "Pc_login";
    private String toastMessage = " ";
    private DatePickerDialog.OnDateSetListener mDateSetListener ;
    TextView _address1, _birthday;

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

//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tcode = "select_customer_info";
        new HttpTaskSignIn().execute(loginInfo.getEmail());


        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(loginInfo.getEmail());
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);


        mContext = getApplicationContext();
        mPreference = getSharedPreferences("pcoordinator", MODE_PRIVATE);

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
            case R.id.tv_address:
                intent = new Intent(mContext, AddressPostActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;

            case R.id.btn_save:  //고객정보 저장
                if(check_validation_info()){
                    tcode = "save_customer_info";
                    new HttpTaskSignIn().execute(loginInfo.getEmail(), _name.getText().toString(),_birthday.getText().toString(), _tel.getText().toString(), _address1.getText().toString(), _address2.getText().toString());
                }
                break;

            case R.id.iv_profile:
                insert_picture();
                break;
        }
    }


    private  void insert_picture(){
        Log.i(TAG, "이미지클릭");

        final AlertDialog.Builder build = new AlertDialog.Builder( // 다이얼로그
                this);
        build.setTitle("프로필 사진 등록")
                .setMessage("프로필 사진을 등록을 원하시면 \n\n'등록'을 눌러주시기 바랍니다. ")
                .setPositiveButton("등록",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_PICK);
                                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                Log.i(TAG, "사진선택1");
                                startActivityForResult(intent, 4);
                                Log.i(TAG, "사진선택완료2");

                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
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
        }else{

            //Intent x = getActivity().getIntent();
//		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
            if (requestCode == 4 && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//		if (resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
// 따라서, Activity에서 사용되는 RESULT_OK값을 가져와서 사용한다.
                Log.i("onActivityResult", "request pick");
                beginCrop(data.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
                Log.i("onActivityResult", "request crop");
                handleCrop(resultCode, data, this);
            } else {
                Log.i("onActivityResult", "Activity.requestCode 는 " + String.valueOf(requestCode) + " resultCode는 " + String.valueOf(resultCode));
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
                        String imgUrl = urlPath.getUrlMemberImg() + loginInfo.getEmail() + ".jpg";  //확장자 대소문자 구별함.
                        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(_profile);  //image가 reload 되도록 하기 위하여 필요함
                        _name.setText(jObject.getString("nickname"));
                        _birthday.setText(jObject.getString("birthday"));
                        _tel.setText(jObject.getString("tel"));
                        Log.i(TAG, "주소는 ??" + (jObject.getString("address1")));
                        _address1.setText(jObject.getString("address1"));
                        _address2.setText(jObject.getString("address2"));
                        toastMessage = "";
                        break;
                }

                if(!toastMessage.isEmpty()) {
                    showToast(toastMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            tcode = "";   //초기화
            toastMessage = "";   //초기화
        }
    }

    /////////////////////////////////////사진업로드 시작 //////////////////////////////////////////////
//    public void onActivityResult(int requestCode, int resultCode,	Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        //Intent x = getActivity().getIntent();
////		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//        if (requestCode == 4 && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
////		if (resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//// 따라서, Activity에서 사용되는 RESULT_OK값을 가져와서 사용한다.
//            Log.i("onActivityResult", "request pick");
//            beginCrop(imageReturnedIntent.getData());
//        } else if (requestCode == Crop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
//            Log.i("onActivityResult", "request crop");
//            handleCrop(resultCode, imageReturnedIntent, this);
//        } else {
//            Log.i("onActivityResult", "Activity.requestCode 는 " + String.valueOf(requestCode) + " resultCode는 " + String.valueOf(resultCode));
//        }
//    }


    private void beginCrop(Uri source) {
//        Log.d("beginCrop", "Start" +source.toString());
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
//        Log.d("beginCrop", "End");
    }

    private void handleCrop(int resultCode, Intent result, Context ct) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용
//            Log.d("handleCrop", "RESULT_OK" + (Crop.getOutput(result).toString()));
            _profile.setImageDrawable(null);
            _profile.setImageURI(Crop.getOutput(result));
            _profile.invalidate();

            final String absolutePath = Crop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {	}	});
//                    Log.i(TAG, "파일명은 " + loginInfo.getEmail() + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, loginInfo.getEmail(), "memberprofile");
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
//            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }
    }

    /////////////////////////////////////사진업로드 끝 //////////////////////////////////////////////

}
