package com.company.jk.pcoordinator;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.jk.pcoordinator.http.HttpHandler2;

import org.json.JSONObject;

/**
 * Created by Suleiman on 26-07-2015.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {
    final static String TAG = "BuyTransation";
    private Context context;
    final static String Controller = "BuyTransation";
    StringBuffer sb = new StringBuffer();
    LoginInfo loginInfo = LoginInfo.getInstance();

    int[] imgList = {R.drawable.two, R.drawable.one, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.nine, R.drawable.ten};    //남성복
    int[] imgList2 = { R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.two, R.drawable.one, R.drawable.three, R.drawable.four,
            R.drawable.nine, R.drawable.ten};     //여성복
    int[] imgList3 = {R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.two, R.drawable.one, R.drawable.three};     //아동복
    private int tabname;

    String[] nameList = {"와이셔츠", "정장바지", "팬티", "런닝", "정장양말", "캐주얼바지",
            "라운드티", "후드티", "집업", "잠옷"};    //남성복
    String[] nameList2 = {"치마", "바지", "팬티", "브라", "니트", "캐주얼바지",
            "라운드티", "후드티", "집업", "잠옷"};    //여성복
    String[] nameList3 = {"상의", "바지", "치마", "기저귀", "양말", "점퍼",
            "라운드티", "후드티", "집업", "잠옷"};    //아동복

    public MasonryAdapter(Context context, int tab ) {
        this.context = context;
        tabname = tab;
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        MasonryView masonryView = new MasonryView(layoutView);
        return masonryView;
    }

    @Override
    public void onBindViewHolder(MasonryView holder, final int position) {

        int[] imgArray = null;
        String[] nameArray = null;


        Log.i("onBindViewHolder 는", String.valueOf(tabname));
        //탭화면에 따라 다른이미지를 보여준다.
        switch (tabname) {
            case 1: imgArray = imgList.clone();
                     nameArray = nameList.clone();
                     break;
            case 2: imgArray = imgList2.clone();
                     nameArray = nameList2.clone();
                     break;
            case 3: imgArray = imgList3.clone();
                     nameArray = nameList3.clone();
                     break;
        }

        holder.imageView.setImageResource(imgArray[position]);
        holder.textView.setText(nameArray[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(loginCheck(position)) {
                    new HttpTaskSignIn().execute(loginInfo.getEmail(), String.valueOf(position));
                }else{
                    //로그인되어 있지 않으면 로그인 화면으로 이동
                }
                //Toast.makeText( context, "Clicked at position" + position, Toast.LENGTH_LONG).show();
            }
        });

    }

    private Boolean loginCheck(int selected) {

        if(loginInfo.getEmail() == null){   //로그인 되어 있지 않으면 팝업창을 띄여서 로그인 하도록 유도 후 로그인 화면으로 이동한다.
            show(selected);
            return  false;
        }
        return  true;
    }


    void show(final int selected)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("로그인 후에 구매 가능합니다.");
        builder.setMessage("로그인 하시겠습니까");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context.getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class );
                        intent.putExtra("selectedItem", String.valueOf(selected));
                context.startActivity(intent);
                //finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context.getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    class MasonryView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MasonryView(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img);
            textView = (TextView) itemView.findViewById(R.id.img_name);

        }
    }

    //  DB 쓰레드 작업
    class HttpTaskSignIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            String result = "";
            HttpHandler2 httpHandler = new HttpHandler2.Builder(Controller,"buyItem").email(args[0]).itemCode(args[1]).build();

            sb = httpHandler.getData();
            try {
                //결과값에 jsonobject 가 두건 이상인 경우 한건 조회
//                JSONObject jsonObject = httpHandler.getNeedJSONObject(sb, "result");

                //결과값이 한건의 json인 경우
                JSONObject jsonObject = new JSONObject(sb.toString());
                result = jsonObject.getString("result");
                Log.i(TAG, result);
                //  String level = jsonObject.getString("level");
                //  String birthday = jsonObject.getString("birthday");
                if (result !=""){
                   // loginInfo.setName(result);
                    //  loginInfo.setBirthday(birthday);
                    //  loginInfo.setLevel(level);

                }
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
