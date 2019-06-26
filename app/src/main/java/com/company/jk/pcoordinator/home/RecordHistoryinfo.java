package com.company.jk.pcoordinator.home;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RecordHistoryinfo implements Serializable {
    String id;
    String date;
    String day;
    String yearDate;
    String time;
    int milk;
    int mothermilk;
    int rice;
    String author;
    String comments;


    public RecordHistoryinfo(String id, String date, String time, int milk,int mothermilk, int rice,  String author, String comments){
        this.id = id;
        this.yearDate = date;
//        this.date = date.substring(5,10);
        this.date = date;
        this.milk = milk;
        this.mothermilk = mothermilk;
        this.rice = rice;
        this.time = time.substring(0,5);
        this.author = author;
        this.comments = comments;
    }


    public String getId() {
        return id;
    }

    public String getYearDate() {
        return yearDate;
    }

    public String getDate() {
        return date;
    }

    public String getDay(){
//        String day = date.substring(8,10);
//        if (day.substring(0,1).equals("0")){
//            day = date.substring(9,10);
//        }
        return  date.substring(8,10);
    }
    public String getTime() {
        int hh = Integer.parseInt(time.substring(0,2));
        if(hh > 12){
            hh = hh - 12;
            return "pm " + String.valueOf(hh) + time.substring(2,5);
        }else {
            return "am " + String.valueOf(hh) + time.substring(2,5);
        }
    }

    public String getMilk() {
        return String.valueOf(milk);
    }

    public String getMothermilk() {
        return String.valueOf(mothermilk);
    }

    public String getRice() {
        return String.valueOf(rice);
    }

    public String getEat(){
        String _mlik ="";

        if(milk==0){
            _mlik = "";
        }else {

            _mlik = "  분유: " + String.valueOf(milk) + "ml";
        }

        String _rice ="";

        if(rice==0){
            _rice = "";
        }else {

            _rice = "  이유식: " + String.valueOf(rice) + "mg";
        }

        String _mothermilk ="";

        if(mothermilk==0){
            _mothermilk = "";
        }else {

            _mothermilk = "  수유: " + String.valueOf(mothermilk) + "분";
        }

        return _mothermilk + _mlik + _rice  ;
    }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }



}
