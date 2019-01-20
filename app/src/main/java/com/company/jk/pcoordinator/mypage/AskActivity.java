package com.company.jk.pcoordinator.mypage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.util.HashMap;
import java.util.Map;


public class AskActivity extends MyActivity {

    private final static String TAG = "AskActivity";
    private Toolbar myToolbar;
    EditText et_contents, et_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);


        // Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.QnA);
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        et_title = findViewById(R.id.et_title);
        et_contents = findViewById(R.id.et_contents);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_ask, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_send:
                send_mail();
                return true;

            default:
            onBackPressed();

        }
        return true;

    }

    private void  send_mail(){
        LoginInfo loginInfo = LoginInfo.getInstance();
        MyDataTransaction dataTransaction = new MyDataTransaction(getApplicationContext());
        Map<String, String> params = new HashMap<>();
        params.put("title", et_title.getText().toString());
        params.put("contents", et_contents.getText().toString());
        params.put("email",loginInfo.getEmail() );

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {

            }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        dataTransaction.queryExecute(1, params, "Pc_board/sendAsk", callback);

        showToast(getResources().getString(R.string.send_ask));
        finish();
    }
}
