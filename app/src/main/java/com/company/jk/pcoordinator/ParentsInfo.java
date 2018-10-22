package com.company.jk.pcoordinator;

public class ParentsInfo {
    String email;
    String baby_id;
    String nickname; 
    String birthday;
    String relationship;




    public ParentsInfo(String email, String nickname, String birthday, String relationship, String baby_id){
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.relationship = relationship;
        this.baby_id = baby_id;
    }


    public String getId() {
        return email;
    }

    public String getName() {
        return nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return relationship;
    }

    public  String getBaby_id(){ return baby_id; }
}
