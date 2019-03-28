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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class TalkDetailActivity extends MyActivity implements View.OnClickListener{

    RecyclerView mRecyclerView;
    ArrayList<Talkinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerDetailViewAdapter mAdapter;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    LoginInfo loginInfo = LoginInfo.getInstance();
    String TAG = "TalkDetailActivity";
    int id = 0;
    int reply_id = 0;
    int reply_level = 0;
    int goodCount = 0 ;
    TextView title;
    TextView author;
    TextView contents;
//    TextView eyes;
//    TextView talks;
    TextView good;
    TextView createDate;
    TextView tv_register;
    ImageView mPicture;
    ImageView iv_good;
    boolean goodChecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);

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
        mAdapter = new RecyclerDetailViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        findviewByid();
        iv_good.setOnClickListener(this);
        tv_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == iv_good){
            update_contents("good");
        }else if(view == tv_register){
            update_contents("reply");
        }
    }

    private void findviewByid(){
        title    = findViewById(R.id.tv_title);
        author    = findViewById(R.id.tv_author);
        contents = findViewById(R.id.tv_contents);
//        eyes = findViewById(R.id.tv_eyecount);
        good = findViewById(R.id.tv_goodcount);
        iv_good = findViewById(R.id.iv_good);
//        talks = findViewById(R.id.tv_chatcount);
        createDate = findViewById(R.id.tv_createDate);
        mPicture = findViewById(R.id.iv_image);
        tv_register = findViewById(R.id.tv_register);
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
        transaction.queryExecute(2, params, "Pc_board/get_talk_detail", callback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_data();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();

        if(loginInfo.getEmail().equals(intent.getExtras().getString("author_email"))){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_talk, menu);
        }

        return true;
    }

    private void responseSuccess(String response) {
        Log.i(TAG, "결과값은 " + response);

        items.clear();

        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");

        set_main_contents(jsonArray);
        set_reply(jsonArray);

        mAdapter.notifyDataSetChanged();
    }


    private void set_main_contents(JSONArray jsonArray){

        try {
            JSONObject rs = (JSONObject) jsonArray.get(0);
            id = rs.getInt("id");
            reply_id = rs.getInt("reply_id");
            reply_level = rs.getInt("reply_level");
            title.setText(rs.getString("title"));
            author.setText(rs.getString("author"));
            contents.setText(rs.getString("contents"));
//            eyes.setText(rs.getString("eyes"));
//            talks.setText(rs.getString("talk"));
            good.setText(rs.getString("good"));
            goodChecked = rs.getBoolean("goodChecked");
            createDate.setText(rs.getString("createDate"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void set_reply(JSONArray jsonArray){   //recyclerview 에 뿌려줄 댓글들
        int id = 0, reply_id = 0, reply_level = 0, eyes = 0, talks = 0;
        String title = null;
        String author = null;
        String email = null;
        String contents = null;
        String createDate = null;
        boolean goodChecked = false;

        for (int i = 1; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                id = rs.getInt("id");
                reply_id = rs.getInt("reply_id");
                reply_level = rs.getInt("reply_level");
                title = rs.getString("title");
                author = rs.getString("author");
                email = rs.getString("email");
                contents = rs.getString("contents");
                eyes = rs.getInt("eyes");
                talks = rs.getInt("talk");
                goodCount = rs.getInt("good");
                goodChecked = rs.getBoolean("goodChecked");
                createDate = rs.getString("createDate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            items.add(new Talkinfo(id, reply_id, reply_level, title, author, email, contents, eyes, talks, goodCount, goodChecked, createDate));
        }
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

    private void update_contents(String gubun){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));


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
        int method = 0;

        switch(gubun){
            case "good":  //좋아요 업데이트
                if (!goodChecked){
                    goodChecked = true;
                    good.setText(++goodCount);
                    url = "Pc_board/add_talk_good";

                }else {
                    goodChecked = false;
                    good.setText(--goodCount);
                    url = "Pc_board/delete_talk_good";

                }
                method = 1;

            case "reply": //댓글
                params.put("reply_id", String.valueOf(reply_id));   //댓글 수정시 필요 (값이 있으면 수정, 없으면 삽입
                params.put("reply_level", String.valueOf(reply_level));  //댓글 수정시 필요
                url = "Pc_board/add_reply";
                method =2;
        }

        transaction.queryExecute(method, params, url, callback);  //좋아요 체크 업데이트


    }

}
