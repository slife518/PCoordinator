package com.company.jk.pcoordinator.common;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter extends ValueFormatter
{

    private DecimalFormat mFormat;
    
    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value) {

        if(value==0){
            return "";
        }else {
            return mFormat.format(value);
        }
//        return mFormat.format(value) + " ml";

    }
}
