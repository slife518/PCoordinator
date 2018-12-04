package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.graphics.Color;
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

    private ArrayList<RecordHistoryinfo> data;
    private int layout;
    private String TAG = "MilkRiceListViewAdapter";
    Context context;
    DriveHolder holder;
    int COLOR[] = { Color.rgb(153,255,153), Color.rgb(255,000,255), Color.rgb(255,255,153), Color.rgb(204,153,255) };




    public MilkRiceListViewAdapter(Context mContext, int layout, ArrayList<RecordHistoryinfo> data){

        this.data=data;
        this.layout=layout;
        context = mContext;
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


        //항목 데이터 획득
        RecordHistoryinfo listviewitem=data.get(position);


        if(itemView==null){
            // 항목 layout 초기화
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView=inflater.inflate(layout,parent,false);
            holder = new DriveHolder(itemView);
            itemView.setTag(holder);
        }

        Log.i("Adapter", listviewitem.getTime());


        //view에 데이터 바인딩
        holder.mDate.setText(listviewitem.getDate());
        holder.mTime.setText(listviewitem.getTime());
        holder.mRice.setText(listviewitem.getRice());
        holder.mMilk.setText(listviewitem.getMilk());
        holder.mComments.setText(listviewitem.getComments());
        holder.mAuthor.setText(listviewitem.getAuthor());

        String day = listviewitem.getDate().substring(3,5);

        Log.i(TAG, day);
        itemView.setBackgroundColor(COLOR[Integer.parseInt(day) % 3]);

        return itemView;
    }


}
