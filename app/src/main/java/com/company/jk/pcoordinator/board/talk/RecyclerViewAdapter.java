package com.company.jk.pcoordinator.board.talk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.MyDataTransaction;
import com.company.jk.pcoordinator.common.VolleyCallback;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private UrlPath urlPath = new UrlPath();
    private LoginInfo loginInfo;
    private ArrayList<Talkinfo> mItems;
    MyDataTransaction transaction;
    public RecyclerViewAdapter(ArrayList itemList) {
        mItems = itemList;
    }


    // 필수 오버라이드 : view 생성, viewholder 호출
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_talkcard, parent, false);
        mContext = parent.getContext();
        loginInfo = LoginInfo.getInstance(mContext);
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }

    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.title.setText(mItems.get(position).title);
        holder.author.setText(mItems.get(position).author);
        holder.contents.setText(mItems.get(position).contents);
        holder.eyes.setText(String.valueOf(mItems.get(position).eyes));
        holder.talks.setText(String.valueOf(mItems.get(position).talks));
        holder.good.setText(String.valueOf(mItems.get(position).good));
        holder.createDate.setText(String.valueOf(mItems.get(position).createDate));
      //  holder.goodChecked.setText(mItems.get(position).goodChecked);

        final String imgUrl = urlPath.getUrlTalkImg() + mItems.get(position).id + "_" + mItems.get(position).reply_id + "_" + mItems.get(position).reply_level +  ".jpg";  //확장자 대소문자 구별함(무조건 소문자 jpg 사용할 것.
//        Log.i(TAG, imgUrl);

        Picasso.with(mContext).invalidate(imgUrl);   //image가 reload 되도록 하기 위하여 필요함.
//        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.mPicture);  //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.mPicture, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                holder.mPicture.setVisibility(View.VISIBLE);
            }
            @Override
            public void onError() {
                holder.mPicture.setVisibility(View.GONE);
            }
        });

        // 이벤트처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, TalkDetailActivity.class);
                intent.putExtra("id", mItems.get(position).id);
                intent.putExtra("author_email", mItems.get(position).email);
                mContext.startActivity(intent);
            }
        });

        holder.iv_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItems.get(position).getGoodChecked()){
                    mItems.get(position).setGoodChecked(false);
                    holder.good.setText(String.valueOf(--mItems.get(position).good));
                }else{
                    mItems.get(position).setGoodChecked(true);
                    holder.good.setText(String.valueOf(++mItems.get(position).good));
                }
                update_good(mItems.get(position).id,  mItems.get(position).getGoodChecked());

            }
        });

        holder.good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItems.get(position).getGoodChecked()){
                    mItems.get(position).setGoodChecked(false);
                    holder.good.setText(String.valueOf(--mItems.get(position).good));
                }else{
                    mItems.get(position).setGoodChecked(true);
                    holder.good.setText(String.valueOf(++mItems.get(position).good));
                }
                update_good(mItems.get(position).id,  mItems.get(position).getGoodChecked());

            }
        });

    }


    private void update_good(int id, boolean goodStatus){

        Map<String, String> params = new HashMap<>();
        params.put("email", loginInfo.getEmail());
        params.put("id", String.valueOf(id));
        params.put("reply_id", "0");
        params.put("reply_level", "0");

        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result, int method) {  // 성공이면 result = 1

                Log.i(TAG, "onSuccessResponse 결과값은" + result + method);
                switch (method){
                    case 2:  //get_data
                        break;
                } }
            @Override
            public void onFailResponse(VolleyError error) {
                Log.d(TAG, "에러발생 원인은 " + error.getLocalizedMessage());
            }
        };
        String url = "";
        if (goodStatus){
            url = "Pc_board/add_talk_good";
        }else {
            url = "Pc_board/delete_talk_good";
        }

        transaction = new MyDataTransaction(mContext);
        transaction.queryExecute(2, params, url, callback);  //좋아요 체크 업데이트
    }

    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
