package com.company.jk.pcoordinator.board.talk;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private UrlPath urlPath = new UrlPath();
    private LoginInfo loginInfo = LoginInfo.getInstance();
    private ArrayList<Talkinfo> mItems;

    public RecyclerViewAdapter(ArrayList itemList) {
        mItems = itemList;
    }


    // 필수 오버라이드 : view 생성, viewholder 호출
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_babycard, parent, false);
        mContext = parent.getContext();
        RecyclerViewHolder holder = new RecyclerViewHolder(v);

        return holder;
    }

    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.title.setText(mItems.get(position).title);
        holder.contents.setText(mItems.get(position).contents);
        holder.eyes.setText(mItems.get(position).eyes);
        holder.talks.setText(mItems.get(position).talks);
        holder.good.setText(mItems.get(position).good);
      //  holder.goodChecked.setText(mItems.get(position).goodChecked);



        String imgUrl = urlPath.getUrlBabyImg() + mItems.get(position).id + ".jpg";  //확장자 대소문자 구별함.

        Log.i(TAG, imgUrl);

        Picasso.with(mContext).invalidate(imgUrl);   //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.mPicture);;  //image가 reload 되도록 하기 위하여 필요함.


        // 이벤트처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, TalkDetailActivity.class);
                intent.putExtra("email", loginInfo.getEmail());
                intent.putExtra("baby_id", mItems.get(position).id);
                mContext.startActivity(intent);
            }
        });
    }

    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        Log.i(TAG, "아기는 " + mItems.size());
        return mItems.size();
    }
}
