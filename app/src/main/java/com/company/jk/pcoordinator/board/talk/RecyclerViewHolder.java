package com.company.jk.pcoordinator.board.talk;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
//    public TextView mID;
    public TextView title;
    public TextView contents;
    public TextView eyes;
    public TextView talks;
    public TextView good;
    public TextView createDate;
    public ImageView mPicture;
    public ImageView iv_good;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mPicture = itemView.findViewById(R.id.iv_image);
        iv_good = itemView.findViewById(R.id.iv_good);
        title = itemView.findViewById(R.id.tv_title);
        contents =  itemView.findViewById(R.id.tv_contents);
        talks = itemView.findViewById(R.id.tv_chatcount);
        eyes = itemView.findViewById(R.id.tv_eyecount);
        good = itemView.findViewById(R.id.tv_goodcount);
        createDate = itemView.findViewById(R.id.tv_createDate);

    }
}
