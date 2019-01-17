package com.company.jk.pcoordinator.home;

import java.io.Serializable;

/**
 * Created by jungkukjo on 06/01/2019.
 */

public class RecordChart1Info implements Serializable {


    private String date;
    private int mothermilk;


    public RecordChart1Info(String date, int mothermilk){

        this.date = date.substring(5,10);
        this.mothermilk = mothermilk;
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

}
