package com.company.jk.pcoordinator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyDetailFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParentsRecyclerViewAdapter extends RecyclerView.Adapter<ParentsRecyclerViewHolder> {
    final static String TAG = "ParentsRecyclerViewAdapter";
    Context mContext;
    UrlPath urlPath = new UrlPath();
    LoginInfo loginInfo = LoginInfo.getInstance();
    private ArrayList<ParentsInfo> mItems;

    public ParentsRecyclerViewAdapter(ArrayList itemList) {
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
    public ParentsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_parentscard, parent, false);
        mContext = parent.getContext();
        ParentsRecyclerViewHolder holder = new ParentsRecyclerViewHolder(v);

        return holder;
    }

    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(@NonNull ParentsRecyclerViewHolder holder, final int position) {
        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.mNickName.setText(mItems.get(position).nickname);
        holder.mRelationship.setText(mItems.get(position).relationship);
        holder.mBirthday.setText(mItems.get(position).birthday);
//        holder.mFather.setText(mItems.get(position).father);
//        holder.mMother.setText(mItems.get(position).mother);
//
        String imgUrl = urlPath.getUrlBabyImg() + mItems.get(position).email + ".jpg";  //확장자 대소문자 구별함.
        Picasso.with(mContext).invalidate(imgUrl);   //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);  //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).into(holder.mPicture);


        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                MypageFragment myFragment = new MypageFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail(), mItems.get(position).email)).addToBackStack(null).commit();

            }
        });
    }

    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
