package com.company.jk.pcoordinator.mypage.mybaby;

public class Mybabyinfo {
    int id;
    String name;
    String birthday;
    String sex;
    String father;
    String mother;


    public Mybabyinfo(int id, String name, String birthday, String sex, String father, String mother){
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.father = father;
        this.mother = mother;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }



}
