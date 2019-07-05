package com.company.jk.pcoordinator.board.talk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.MyPhotoCrop;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewTalkActivity extends MyActivity implements View.OnClickListener {

    EditText et_title, et_contents;
    ImageView iv_insertPhoto1, iv_camera;
    Toolbar myToolbar;
    private UrlPath urlPath = new UrlPath();
    MyDataTransaction transaction;
    String TAG = "NewTalkActivity";
    LoginInfo loginInfo;
    MyPhotoCrop photoCrop = new MyPhotoCrop();
    int id = 0, reply_id = 0, reply_level = 0;
    private final  int CODE_IMG_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_new);

        loginInfo = LoginInfo.getInstance(this);
        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getSupportActionBar().setTitle(getResources().getString(R.string.talklist));
//        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        et_title = findViewById(R.id.et_title);
        et_contents = findViewById(R.id.et_contents);
        iv_insertPhoto1 = findViewById(R.id.iv_insertPhoto1);
        iv_camera = findViewById(R.id.ib_camera);
        iv_camera.setOnClickListener(this);

        get_data();
    }

    public void get_data(){
        Intent intent = getIntent();
        Map<String, String> params = new HashMap<>();

        id = intent.getExtras().getInt("id");

        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);

                switch (method){
                    case 2:  //get_data
                        responseSuccess(result);
                        break;
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        transaction.queryExecute(2, params, "Pc_board/modify_talk_detail", callback);
    }



    private void responseSuccess(String response) {
        Log.d(TAG, "결과값은 " + response);
        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

            try {
                JSONObject rs = (JSONObject) jsonArray.get(0);
                et_title.setText(rs.getString("title"));
                et_contents.setText(rs.getString("contents"));
                final String imgUrl = urlPath.getUrlTalkImg() + id + "_" + reply_id + "_" + reply_level + ".jpg";  //확장자 대소문자 구별함(무조건 소문자 jpg 사용할 것.
                Picasso.with(this).invalidate(imgUrl);   //image가 reload 되도록 하기 위하여 필요함.
                Picasso.with(this).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_insertPhoto1, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        iv_insertPhoto1.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        iv_insertPhoto1.setVisibility(View.GONE);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_register, menu);
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
            case R.id.talk_add:
               register_content();

                return true;

            case R.id.ib_camera:
                Log.d(TAG, "사진업로드 준비");
                insert_picture();
            default:
                onBackPressed();

        }
        return true;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void register_content(){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));
        params.put("title", et_title.getText().toString());
        params.put("contents", et_contents.getText().toString());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
                switch (method){
                    case 1:  //get_data
                        result = result + "_0_0";
                        if(iv_insertPhoto1.getDrawable() != null){
                            photoCrop.uploadPhoto((iv_insertPhoto1.getTag().toString()), "talk", result);
                        }

                        onBackPressed();
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        String url = "";
        int method = 1;
        url = "Pc_board/add_new";

        transaction.queryExecute(method, params, url, callback);  //좋아요 체크 업데이트
    }

    @Override
    public void onClick(View view) {
        insert_picture();
    }

    // 사진 업로드 시작
    private boolean insert_picture(){
        startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), CODE_IMG_GALLERY);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_IMG_GALLERY && resultCode == Activity.RESULT_OK) {
                photoCrop.beginCrop(data.getData(), getApplicationContext(), this, getResources().getString(R.string.choicepicture));
            } else if (requestCode == UCrop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
                if(data !=null) {
                    iv_insertPhoto1.setImageURI(photoCrop.handleCrop(data));
                    iv_insertPhoto1.setTag(photoCrop.handleCrop(data).getPath());
                    iv_insertPhoto1.setVisibility(View.VISIBLE);
                }
            }
        }

        // 사진업로드 끝
}
