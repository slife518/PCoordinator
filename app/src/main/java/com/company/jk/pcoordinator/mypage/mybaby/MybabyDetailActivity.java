package com.company.jk.pcoordinator.mypage.mybaby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.mypage.parents.ParentsActivity;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.Upload;
import com.company.jk.pcoordinator.http.UrlPath;
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

public class MybabyDetailActivity extends MyActivity implements View.OnClickListener, View.OnFocusChangeListener {



    static final String TAG = "MybabyDetailFragment";
    ImageView _profile;
    Button _btn_save, _btn_delete;
    RadioButton _boy, _girl;
    Toolbar myToolbar;
    EditText _name, _sex, _father, _mother, _owner;
    TextView _birthday;
    String email;
    int baby_id;
    UrlPath urlPath = new UrlPath();
    Upload upload = new Upload();
    LoginInfo loginInfo;
    private DatePickerDialog.OnDateSetListener mDateSetListener ;
    String absolutePath = "";
    private final  int CODE_IMG_GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        baby_id = intent.getExtras().getInt("baby_id");

        setContentView(R.layout.activity_mybaby_detail);

        loginInfo = LoginInfo.getInstance(this);
        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.mybabyinfo));
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

        if(baby_id != 0) {
            initLoader();   //기존아기 세부정보 db에서 가져오기
        }else{
            _btn_delete.setVisibility(View.GONE);
        }

        Log.d(TAG, "이메일은 " + email + " id는 " + baby_id);


    }

    private void initLoader() {
        //data binding start
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/get_baby_info_detail";
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "2");
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
                params.put("baby_id", String.valueOf(baby_id));
                return params;
            }
        };
        Log.d(TAG, "1");
        postRequestQueue.add(postStringRequest);

        //data binding end

    }

    private void responseReceiveData(String response) {
        Log.d(TAG, "결과값은 " + response);

        try {
            JSONObject rs = JsonParse.getJsonObjectFromString(response, "result");
            String name = rs.getString("babyname");
            String id = rs.getString("baby_id");
            String birthday = rs.getString("birthday");
//            _birthday.setText(birthday.substring(0, 4)+"년"+birthday.substring(4, 6)+"월"+birthday.substring(6, 8)+"일");
            _birthday.setText(birthday);

            String sex = rs.getString("sex");

            String imgUrl = urlPath.getUrlBabyImg() + id + ".jpg";  //확장자 대소문자 구별함.
            Log.d(TAG, imgUrl);
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
        Log.d("onClick", v.toString() + _birthday.toString());
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
        Log.d(TAG, "포커스가 변경 되었습니다.");
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

    private  void modify_data(){


        String server_url = new UrlPath().getUrlPath() + "Pc_baby/modifyBaby";
        Log.d(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                modifyResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(baby_id != 0) {
                    params.put("baby_id", String.valueOf(baby_id));
                }
                params.put("owner", email);
                Log.d(TAG, "owner 는  " + email);
                params.put("babyname", _name.getText().toString());
//        Log.d(TAG, _birthday.getText().toString());
//        Log.d(TAG, _birthday.getText().toString().substring(0, 4)+_birthday.getText().toString().substring(5, 7)+_birthday.getText().toString().substring(8, 10));
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
        Log.d(TAG, "결과값은 " + response);

        JSONObject jsonObject = JsonParse.getJsonObjectSingleFromString(response);
        try {
            String type = (String)jsonObject.get("type");
            if(type.equals("new")){  // 신규 아기 등록시
                baby_id = (Integer)jsonObject.get("result");;
                loginInfo.setBabyID(baby_id);
            }
            upload_baby_image(baby_id);
        }catch (JSONException e){
            e.printStackTrace();
        }


        super.onBackPressed();

    }


    private  void delete_data(){

        String server_url = new UrlPath().getUrlPath() + "Pc_baby/deleteBaby";
        Log.d(TAG, server_url);

        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                deleteResponse(response);    // 결과값 받아와서 처리하는 부분
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("baby_id", String.valueOf(baby_id));
                params.put("email", email);

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }

    private void deleteResponse(String response){
        Log.d(TAG, "deleteResponse 결과값은 " + response);
        if(response.equals("true")) {
            String imgUrl = urlPath.getUrlBabyImg() + baby_id + ".jpg";  //확장자 대소문자 구별함.
            upload.deleteFile(imgUrl);
            if(loginInfo.getBabyID() == baby_id){
                update_user_babyid(baby_id);
                loginInfo.setBabyID(0);}  //선택하고 있는 아기를 삭제 할 경우 선택된 아기가 없도록 처리
            super.onBackPressed();
        }else{
            showToast(response);
        }
    }

    private void update_user_babyid(int babyid){

        Map<String, String> param = new HashMap<>();
        param.put("email",loginInfo.getEmail());
        param.put("baby_id","0");

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {
                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
            }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        dataTransaction.queryExecute(1,param,"Pc_baby/set_main_baby", callback);
    }

    /////////////////////////////////////사진업로드 시작 //////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_IMG_GALLERY && resultCode == Activity.RESULT_OK) {
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
            _profile.setImageDrawable(null);
            _profile.setImageURI(UCrop.getOutput(result));
            _profile.invalidate();

            absolutePath = UCrop.getOutput(result).getPath();   // 쓰레드 내에서 사용할 변수는 final 로 정의 되어야 함.  uri 의 절대 경로는 uri.getPath()

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, UCrop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void upload_baby_image(final int baby){
        if(absolutePath != "") {  //사진이 첨부된 적이 있으면 업로드
            //파일 업로드 시작! 파일 업로드 call 할 때는 반드시 쓰레드 이용해야 함.
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                        }
                    });
                    Log.d(TAG, "파일명은 " + baby + " 업로드할 사진의 절대 경로 " + absolutePath);
                    upload.uploadFile(absolutePath, String.valueOf(baby), "babyprofile");
                    //			saveBitmaptoJpeg(bitmap, "",loginInfo.getEmail());
                }
            }).start();
        }
    }
    /////////////////////////////////////사진업로드 끝 //////////////////////////////////////////////


//    추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        if(baby_id != 0) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_babydetail, menu);
        }

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.iconColorDarkBackground), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add:
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
