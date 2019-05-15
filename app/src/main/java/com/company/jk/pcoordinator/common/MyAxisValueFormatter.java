package com.company.jk.pcoordinator.common;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter extends ValueFormatter {

    private DecimalFormat mFormat;

    public MyAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");

    }

    @Override
    public String getFormattedValue(float value) {
//        return mFormat.format(value) + " ml";
        return mFormat.format(value);

    }

}
