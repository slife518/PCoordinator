package com.company.jk.pcoordinator;

public class ParentsInfo {
    String email;
    String nickname; 
    String birthday;
    String relationship;



    public ParentsInfo(String email, String nickname, String birthday, String relationship){
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.relationship = relationship;
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
}
