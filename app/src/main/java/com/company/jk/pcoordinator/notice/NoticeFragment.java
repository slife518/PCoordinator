package com.company.jk.pcoordinator.notice;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyFragment;
import com.company.jk.pcoordinator.login.LoginService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NoticeFragment extends MyFragment {
    //DATA parsing 관련

    private static final String Controller = "Pc_BuyTransation";
    private static final String TAG = "NoticeFragment";
    StringBuffer sb = new StringBuffer();

    private static final String TAG_RESULTS="posts";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "registDay";
    private static final String TAG_CONTENT = "content";

    ArrayList<HashMap<String,String>> noticeList;
    //UI 관련
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;
    Context mContext;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        v = inflater.inflate(R.layout.fragment_notice, container, false);

        mContext = getActivity();

        return  v;

    }

    @Override
    public void onResume() {

        noticeList = new ArrayList<HashMap<String, String>>();
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView)v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        new HttpTaskGetData().execute();

        super.onResume();
    }

    /** JSON -> LIST 가공 메소드 **/
    public void makeList(String myJSON) {
        try {

            Log.d(TAG, "여기" + myJSON);

            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray  posts = jsonObj.getJSONArray("rs");

            for(int i=0; i<posts.length(); i++) {

                Log.d(TAG, "makeList" + posts.length());
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String writer = c.getString(TAG_WRITER);
                String date = c.getString(TAG_DATE);
                String content = c.getString(TAG_CONTENT);
//                if(content.length() > 50 ) {
//                    content = content.substring(0,50) + "..."; //50자 자르고 ... 붙이기
//                }
//                if(title.length() > 16 ) {
//                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
//                }

                //HashMap에 붙이기
                HashMap<String,String> hmposts = new HashMap<String,String>();
                hmposts.put(TAG_TITLE,title);
                hmposts.put(TAG_WRITER,writer);
                hmposts.put(TAG_DATE,date);
                hmposts.put(TAG_CONTENT, content);

                //ArrayList에 HashMap 붙이기
                noticeList.add(hmposts);
            }
            //카드 리스트뷰 어댑터에 연결
            NoticeAdapter adapter = new NoticeAdapter(getContext().getApplicationContext(),noticeList);
            Log.e("onCreate[noticeList]", "" + noticeList.size());
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();



        }catch(JSONException e) {
            e.printStackTrace();
        }
    }



    //  DB 쓰레드 작업
    class HttpTaskGetData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            String result = "";
            LoginService httpHandler = new LoginService.Builder(Controller,"getBoardData").build();

            sb = httpHandler.getData();

            Log.d(TAG, sb.toString());
            try {

                result = sb.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            if (value != "") {
                makeList(sb.toString());
            } else {
                //Toast.makeText(NoticeFragment.this, "공지사항이 없습니다.", Toast.LENGTH_SHORT).show();
            }

        }


    }
}
