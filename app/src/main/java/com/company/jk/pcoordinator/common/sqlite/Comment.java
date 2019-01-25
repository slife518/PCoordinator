package com.company.jk.pcoordinator.common.sqlite;

import java.util.Date;

public class Comment {

    int id;
    String comment;

    public Comment() {
    }
    public Comment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
