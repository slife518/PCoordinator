package com.company.jk.pcoordinator.board.talk;

import android.app.Activity;
import android.content.Intent;
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
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.MyPhotoCrop;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.soundcloud.android.crop.Crop;

import java.util.HashMap;
import java.util.Map;

public class NewTalkActivity extends MyActivity implements View.OnClickListener {

    EditText et_title, et_contents;
    ImageView iv_insertPhoto1, iv_camera;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    String TAG = "NewTalkActivity";
    LoginInfo loginInfo = LoginInfo.getInstance();
    MyPhotoCrop photoCrop = new MyPhotoCrop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_new);

        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.talklist));
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        et_title = findViewById(R.id.et_title);
        et_contents = findViewById(R.id.et_contents);
        iv_insertPhoto1 = findViewById(R.id.iv_insertPhoto1);
        iv_camera = findViewById(R.id.ib_camera);
        iv_camera.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_register, menu);
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
                Log.i(TAG, "사진업로드 준비");
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
        params.put("title", et_title.getText().toString());
        params.put("contents", et_contents.getText().toString());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.i(TAG, "onSuccessResponse 결과값은" + result + method);
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

    private boolean insert_picture(){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        Log.i(TAG, "사진선택1");
        startActivityForResult(intent, 4);
        Log.i(TAG, "사진선택2");


        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            //Intent x = getActivity().getIntent();
//		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
            if (requestCode == 4 && resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
//		if (resultCode == Activity.RESULT_OK) { // RESULT_OK 는 동작 성공을 의미하며 수치는 -1 인데, Fragment에는 없다.
// 따라서, Activity에서 사용되는 RESULT_OK값을 가져와서 사용한다.
                Log.i("onActivityResult", "request pick");

                photoCrop.beginCrop(data.getData(), getApplicationContext(), this);
            } else if (requestCode == Crop.REQUEST_CROP) {   // Crop.REQUEST_CROP = 6709
                Log.i("onActivityResult", "request crop");
                if(data !=null) {
                    Log.i("onActivityResult", "handle crop 에서 발생 ");
                    iv_insertPhoto1.setImageURI(photoCrop.handleCrop(data));
                    iv_insertPhoto1.setTag(photoCrop.handleCrop(data).getPath());
                    iv_insertPhoto1.setVisibility(View.VISIBLE);
                }
            } else {
                Log.i("onActivityResult", "Activity.requestCode 는 " + String.valueOf(requestCode) + " resultCode는 " + String.valueOf(resultCode));
            }

        }


}
