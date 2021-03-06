package com.company.jk.pcoordinator.mypage.parents;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.company.jk.pcoordinator.R;

public class ParentsRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mEmail;
    public TextView mNickName;
//    public TextView mRelationship;
    public TextView mBirthday;
    public ImageView mPicture;
    public ImageButton mDelete;

    public ParentsRecyclerViewHolder(View itemView) {
        super(itemView);
        mPicture = (ImageView) itemView.findViewById(R.id.iv_profile);
        mEmail = (TextView) itemView.findViewById(R.id.email);
        mNickName = (TextView) itemView.findViewById(R.id.nickname);
//        mRelationship = (TextView) itemView.findViewById(R.id.relationship);
        mBirthday = (TextView) itemView.findViewById(R.id.birthday);
        mDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);

    }
}
