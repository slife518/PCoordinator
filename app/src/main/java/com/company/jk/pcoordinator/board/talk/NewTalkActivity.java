package com.company.jk.pcoordinator.board.talk;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.Textview_nanumsquareeb;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.util.HashMap;
import java.util.Map;

public class NewTalkActivity extends MyActivity implements View.OnClickListener {

    TextView tv_register;
    EditText et_title, et_contents;
    ImageView iv_camera;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    String TAG = "NewTalkActivity";
    LoginInfo loginInfo = LoginInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_talk);

        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.talklist));
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);


        tv_register = findViewById(R.id.tv_register);
        et_title = findViewById(R.id.et_title);
        et_contents = findViewById(R.id.et_contents);
        iv_camera = findViewById(R.id.iv_camera);

        tv_register.setOnClickListener(this);
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

            default:
                onBackPressed();

        }
        return true;

    }

    private void register_content(){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.i(TAG, "onSuccessResponse 결과값은" + result + method);
                switch (method){
                    case 1:  //get_data
                        break;
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        String url = "";
        int method = 1;
        url = "Pc_board/add_reply";

        transaction.queryExecute(method, params, url, callback);  //좋아요 체크 업데이트
    }

    @Override
    public void onClick(View view) {

    }
}
