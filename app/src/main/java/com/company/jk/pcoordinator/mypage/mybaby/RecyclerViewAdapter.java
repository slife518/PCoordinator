package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.MypageFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    final static String TAG = "RecyclerViewAdapter";
    Context mContext;
    UrlPath urlPath = new UrlPath();
    LoginInfo loginInfo = LoginInfo.getInstance();
    private ArrayList<Mybabyinfo> mItems;

    public RecyclerViewAdapter(ArrayList itemList) {
        mItems = itemList;
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String param1, String param2) {
        MybabyDetailFragment fragment = new MybabyDetailFragment();
        Bundle args = new Bundle();
        args.putString("email", param1);
        args.putString("baby_id", param2);
        fragment.setArguments(args);
        return fragment;
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
        holder.mName.setText(mItems.get(position).name);
        holder.mSex.setText(mItems.get(position).sex);
        holder.mBirthday.setText(mItems.get(position).birthday);
//        holder.mFather.setText(mItems.get(position).father);
//        holder.mMother.setText(mItems.get(position).mother);
//
        String imgUrl = urlPath.getUrlBabyImg() + mItems.get(position).id + ".JPG";  //확장자 대소문자 구별함.
        Log.i(TAG, imgUrl);
        Picasso.with(mContext).load(imgUrl).into(holder.mPicture);


        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail(), mItems.get(position).id)).addToBackStack(null).commit();

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
