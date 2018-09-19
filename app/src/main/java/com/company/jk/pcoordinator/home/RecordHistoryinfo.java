package com.company.jk.pcoordinator.home;

public class RecordHistoryinfo {
    String id;
    String date;
    String time;
    String milk;
    String rice;
    String author;
    String comments;


    public RecordHistoryinfo(String id, String date, String time, String milk, String rice,  String author, String comments){
        this.id = id;
        this.date = date.substring(5,10 );
        this.milk = milk;
        this.rice = rice;
        this.time = time.substring(0,5);
        this.author = author;
        this.comments = comments;
    }


    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMilk() {
        return milk;
    }

    public String getRice() {
        return rice;
    }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }



}
