package com.example.yofficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void joonButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void jaeButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), JaeWonActivity.class);
        startActivity(intent);
    }

    public void hyunButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), eval_popup.class);
        startActivity(intent);
    }
}
