package com.company.jk.pcoordinator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jizard.testapplication.adapter.ReplyListAdapter;
import com.example.jizard.testapplication.datatype.ReplyData;
import com.example.jizard.testapplication.parser.ReplyParser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by gold24park on 2016. 4. 5..
 */
public class MainActivity extends Activity {

    private final String TAG_TYPE = "notice";

    private String title,writer,regist_day,r_count,content,img1,img2,img3;
    private TextView tv_title,tv_content,tv_count,tv_writer,tv_date;
    private ImageView iv_img1,iv_img2,iv_img3;
    private View header;
    private static ListView replyListView;
    private static String cnum, id;
    private static ReplyListAdapter replyListAdapter;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        receiveIntentData();

        try {
            updateReplyList();
            loadReplyFragment();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //뷰에 정보 세팅 & 출력
        tv_title.setText(title);
        tv_writer.setText(writer);
        tv_date.setText(regist_day);
        tv_content.setText(content);
        tv_count.setText(r_count);

        loadImage1(img1);
        loadImage2(img2);
        loadImage3(img3);

        iv_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(context.getApplicationContext(), "클릭1", Toast.LENGTH_SHORT);
            }
        });
        iv_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "클릭2", Toast.LENGTH_SHORT);
            }
        });
        iv_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(), "클릭3", Toast.LENGTH_SHORT);
            }
        });
    }

    public static void updateReplyList() throws ExecutionException, InterruptedException {
        String url = "http://pho901121.cafe24.com/login/db_get_reply.php?cnum=" + cnum + "&id=" + id;
        ReplyParser replyParser = new ReplyParser(context);
        ArrayList<ReplyData> replyList = replyParser.execute(url).get();
        replyListAdapter = new ReplyListAdapter(context, replyList);
        replyListView.setAdapter(replyListAdapter);
        replyListAdapter.notifyDataSetChanged();
    }

    public void loadReplyFragment() {
        HashMap<String, String> postInfomation = new HashMap<String, String>();
        postInfomation.put("id", id);
        postInfomation.put("cnum", cnum);
        postInfomation.put("type", TAG_TYPE);

        Fragment fragment = new ReplyFragment().addInformation(postInfomation);
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_reply_fragment, fragment); //fragment 지운 후 add
        transaction.commit();
    }

    public void loadImage1(String url) {
        if(!url.isEmpty()) {
            Picasso.with(this).load(url).into(iv_img1);
            Toast.makeText(context, "image: " + url, Toast.LENGTH_SHORT).show();
        }
    }
    public void loadImage2(String url) {
        if(!url.isEmpty()) {
            Picasso.with(this).load(url).into(iv_img2);
        }
    }
    public void loadImage3(String url) {
        if(!url.isEmpty()) {
            Picasso.with(this).load(url).into(iv_img3);
        }
    }


    public void init() {
        context = getApplicationContext();
        header = getLayoutInflater().inflate(R.layout.header_notice, null);
        replyListView = (ListView) findViewById(R.id.lv_reply);
        replyListView.addHeaderView(header);
        tv_title = (TextView) header.findViewById(R.id.tv_title);
        tv_writer = (TextView) header.findViewById(R.id.tv_writer);
        tv_date = (TextView) header.findViewById(R.id.tv_date);
        tv_count = (TextView) header.findViewById(R.id.tv_count);
        tv_content = (TextView) header.findViewById(R.id.tv_content);
        iv_img1 = (ImageView) header.findViewById(R.id.iv_img1);
        iv_img2 = (ImageView) header.findViewById(R.id.iv_img2);
        iv_img3 = (ImageView) header.findViewById(R.id.iv_img3);
    }

    public void receiveIntentData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        writer = intent.getStringExtra("writer");
        regist_day = intent.getStringExtra("regist_day");
        r_count = intent.getStringExtra("r_count");
        content = intent.getStringExtra("content");
        cnum = intent.getStringExtra("cnum");
        img1 = intent.getStringExtra("img1");
        img2 = intent.getStringExtra("img2");
        img3 = intent.getStringExtra("img3");
        id = intent.getStringExtra("id");
    }
}


출처: http://jizard.tistory.com/40 [JIZARD]