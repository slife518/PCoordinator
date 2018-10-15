package com.company.jk.pcoordinator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ParentsRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mEmail;
    public TextView mNickName;
    public TextView mRelationship;
    public TextView mBirthday;
    public ImageView mPicture;

    public ParentsRecyclerViewHolder(View itemView) {
        super(itemView);
        mPicture = (ImageView) itemView.findViewById(R.id.iv_profile);
        mEmail = (TextView) itemView.findViewById(R.id.email);
        mNickName = (TextView) itemView.findViewById(R.id.nickname);
        mRelationship = (TextView) itemView.findViewById(R.id.relationship);
        mBirthday = (TextView) itemView.findViewById(R.id.birthday);

    }
}
