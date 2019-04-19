package com.company.jk.pcoordinator.board.talk;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.jk.pcoordinator.R;
import com.company.jk.pcoordinator.login.LoginInfo;

import java.util.List;

public class BottomRecyclerViewAdapter extends RecyclerView.Adapter<ItemHolder> {
    private List<DataVO> list;
    private Context mContext;
    BottomSheetListener mListener;
    LoginInfo loginInfo ;


    String TAG = "BottomRecyclerViewAdapter";
    public BottomRecyclerViewAdapter(List<DataVO> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bottom_sheet_row, parent, false);
        mContext = parent.getContext();
        loginInfo = LoginInfo.getInstance(mContext);
        mListener = (BottomSheetListener)mContext;
        return new ItemHolder(root);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        final DataVO vo=list.get(position);
        holder.textView.setText(vo.title);
        holder.imageView.setImageDrawable(vo.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
               public void onClick(View v) {
                switch (vo.tcode){
                    case "rereply":
                        mListener.onButtonClicked(vo.tcode, vo.id, vo.reply_id, vo.reply_level);
                        break;
                    case "delreply":
                        mListener.onButtonClicked(vo.tcode, vo.id, vo.reply_id, vo.reply_level);
                        break;
                    default:
                        mListener.onButtonClicked(vo.tcode);
                }


               }
        });
    }

    public interface BottomSheetListener{
            void onButtonClicked(String tcode);
            void onButtonClicked(String tcode, int id, int reply_id, int reply_level);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ItemHolder extends RecyclerView.ViewHolder{

    TextView textView;
    ImageView imageView;

    public ItemHolder(View root) {
        super(root);
        imageView = (ImageView) itemView.findViewById(R.id.bottom_sheet_row_imageView);
        textView = (TextView) itemView.findViewById(R.id.bottom_sheet_row_textView);
    }
}
class DataVO {
    String title;
    String tcode;   //기능코드
    Drawable image;
    int id, reply_id, reply_level;
}
