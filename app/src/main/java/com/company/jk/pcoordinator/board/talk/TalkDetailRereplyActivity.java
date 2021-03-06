package com.company.jk.pcoordinator.board.talk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.JsonParse;
import com.company.jk.pcoordinator.common.MyActivity;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TalkDetailRereplyActivity extends MyActivity implements View.OnClickListener, BottomRecyclerViewAdapter.BottomSheetListener, RecyclerDetailViewAdapter.BottomSheetCallListener{

    RecyclerView mRecyclerView;
    ArrayList<Talkinfo> items = new ArrayList();
    LinearLayoutManager mLayoutManager;
    RecyclerDetailRereplyViewAdapter mAdapter;
    BottomSheetDialog modalBottomSheet;
    Toolbar myToolbar;
    MyDataTransaction transaction;
    LoginInfo loginInfo;
    String TAG = "TalkDetailRereplyActivity";
    TextView tv_register;
    EditText et_reply;
   int id = 0, reply_id = 0, reply_level = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_detail);
        loginInfo = LoginInfo.getInstance(this);
        transaction = new MyDataTransaction(getApplicationContext());
// Toolbar를 생성한다.
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getSupportActionBar().setTitle(getResources().getString(R.string.talklist));
//        myToolbar.setTitleTextAppearance(getApplicationContext(), R.style.toolbarTitle);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = findViewById(R.id.listView_main);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        mAdapter = new RecyclerDetailRereplyViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        findviewByid();
        tv_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
            update_contents("reply");
    }

    private void findviewByid(){
        tv_register = findViewById(R.id.tv_register);
        et_reply = findViewById(R.id.tv_reply);
    }
    public void get_data(){
        Intent intent = getIntent();
        Map<String, String> params = new HashMap<>();

        id = intent.getExtras().getInt("id");
        reply_id = intent.getExtras().getInt("reply_id");

        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));
        params.put("reply_id", String.valueOf(reply_id));

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
        transaction.queryExecute(2, params, "Pc_board/get_talk_detail_reply", callback);
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

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.iconColorDarkBackground), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    private void responseSuccess(String response) {
        Log.d(TAG, "결과값은 " + response);
        items.clear();
        JSONArray jsonArray = JsonParse.getJsonArrayFromString(response, "result");
        set_recyclerview(jsonArray);

        mAdapter.notifyDataSetChanged();
    }

    private void set_recyclerview(JSONArray jsonArray){   //recyclerview

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject rs = (JSONObject) jsonArray.get(i);
                items.add(new Talkinfo( rs.getInt("id")
                        , rs.getInt("reply_id")
                        , rs.getInt("reply_level")
                        , rs.getString("title")
                        , rs.getString("author")
                        , rs.getString("email")
                        , rs.getString("contents")
                        , rs.getInt("eyes")
                        , rs.getInt("talk")
                        , rs.getInt("good")
                        , rs.getBoolean("goodChecked")
                        , rs.getString("createDate")
                ));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
//
//    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //return super.onOptionsItemSelected(item);
//        switch (item.getItemId()) {
//            case R.id.action_add:
//
//                createDialog();
//
////                Intent intent = new Intent(this, NewTalkActivity.class);
////                intent.putExtra("email",loginInfo.getEmail() );
////                intent.putExtra("id", id);
////                startActivityForResult(intent, 300);
//                return true;
//
//            default:
//                onBackPressed();
//        }
//        return true;
//
//    }

    private void update_contents(String gubun){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));
        params.put("reply_id", String.valueOf(reply_id));
        params.put("reply_level", String.valueOf(reply_level));

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
                switch (method){
                    case 2:  //댓글추가 완료
//                        items.add(new Talkinfo(id, reply_id, reply_level,"", loginInfo.getName(), loginInfo.getEmail(),et_reply.getText().toString(), 0, 0, 0, false, getResources().getString(R.string.now)));
                        get_data();
                        break;

                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        params.put("id", String.valueOf(id));   //댓글 수정시 필요 (값이 있으면 수정, 없으면 삽입
        params.put("reply_id", String.valueOf(reply_id));   //댓글 수정시 필요 (값이 있으면 수정, 없으면 삽입
        params.put("reply_level", String.valueOf(reply_level));  //댓글 수정시 필요
        params.put("contents", et_reply.getText().toString());  //댓글 수정시 필요
        params.put("email", loginInfo.getEmail());  //댓글 수정시 필요
        String url = "Pc_board/add_reply";
        int method =2;

        transaction.queryExecute(method, params, url, callback);

        et_reply.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_reply.getWindowToken(), 0);


    }


//    private void createDialog(){    // 게시글 수정/삭제
//        List<DataVO> list=new ArrayList<>();
//        DataVO vo=new DataVO();
//        vo.title=getResources().getString(R.string.update);
//        vo.tcode = "modify";
//        vo.id = id;
//        vo.reply_id = reply_id;
//        vo.reply_level = reply_level;
//        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_create_24px, null);
//        list.add(vo);
//
//        vo=new DataVO();
//        vo.title=getResources().getString(R.string.delete);
//        vo.tcode = "delete";
//        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_delete_24px, null);
//        vo.id = id;
//        vo.reply_id = reply_id;
//        vo.reply_level = reply_level;
//        list.add(vo);
//
//
//        BottomRecyclerViewAdapter adapter=new BottomRecyclerViewAdapter(list);
//        View view=getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
//        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.lab4_recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//
//
//        modalBottomSheet = new BottomSheetDialog(this);
//        modalBottomSheet.setContentView(view);
//        modalBottomSheet.show();
//
//    }

    @Override
    public void onButtonClicked(String tcode) {
    }

    @Override
    public void onButtonClicked(String tcode, int id, int reply_id, int reply_level) {
        switch (tcode){
            case "delete" :  // 삭제하기;
                deleteAelrtDialog();
                break;
        }

        modalBottomSheet.dismiss();
    }

    private void delete_talk(){

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("reply_id", String.valueOf(reply_id));
        params.put("reply_level", String.valueOf(reply_level));

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.d(TAG, "onSuccessResponse 결과값은" + result + method);
                switch (method){
                    case 1:  //get_data

                        //onBackPressed();
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        String url = "";
        int method = 1;
        url = "Pc_board/delete_talk";

        MyDataTransaction transaction;
        transaction = new MyDataTransaction(this.getApplicationContext());
        transaction.queryExecute(method, params, url, callback);
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
                .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete_talk();
                        onBackPressed();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void createDialog(String email, int id, int reply_id, int reply_level) {
        List<DataVO> list=new ArrayList<>();
        DataVO vo=new DataVO();
        vo.title=getResources().getString(R.string.delete);
        vo.tcode = "delete";   //대댓글 달기
        vo.id = id;
        vo.reply_id = reply_id;
        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_create_24px, null);
        list.add(vo);

        BottomRecyclerViewAdapter adapter=new BottomRecyclerViewAdapter(list);
        View view=getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.lab4_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        modalBottomSheet = new BottomSheetDialog(this);
        modalBottomSheet.setContentView(view);
        modalBottomSheet.show();

    }


}
