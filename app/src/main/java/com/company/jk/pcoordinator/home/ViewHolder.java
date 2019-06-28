package com.company.jk.pcoordinator.home;

import android.view.View;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.common.DayTextView;

public class ViewHolder {

    //view 획득
    public TextView mDate;
    DayTextView mDay;
    public TextView mTime;
//    public TextView mRice;
//    public TextView mMilk;
//    public TextView mMotherMilk;
    public TextView mEat;
    public TextView mComments;
    public TextView mAuthor;



    public ViewHolder(View root){

        //view 획득
        mDate = (TextView) root.findViewById(R.id.tv_date);
        mDay = root.findViewById(R.id.tv_day);
        mTime = (TextView) root.findViewById(R.id.tv_time);
//        mRice = (TextView) root.findViewById(R.id.tv_rice);
//        mMilk = (TextView) root.findViewById(R.id.tv_milk);
//        mMotherMilk = (TextView) root.findViewById(R.id.tv_mothermilk);
        mEat = (TextView) root.findViewById(R.id.tv_eat);
        mComments = (TextView) root.findViewById(R.id.tv_comments);
        mAuthor = (TextView) root.findViewById(R.id.tv_author);

    }

}
