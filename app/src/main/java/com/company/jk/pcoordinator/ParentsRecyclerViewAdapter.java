package com.company.jk.pcoordinator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.company.jk.pcoordinator.http.UrlPath;
import com.company.jk.pcoordinator.login.LoginInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ParentsRecyclerViewAdapter extends RecyclerView.Adapter<ParentsRecyclerViewHolder>{
    final static String TAG = "ParentsRecwAdapter";
    Context mContext;
    UrlPath urlPath = new UrlPath();
    LoginInfo loginInfo = LoginInfo.getInstance();
    private ArrayList<ParentsInfo> mItems;

    public ParentsRecyclerViewAdapter(ArrayList itemList) {
        mItems = itemList;
    }


    // 필수 오버라이드 : view 생성, viewholder 호출
    @NonNull
    @Override
    public ParentsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_parentscard, parent, false);
        mContext = parent.getContext();
        ParentsRecyclerViewHolder holder = new ParentsRecyclerViewHolder(v);

        return holder;
    }

    // 필수 오버라이드 : 재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(@NonNull ParentsRecyclerViewHolder holder, final int position) {
        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
        holder.mNickName.setText(mItems.get(position).nickname);
//        holder.mRelationship.setText(mItems.get(position).relationship);
        holder.mBirthday.setText(mItems.get(position).birthday);
        holder.mEmail.setText(mItems.get(position).email);
//        holder.mFather.setText(mItems.get(position).father);
//        holder.mMother.setText(mItems.get(position).mother);
        if (loginInfo.getEmail().equals(mItems.get(position).email)){
            holder.mDelete.setVisibility(View.GONE);
        }
//

        String imgUrl = urlPath.getUrlMemberImg() + mItems.get(position).email + ".jpg";  //확장자 대소문자 구별함.
        Picasso.with(mContext).invalidate(imgUrl);   //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE);  //image가 reload 되도록 하기 위하여 필요함.
        Picasso.with(mContext).load(imgUrl).into(holder.mPicture);


        // 이벤트처리 : 생성된 List 중 선택된 목록번호를 Toast로 출력
//        holder.itemView.setOnClickListener(this);
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click 클릭되었습니다.");
                popup_to_confirm_delete( mItems.get(position).email, mItems.get(position).baby_id, position);
                Log.i(TAG, "삭제");

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "아이템클릭");
            }
        });
    }

    // 필수 오버라이드 : 데이터 갯수 반환
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private  void popup_to_confirm_delete(final String email, final  int baby_id, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.message_confirm_delete);
        builder.setMessage(R.string.message_confirm_delete_parents);
        builder.setPositiveButton(R.string.parent_yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {  //예 선택
                        delete_parents(email, baby_id, position);
                    }
                });
        builder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(mContext.getApplicationContext(),"취소되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    private  void  delete_parents(final String email, final int baby_id, final int position){
        String server_url = new UrlPath().getUrlPath() + "Pc_baby/delete_relation";
        Log.i(TAG, server_url);
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "결과값은 " + response);
                mItems.remove(position);
                notifyItemChanged(position);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "아기아이디는 " + baby_id);
                Log.e(TAG, error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("baby_id", String.valueOf(baby_id));

                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

    }


}
