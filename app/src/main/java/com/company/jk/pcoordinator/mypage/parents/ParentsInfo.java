package com.company.jk.pcoordinator.mypage.parents;

public class ParentsInfo {
    String email;
    int baby_id;
    String nickname; 
    String birthday;
    String relationship;




    public ParentsInfo(String email, String nickname, String birthday, String relationship, int baby_id){
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

    public  int getBaby_id(){ return baby_id; }
}
