package com.company.jk.pcoordinator.mypage.mybaby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.ParentsActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.company.jk.pcoordinator.MainActivity.bottomBar;

public class MybabyDetailActivity extends MyActivity implements View.OnClickListener, View.OnFocusChangeListener {



    static final String TAG = "MybabyDetailFragment";
    ImageView _profile;
    Button _btn_save, _btn_delete;
    RadioButton _boy, _girl;
    Toolbar myToolbar;
    EditText _name, _sex, _father, _mother, _owner;
    TextView _birthday;
    String email, baby_id;
    UrlPath urlPath = new UrlPath();
    Upload upload = new Upload();
    LoginInfo loginInfo = LoginInfo.getInstance();
    private DatePickerDialog.OnDateSetListener mDateSetListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        baby_id = intent.getExtras().getString("baby_id");

        setContentView(R.layout.activity_mybaby_detail);

        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        findViewsById();

        _btn_save.setOnClickListener(this);
        _btn_delete.setOnClickListener(this);
        _profile.setOnClickListener(this);
        _birthday.setOnClickListener(this);

        _name.setOnFocusChangeListener(this);

        _birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MybabyDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);

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

        if(baby_id != null) {
            initLoader();   //기존아기 세부정보 db에서 가져오기
        }else{
            _btn_delete.setVisibility(View.GONE);
        }

        Log.i(TAG, "이메일은 " + email + " id는 " + baby_id);


    }


    private void initLoader() {
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info_detail";
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "2");
                responseReceiveData(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("baby_id", baby_id);
                return params;
            }
        };
        Log.i(TAG, "1");
        postRequestQueue.add(postStringRequest);

        //data binding end

    }

    private void responseReceiveData(String response) {
        Log.i(TAG, "결과값은 " + response);

        try {
            JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");
            String name = rs.getString("babyname");
            String id = rs.getString("baby_id");
            String birthday = rs.getString("birthday");
//            _birthday.setText(birthday.substring(0, 4)+"년"+birthday.substring(4, 6)+"월"+birthday.substring(6, 8)+"일");
            _birthday.setText(birthday);

            String sex = rs.getString("sex");

            String imgUrl = urlPath.getUrlBabyImg() + id + ".jpg";  //확장자 대소문자 구별함.
            Log.i(TAG, imgUrl);
            Picasso.with(this).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(_profile);  //image가 reload 되도록 하기 위하여 필요함
            _name.setText(name);
//            _birthday.setText(birthday);
            if (sex.equals("1")) {
                _boy.setChecked(true);
            } else {
                _girl.setChecked(true);
            }
        } catch (JSONException e) {
            showToast("등록된 아기가 없습니다.");
            e.printStackTrace();
        }
    }

    private void findViewsById() {  // 위젯 세팅

        _btn_save = findViewById(R.id.btn_save);
        _btn_delete = findViewById(R.id.btn_delete);
        _profile = findViewById(R.id.iv_profile);
        _name = findViewById(R.id.et_name);
        _boy = findViewById(R.id.rd_boy);
        _girl = findViewById(R.id.rd_girl);
        _birthday = findViewById(R.id.et_birthday);
    }

    @Override
    public void onClick(View v) {
        Log.i("onClick", v.toString() + _birthday.toString());
        if(v==_btn_save) {
//            save_data();
            modify_data();
//        }else if(v==_birthday){
//
//            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int day = cal.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog dialog = new DatePickerDialog(
//                    MybabyDetailActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener, year, month, day);
//
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//
//            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                    month = month + 1;
//                    Log.d(TAG, "on Date set " + year + "/" + month + 1 + "/" + day);
//
//                    _birthday.setText(year + "/" + month + "/" + day);
//                }
//            };


        }else if(v==_profile){   //사진 클릭시
            insert_picture();
        }else if(v==_btn_delete){
            deleteAelrtDialog();
        }
    }


    private void deleteAelrtDialog(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.btn_delete)
                .setMessage(R.string.deleteAlert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete_data();
//                        MybabyFragment fragment = new MybabyFragment();
//                        AppCompatActivity activity = (AppCompatActivity)getActivity();
//                        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.enter_from_right).replace(R.id.frame, fragment).addToBackStack(null).commit();
//
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onFocusChange(View v, boolean b) {
        Log.i(TAG, "포커스가 변경 되었습니다.");
        if(!b){   //포커스아웃이면
            if(v ==  _name){
                if(TextUtils.isEmpty(_name.getText().toString())) {
                    _name.setError("필수항목입니다.");
                    return;
                }

            }
        }else{  //포커스인이면
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

    private  void modify_data(){


        String server_url = new UrlPath().getUrlPath() + "Pc_baby/modifyBaby";
        Log.i(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                modifyResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(baby_id != null) {
                    params.put("baby_id", baby_id);
                }
                params.put("owner", email);
                Log.i(TAG, "owner 는  " + email);
                params.put("babyname", _name.getText().toString());
//        Log.i(TAG, _birthday.getText().toString());
//        Log.i(TAG, _birthday.getText().toString().substring(0, 4)+_birthday.getText().toString().substring(5, 7)+_birthday.getText().toString().substring(8, 10));
                params.put("birthday", _birthday.getText().toString());
                if(_boy.isChecked()){
                    params.put("sex", "1");
                }else if(_girl.isChecked()){
                    params.put("sex", "2");
                }
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void modifyResponse(String response){
        Log.i(TAG, "결과값은 " + response);
        super.onBackPressed();
//        MybabyActivity mf = new MybabyActivity();
//        }else{
//            showToast(getString(R.string.savefail));
//        }
    }




    private  void delete_data(){

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/deleteBaby";
        Log.i(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deleteResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("baby_id", baby_id);
                params.put("email", email);

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void deleteResponse(String response){
        Log.i(TAG, "deleteResponse 결과값은 " + response);
        if(response.equals("true")) {
            if(loginInfo.getBabyID().equals(baby_id)){
                update_user_babyid(baby_id);
                loginInfo.setBabyID("");}  //선택하고 있는 아기를 삭제 할 경우 선택된 아기가 없도록 처리
            super.onBackPressed();
        }else{
            showToast(response);
        }
    }

    private void update_user_babyid(String babyid){

        Map<String, String> param = new HashMap<>();
        param.put("email",loginInfo.getEmail());
        param.put("baby_id","0");

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                Log.i(TAG, "onSuccessResponse 결과값은" + result + method);
            }
        };
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        dataTransaction.queryExecute(1,param,"Pc_baby/set_main_baby", callback);
    }
    /////////////////////////////////////사진업로드 시작 //////////////////////////////////////////////
    public void onActivityResult(int requestCode, int resultCode,	Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //Intent x = getActivity().getIntent();
//		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
        if (requestCode == 4 && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//		if (resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
// 따라서, Activity에서 사용되는 RESULT_OK값을 가져와서 사용한다.
            Log.i("onActivityResult", "request pick");
            beginCrop(imageReturnedIntent.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
            Log.i("onActivityResult", "request crop");
            handleCrop(resultCode, imageReturnedIntent, this);
        } else {
            Log.i("onActivityResult", "Activity.requestCode 는 " + String.valueOf(requestCode) + " resultCode는 " + String.valueOf(resultCode));
        }
    }


    private void beginCrop(Uri source) {
        Log.d("beginCrop", "Start" +source.toString());
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
        Log.d("beginCrop", "End");
    }

    private void handleCrop(int resultCode, Intent result, Context ct) {
        if (resultCode == Activity.RESULT_OK) { // Activity 의 RESULT_OK값을 사용
            Log.d("handleCrop", "RESULT_OK" + (Crop.getOutput(result).toString()));
            _profile.setImageDrawable(null);
            _profile.setImageURI(Crop.getOutput(result));
            _profile.invalidate();

            final String absolutePath = Crop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {	}	});
                    Log.i(TAG, "파일명은 " + baby_id + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, baby_id, "babyprofile");
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Log.d("handleCrop", "RESULT_ERROR");
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//Activity에서 사용되던 this는 Fragment에서 보통 getActivity() 또는 getContext() 로 변경해서 사용한다.
        }
    }

    /////////////////////////////////////사진업로드 끝 //////////////////////////////////////////////


//    추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        if(baby_id != null) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_babydetail, menu);
        }
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
;

        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_parentlist:
                // User chose the "Settings" item, show the app settings UI...

                Intent intent = new Intent(getApplicationContext(),ParentsActivity.class);
                intent.putExtra("baby_id", baby_id);
                startActivityForResult(intent, 1000);
                return true;
            default:
                super.onBackPressed();
                return true;
        }

    }
}
