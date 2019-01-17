package com.company.jk.pcoordinator.home;

import java.io.Serializable;

/**
 * Created by jungkukjo on 06/01/2019.
 */

public class RecordChart2Info implements Serializable {


    private String date;
    private int milk;
    private int rice;


    public RecordChart2Info(String date, int milk, int rice){

        this.date = date.substring(5,10);
        this.milk = milk;
        this.rice = rice;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
