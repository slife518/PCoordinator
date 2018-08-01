package com.company.jk.pcoordinator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.http.HttpHandler2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    //DATA parsing 관련

    final static String Controller = "BuyTransation";
    final static String TAG = "MainActivity";
    StringBuffer sb = new StringBuffer();
    LoginInfo loginInfo = LoginInfo.getInstance();

    private static final String TAG_RESULTS="posts";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DATE = "regist_day";
    private static final String TAG_CONTENT = "content";
    JSONArray posts = null;
    ArrayList<HashMap<String,String>> noticeList;
    //UI 관련
    private RecyclerView rv;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        noticeList = new ArrayList<HashMap<String, String>>();
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(mLinearLayoutManager);

        new HttpTaskGetData().execute(loginInfo.getEmail());


    }
    /** JSON 파싱 메소드 **/
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params) {
                //JSON 받아온다.
                String uri = params[0];
                BufferedReader br = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;
                    while((json = br.readLine()) != null) {
                        sb.append(json+"\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String myJSON) {
                makeList(myJSON); //리스트를 보여줌
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
    /** JSON -> LIST 가공 메소드 **/
    public void makeList(String myJSON) {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0; i<posts.length(); i++) {
                //JSON에서 각각의 요소를 뽑아옴
                JSONObject c = posts.getJSONObject(i);
                String title = c.getString(TAG_TITLE);
                String writer = c.getString(TAG_WRITER);
                String date = c.getString(TAG_DATE);
                String content = c.getString(TAG_CONTENT);
                if(content.length() > 50 ) {
                    content = content.substring(0,50) + "..."; //50자 자르고 ... 붙이기
                }
                if(title.length() > 16 ) {
                    title = title.substring(0,16) + "..."; //18자 자르고 ... 붙이기
                }

                //HashMap에 붙이기
                HashMap<String,String> posts = new HashMap<String,String>();
                posts.put(TAG_TITLE,title);
                posts.put(TAG_WRITER,writer);
                posts.put(TAG_DATE,date);
                posts.put(TAG_CONTENT, content);

                //ArrayList에 HashMap 붙이기
                noticeList.add(posts);
            }
            //카드 리스트뷰 어댑터에 연결
            NoticeAdapter adapter = new NoticeAdapter(getApplicationContext(),noticeList);
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
            HttpHandler2 httpHandler = new HttpHandler2.Builder(Controller,"getBoardData").build();

            sb = httpHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//                JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");
                makeList(sb.toString()); //리스트를 보여줌

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            if (value != "") {   // 구매목록에 담겼으면 구매이력화면으로 이동
//                Intent intent = new Intent(Context, )
//                toastMessage = getString((R.string.Welcome));
//                intent.putExtra("jsReserved", String.valueOf(sb));
//                startActivity(intent);
//                finish();
            } else {
//                toastMessage = getString(R.string.Warnning);
            }
//            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        }


    }
}
