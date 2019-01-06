package com.company.jk.pcoordinator.home;

import java.io.Serializable;

/**
 * Created by jungkukjo on 06/01/2019.
 */

public class RecordChartInfo implements Serializable {


    private String date;
    private int mothermilk;
    private int milk;
    private int rice;


    public RecordChartInfo(String date, int mothermilk, int milk, int rice){

        this.date = date.substring(5,10);
        this.milk = milk;
        this.mothermilk = mothermilk;
        this.rice = rice;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMothermilk() {
        return mothermilk;
    }

    public void setMothermilk(int mothermilk) {
        this.mothermilk = mothermilk;
    }

    public int getMilk() {
        return milk;
    }

    public void setMilk(int milk) {
        this.milk = milk;
    }

    public int getRice() {
        return rice;
    }

    public void setRice(int rice) {
        this.rice = rice;
    }
}
