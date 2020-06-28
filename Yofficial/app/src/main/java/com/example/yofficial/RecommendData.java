package com.example.yofficial;



public class RecommendData {


    String food_name;
    int savory;
    int sweet;
    int sour;
    int spicy;
    int bitter;
    int salty;

    public RecommendData(String title, int _savory, int _sweet, int _sour, int _spicy, int _bitter, int _salty){
        this.food_name = title;
        this.savory= _savory;
        this.sweet= _sweet;
        this.sour= _sour;
        this.spicy= _spicy;
        this.bitter= _bitter;
        this.salty= _salty;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getSavory() {
        return savory;
    }

    public void setSavory(int savory) {
        this.savory = savory;
    }

    public int getSweet() {
        return sweet;
    }

    public void setSweet(int sweet) {
        this.sweet = sweet;
    }

    public int getSour() {
        return sour;
    }

    public void setSour(int sour) {
        this.sour = sour;
    }

    public int getSpicy() {
        return spicy;
    }

    public void setSpicy(int spicy) {
        this.spicy = spicy;
    }

    public int getBitter() {
        return bitter;
    }

    public void setBitter(int bitter) {
        this.bitter = bitter;
    }

    public int getSalty() {
        return salty;
    }

    public void setSalty(int salty) {
        this.salty = salty;
    }

    public double cos_similarity(int user_savory, int user_sweet, int user_spicy, int user_sour, int user_salty){
        double result;
        double numerator;
        double denominator;
        int standard_sum;
        int user_sum;

        numerator = (user_savory * savory) + (user_sweet * sweet) + (user_spicy * spicy) + (user_sour * sour) + (user_salty * salty);

        standard_sum = (savory * savory) + (sweet * sweet) + (spicy * spicy) + (sour * sour) + (salty * salty);
        user_sum = (user_savory * user_savory) + (user_sweet * user_sweet) + (user_spicy * user_spicy) + (user_sour * user_sour) + (user_salty * user_salty);
        denominator = Math.sqrt(standard_sum) * Math.sqrt(user_sum);

        result = numerator / denominator;

        return result;
    }

}
