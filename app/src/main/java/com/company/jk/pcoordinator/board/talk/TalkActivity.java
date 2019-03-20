package com.company.jk.pcoordinator.board.talk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TalkActivity extends MyActivity{

    RecyclerView mRecyclerView;
    ArrayList<Talkinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    LoginInfo loginInfo = LoginInfo.getInstance();
    String TAG = "MybabyFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle(getResources().getString(R.string.talklist));
        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = findViewById(R.id.listView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void get_data(){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.i(TAG, "onSuccessResponse 결과값은" + result + method);

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
        transaction.queryExecute(2, params, "Pc_board/get_talklist", callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_data();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_talk, menu);
        return true;
    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);
        int id = 0, eyes = 0, talks = 0, good = 0;
        String title = null;
        String contents = null;
        String createDate = null;
        boolean goodChecked = false;

        items.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getInt("id");
                title = rs.getString("title");
                contents = rs.getString("contents");
                eyes = rs.getInt("eyes");
                talks = rs.getInt("talk");
                good = rs.getInt("good");
                goodChecked = rs.getBoolean("goodChecked");
                createDate = rs.getString("createDate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(new Talkinfo(id, title, contents, eyes, talks, good, goodChecked, createDate));
        }
        mAdapter.notifyDataSetChanged();

    }


    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, TalkDetailActivity.class);
                intent.putExtra("email",loginInfo.getEmail() );
                startActivityForResult(intent, 300);

                return true;

            default:
                onBackPressed();

        }
        return true;

    }

}
