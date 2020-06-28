package com.example.yofficial;


public class UserInfo {
    private String id;


    private int cookLevel;
    private int cookExp;

    private int chefLevel;
    private int chefExp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCookLevel() {
        return cookLevel;
    }

    public void setCookLevel(int cookLevel) {
        this.cookLevel = cookLevel;
    }

    public int getCookExp() {
        return cookExp;
    }

    public void setCookExp(int cookExp) {
        this.cookExp = cookExp;
    }

    public int getChefLevel() {
        return chefLevel;
    }

    public void setChefLevel(int chefLevel) {
        this.chefLevel = chefLevel;
    }

    public int getChefExp() {
        return chefExp;
    }

    public void setChefExp(int chefExp) {
        this.chefExp = chefExp;
    }
}
