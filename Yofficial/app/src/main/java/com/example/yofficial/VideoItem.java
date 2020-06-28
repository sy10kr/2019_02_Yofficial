package com.example.yofficial;

import android.graphics.drawable.Drawable;

public class VideoItem {
    Drawable iconDrawable ;

    String v_title;
    String v_uploader;
    String view_num;
    String recipe_id;


    public Drawable getIcon() {
        return this.iconDrawable ;
    }

    public String getV_title(){
        return v_title;
    }

    public String getV_uploader(){
        return v_uploader;
    }

    public String getView_num(){
        return view_num;
    }

    public void setV_title(String v_title){
        this.v_title = v_title;
    }

    public void setV_uploade(String v_uploader){
        this.v_uploader = v_uploader;
    }

    public void setView_num(String view_num){
        this.view_num = view_num;
    }

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public VideoItem(Drawable _iconDrawable, String _v_title, String _v_uploader, String _view_num, String _recipe_id){
        iconDrawable = _iconDrawable;
        v_title = _v_title;
        v_uploader = _v_uploader;
        view_num = _view_num;
        recipe_id = _recipe_id;
    }


}
