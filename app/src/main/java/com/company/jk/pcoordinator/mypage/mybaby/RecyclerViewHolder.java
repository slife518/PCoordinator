package com.company.jk.pcoordinator.mypage.mybaby;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
//    public TextView mID;
    public TextView mName;
    public TextView mSex;
    public TextView mBirthday;
//    public TextView mMother;
//    public TextView mFather;
    public ImageView mPicture;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mPicture = (ImageView) itemView.findViewById(R.id.date);
//        mID = (TextView) itemView.findViewById(R.id.ID);
        mName = (TextView) itemView.findViewById(R.id.baby_name);
        mSex = (TextView) itemView.findViewById(R.id.sex);
        mBirthday = (TextView) itemView.findViewById(R.id.birthday);
//        mMother = (TextView) itemView.findViewById(R.id.mother_name);
//        mFather = (TextView) itemView.findViewById(R.id.father_name);

    }
}
