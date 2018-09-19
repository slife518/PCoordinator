package com.company.jk.pcoordinator.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;

public class MilkRiceRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mDate;
    public TextView mTime;
    public TextView mRice;
    public TextView mMilk;
    public TextView mAuthor;
    public TextView mComments;


    public MilkRiceRecyclerViewHolder(View itemView) {
        super(itemView);
        mDate = (TextView) itemView.findViewById(R.id.tv_date);
        mTime = (TextView) itemView.findViewById(R.id.tv_time);
        mRice = (TextView) itemView.findViewById(R.id.tv_rice);
        mMilk = (TextView) itemView.findViewById(R.id.tv_milk);
        mComments = (TextView) itemView.findViewById(R.id.tv_comments);
        mAuthor = (TextView) itemView.findViewById(R.id.tv_author);

    }
}
