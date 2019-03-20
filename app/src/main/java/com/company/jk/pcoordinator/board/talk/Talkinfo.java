package com.company.jk.pcoordinator.board.talk;

public class Talkinfo {
    int id, eyes, talks, good;
    String title;
    String contents;
    String createDate;
    boolean goodChecked;


    public Talkinfo(int id, String title, String contents, int eyes, int talks, int good, boolean goodChecked, String createDate){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.eyes = eyes;
        this.talks = talks;
        this.good = good;
        this.goodChecked = goodChecked;
        this.createDate = createDate;

    }


    public int getId() {
        return id;
    }

    public int getEyes() {
        return eyes;
    }

    public int getTalks() {
        return talks;
    }

    public int getGood() {
        return good;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public boolean getGoodChecked(){
        return  goodChecked;
    }

}
