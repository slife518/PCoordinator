package com.company.jk.pcoordinator.board.talk;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public TextView author;
    public ImageView mPicture;
    public ImageView iv_good;
    public ImageView iv_function;
    public LinearLayout layout_rere;
    public LinearLayout layout_main;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mPicture = itemView.findViewById(R.id.iv_image);
        iv_good = itemView.findViewById(R.id.iv_good);
        iv_function = itemView.findViewById(R.id.iv_function);
        title = itemView.findViewById(R.id.tv_title);
        author = itemView.findViewById(R.id.tv_author);
        contents =  itemView.findViewById(R.id.tv_contents);
        talks = itemView.findViewById(R.id.tv_chatcount);
        eyes = itemView.findViewById(R.id.tv_eyecount);
        good = itemView.findViewById(R.id.tv_goodcount);
        createDate = itemView.findViewById(R.id.tv_createDate);

        layout_rere = itemView.findViewById(R.id.layout_rere);
        layout_main = itemView.findViewById(R.id.layout_main);

    }
}
