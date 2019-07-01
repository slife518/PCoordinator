package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.company.jk.pcoordinator.R;

import java.util.ArrayList;
public class MilkRiceListViewAdapter extends BaseAdapter {

    private ArrayList<RecordHistoryinfo> data;
    private int layout;
    private String TAG = "MilkRiceListViewAdapter";
    Context context;

//    int COLOR[] = { Color.rgb(255,199,125), Color.rgb(242,188,248), Color.rgb(210,194,253), Color.rgb(222,250,187) };
    int COLOR[];


    public MilkRiceListViewAdapter(Context mContext, int layout, ArrayList<RecordHistoryinfo> arrayList){

        this.data=arrayList;
        this.layout=layout;
        context = mContext;
        COLOR = new int[]{ context.getResources().getColor(R.color.maintabledetail1),  context.getResources().getColor(R.color.maintabledetail2),  context.getResources().getColor(R.color.maintabledetail3)};
    }
    @Override
    public int getCount(){
        Log.d(TAG, "데이터사이즈는 " + data.size());
        return data.size();
    }
    @Override
    public String getItem(int position){
        return data.get(position).id;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //항목 데이터 획득
        RecordHistoryinfo listviewitem=data.get(position);
        ViewHolder holder;
        if(convertView==null){
            // 항목 layout 초기화
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

//        Log.d(TAG, "getView" + listviewitem.getDay());
        //view에 데이터 바인딩
        holder.mDate.setText(listviewitem.getDate());
        holder.mDay.setText(listviewitem.getDay());
        holder.mTime.setText(listviewitem.getAPTime());
//        holder.mRice.setText(listviewitem.getRice());
//        holder.mMilk.setText(listviewitem.getMilk());
//        holder.mMotherMilk.setText(listviewitem.getMothermilk());
        holder.mEat.setText(listviewitem.getEat());
        holder.mComments.setText(listviewitem.getComments());
        holder.mAuthor.setText(listviewitem.getAuthor());

//        convertView.setBackgroundColor(COLOR[Integer.parseInt(listviewitem.getDay()) % 3]);

//        Log.d(TAG, "holder.day : "+ listviewitem.getDay());
        return convertView;
    }

//
//    public void refreshAdapter(ArrayList<RecordHistoryinfo> newItems) {
//
//        this.data.clear();
//        this.data.addAll(newItems);
//        notifyDataSetChanged();
//    }
}
