package com.example.yofficial;

import java.util.ArrayList;
import java.util.HashMap;


public class RecipeInfo {
    private String recipeId;
    private String recipeTitle;
    private String recipeSubTitle;
    private String introRecipe;
    private String servings;
    private String difficulty;
    private String duraTime;
    private String mainIngredient;
    private String type;
    private String feature;
    private ArrayList<String> ingredientName;
    private ArrayList<String> ingredientAmount;
    private ArrayList<String> seasoningName;
    private ArrayList<String> seasoningAmount;
    private String youtubeUrl;
    private ArrayList<String> startTime;
    private ArrayList<String> endTime;
    private ArrayList<String> stepDescrib;


    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeSubTitle() {
        return recipeSubTitle;
    }

    public void setRecipeSubTitle(String recipeSubTitle) {
        this.recipeSubTitle = recipeSubTitle;
    }

    public String getIntroRecipe() {
        return introRecipe;
    }

    public void setIntroRecipe(String introRecipe) {
        this.introRecipe = introRecipe;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDuraTime() {
        return duraTime;
    }

    public void setDuraTime(String duraTime) {
        this.duraTime = duraTime;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public ArrayList<String> getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(ArrayList<String> ingredientName) {
        this.ingredientName = ingredientName;
    }

    public ArrayList<String> getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(ArrayList<String> ingredientAmount) {
        this.ingredientAmount = ingredientAmount;
    }

    public ArrayList<String > getSeasoningName() {
        return seasoningName;
    }

    public void setSeasoningName(ArrayList<String> seasoningName) {
        this.seasoningName = seasoningName;
    }

    public ArrayList<String > getSeasoningAmount() {
        return seasoningAmount;
    }

    public void setSeasoningAmount(ArrayList<String> seasoningAmount) {
        this.seasoningAmount = seasoningAmount;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public ArrayList<String> getStartTime() {
        return startTime;
    }

    public void setStartTime(ArrayList<String> startTime) {
        this.startTime = startTime;
    }

    public ArrayList<String> getEndTime() {
        return endTime;
    }

    public void setEndTime(ArrayList<String> endTime) {
        this.endTime = endTime;
    }

    public ArrayList<String> getStepDescrib() {
        return stepDescrib;
    }

    public void setStepDescrib(ArrayList<String> stepDescrib) {
        this.stepDescrib = stepDescrib;
    }
}