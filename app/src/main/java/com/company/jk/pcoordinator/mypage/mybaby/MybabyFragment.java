package com.company.jk.pcoordinator.mypage.mybaby;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.R;

import java.util.ArrayList;


public class MybabyFragment extends Fragment {
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerViewAdapter mAdapter;
    View v;
    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mybaby, container, false);
        mContext = v.getContext();
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_main);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // ArrayList 에 Item 객체(데이터) 넣기
        ArrayList<Mybabyinfo> items = new ArrayList();
        items.add(new Mybabyinfo("1","조민준", "2017년8월1일", "남자", "조정국", "배윤지"));
        items.add(new Mybabyinfo("2","조민준", "2017년8월1일", "남자", "조정국", "배윤지"));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
       // mRecyclerView.addItemDecoration(new RecyclerViewDecoration(this, RecyclerViewDecoration.VERTICAL_LIST));
        // Adapter 생성
        mAdapter = new RecyclerViewAdapter(items);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

}
