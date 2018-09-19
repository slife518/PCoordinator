package com.company.jk.pcoordinator.home;

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
import com.company.jk.pcoordinator.mypage.mybaby.MybabyDetailFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MilkRiceRecyclerViewAdapter extends RecyclerView.Adapter<MilkRiceRecyclerViewHolder> {
    final static String TAG = "RecyclerViewAdapter";
    Context mContext;
    UrlPath urlPath = new UrlPath();
    LoginInfo loginInfo = LoginInfo.getInstance();
    private ArrayList<RecordHistoryinfo> mItems;

    public MilkRiceRecyclerViewAdapter(ArrayList itemList) {
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
    public MilkRiceRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_milk_rice_card, parent, false);
        mContext = parent.getContext();
        MilkRiceRecyclerViewHolder holder = new MilkRiceRecyclerViewHolder(v);

        return holder;
    }

    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(@NonNull MilkRiceRecyclerViewHolder holder, final int position) {
        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.mDate.setText(mItems.get(position).date);
        holder.mTime.setText(mItems.get(position).time);
        holder.mRice.setText(mItems.get(position).rice);
        holder.mMilk.setText(mItems.get(position).milk);
        holder.mAuthor.setText(mItems.get(position).author);
        holder.mComments.setText(mItems.get(position).comments);

        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                HomeFragment myFragment = new HomeFragment();
                //왼쪽에서 오른쪽 슬라이드
                activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.frame, newInstance(loginInfo.getEmail(), mItems.get(position).id)).addToBackStack(null).commit();

            }
        });
    }

    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        Log.i(TAG, "조회 count " + mItems.size());
        return mItems.size();
    }
}
