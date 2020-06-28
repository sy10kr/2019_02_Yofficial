package com.example.yofficial;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CitrusActivity extends AppCompatActivity {
    RecipeInfo reInfo = new RecipeInfo();
    String[] dbIng = {"귤", "그리고귤"};
    String[] ingredients;
    String[] dbSsn = {"손맛", "스킬"};
    String[] seasonings;
    String[] dbStartTime = {"2/30/20","0/22/34","0/20/01"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citrus);

        reInfo.setRecipeTitle("Cuisse de grenouille");
        reInfo.setRecipeSubTitle("귤의 변신");
        reInfo.setIntroRecipe("프리미엄 귤 요리");
        reInfo.setServings("1인분");
        reInfo.setDifficulty("다이아몬드");
        reInfo.setDuraTime("5분 이하");

        //db에서 쏴 준 정보 임시 문자열 테이블에 저장, 나중에 db연동 시 수정
        ingredients = new String[dbIng.length];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = dbIng[i];
        }

        //reInfo.setIngredient(ingredients);
        seasonings = new String[dbSsn.length];
        for (int i = 0; i < seasonings.length; i++) {
            seasonings[i] = dbSsn[i];
        }
        //reInfo.setSeasoning(seasonings);

        //db정보가 있는 RecipeInfo클래스에서 정보 받아오기
        TextView citrusTitle = (TextView)findViewById(R.id.citrus_title);
        citrusTitle.setText(reInfo.getRecipeTitle());
        TextView citrusSubTitle = (TextView)findViewById(R.id.citrus_sub_title);
        citrusSubTitle.setText(reInfo.getRecipeSubTitle());
        TextView introCitrusEdit = (TextView) findViewById(R.id.introCitrusRecipe);
        introCitrusEdit.setText(reInfo.getIntroRecipe());
        TextView citrus_serveNum = (TextView)findViewById(R.id.citrus_serveNum);
        citrus_serveNum.setText(reInfo.getServings());
        TextView citrus_difficulty = (TextView)findViewById(R.id.citrus_difficulty);
        citrus_difficulty.setText(reInfo.getDifficulty());
        TextView citrus_duraTime = (TextView)findViewById(R.id.citrus_duraTime);
        citrus_duraTime.setText(reInfo.getDuraTime());
        TextView citrus_ingredientList = (TextView)findViewById(R.id.citrus_ingredientList);
        //citrus_ingredientList.setText(reInfo.getIngredient());
        TextView citrus_seasoningList = (TextView)findViewById(R.id.citrus_seasoningList);
        //citrus_seasoningList.setText(reInfo.getSeasoning());

        //뷰, 버튼들의 선언부
        ImageView citrus_imageView = (ImageView)findViewById(R.id.citrus_image);
        ImageView yoficonView = (ImageView)findViewById(R.id.yoficon);
        ImageView citrus_servings = (ImageView)findViewById(R.id.citrus_servings);
        ImageView citrus_level = (ImageView)findViewById(R.id.citrus_level);
        ImageView citrus_duration = (ImageView)findViewById(R.id.citrus_duration);
        ImageView citrus_youtubeUrl = (ImageView) findViewById(R.id.citrus_youtubeUrl);

        // drawable에 있는 이미지를 지정합니다.
        citrus_imageView.setImageResource(R.drawable.citrus_image);
        yoficonView.setImageResource(R.drawable.yicon);
        citrus_servings.setImageResource(R.drawable.servings);
        citrus_level.setImageResource(R.drawable.level);
        citrus_duration.setImageResource(R.drawable.duration);

        citrus_youtubeUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YouTubeActivity.class);
                startActivity(intent);
            }
        });

//        citrus_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),CreateRecipeActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
