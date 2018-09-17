package com.company.jk.pcoordinator.home;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.jk.pcoordinator.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View v;
    private BarChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_home, container, false);

        mChart = v.findViewById(R.id.chart1);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);
        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        mChart.getAxisLeft().setDrawGridLines(false);

        mChart.getLegend().setEnabled(false);

        setData(10);
        mChart.setFitBars(true);
        return v;

    }


    private void setData(int count) {

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * count) + 15;
//            yVals.add(new BarEntry(i, (int) val));
//        }

        yVals.add(new BarEntry(1, new float[] { 30, 30 }));
        yVals.add(new BarEntry(2, new float[] { 10, 20 }));
        yVals.add(new BarEntry(3, new float[] { 20, 50 }));
//
        BarDataSet set = new BarDataSet(yVals, "Milk");

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(Color.rgb(192, 255, 140), Color.rgb(255, 247, 140));
//        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setDrawValues(false);

        BarData data = new BarData(set);

        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(800);
    }
}
