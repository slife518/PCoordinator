package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
public class MilkRiceListViewAdapter extends BaseAdapter {

    private ArrayList<RecordHistoryinfo> data;
    private int layout;
    private String TAG = "MilkRiceListViewAdapter";
    Context context;

    int COLOR[] = { Color.rgb(209,196,233), Color.rgb(187,222,251), Color.rgb(255,205,210), Color.rgb(225,190,231) };


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
            DriveHolder holder = new DriveHolder(itemView);
            itemView.setTag(holder);
        }

//        Log.i("Adapter", listviewitem.getTime());

        DriveHolder holder = (DriveHolder)itemView.getTag();

        //view에 데이터 바인딩
        holder.mDate.setText(listviewitem.getDate());
        holder.mTime.setText(listviewitem.getTime());
        holder.mRice.setText(listviewitem.getRice());
        holder.mMilk.setText(listviewitem.getMilk());
        holder.mMotherMilk.setText(listviewitem.getMothermilk());
        holder.mComments.setText(listviewitem.getComments());
        holder.mAuthor.setText(listviewitem.getAuthor());

        String day = listviewitem.getDate().substring(3,5);

//        Log.i(TAG, day);
        itemView.setBackgroundColor(COLOR[Integer.parseInt(day) % 3]);

        return itemView;
    }


    public void refreshAdapter(ArrayList<RecordHistoryinfo> newItems) {

        this.data.clear();
        this.data.addAll(newItems);
        notifyDataSetChanged();
    }
}
