package com.company.jk.pcoordinator.board.talk;

public class Talkinfo {
    int id, reply_id, reply_level, eyes, talks, good;
    String title;
    String author;


    String email;
    String contents;
    String createDate;
    boolean goodChecked;



    public Talkinfo(int id, int reply_id, int reply_level, String title, String  author, String email, String contents, int eyes, int talks, int good, boolean goodChecked, String createDate){

        this.id = id;
        this.reply_id = reply_id;
        this.reply_level = reply_level;
        this.title = title;
        this.author = author;
        this.email = email;
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

    public int getReply_id() {
        return reply_id;
    }

    public int getReply_level() {
        return reply_level;
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

    public String getAuthor() {
        return author;
    }

    public String getContents() {
        return contents;
    }


    public void setGoodChecked(boolean goodChecked) {
        this.goodChecked = goodChecked;
    }
    public boolean getGoodChecked(){
        return  goodChecked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
