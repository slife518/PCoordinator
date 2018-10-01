package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.company.jk.pcoordinator.mypage.mybaby.MybabyDetailFragment;
import com.company.jk.pcoordinator.record.RecordFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class MilkRiceListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<RecordHistoryinfo> data;
    private int layout;
    private String TAG = "MilkRiceListViewAdapter";

    public MilkRiceListViewAdapter(Context context, int layout, ArrayList<RecordHistoryinfo> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){
        Log.i(TAG, "데이터사이즈는 " + data.size());
        return data.size();
    }
    @Override
    public String getItem(int position){
        return data.get(position).id;
    }
    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View itemView, ViewGroup parent){

        if(itemView==null){
            itemView=inflater.inflate(layout,parent,false);
        }

        RecordHistoryinfo listviewitem=data.get(position);

        Log.i("Adapter", listviewitem.getTime());

        TextView mDate = (TextView) itemView.findViewById(R.id.tv_date);
        TextView mTime = (TextView) itemView.findViewById(R.id.tv_time);
        TextView mRice = (TextView) itemView.findViewById(R.id.tv_rice);
        TextView mMilk = (TextView) itemView.findViewById(R.id.tv_milk);
        TextView mComments = (TextView) itemView.findViewById(R.id.tv_comments);
        TextView mAuthor = (TextView) itemView.findViewById(R.id.tv_author);

        mDate.setText(listviewitem.getDate());
        mTime.setText(listviewitem.getTime());
        mRice.setText(listviewitem.getRice());
        mMilk.setText(listviewitem.getMilk());
        mComments.setText(listviewitem.getComments());
        mAuthor.setText(listviewitem.getAuthor());

        return itemView;
    }


}
