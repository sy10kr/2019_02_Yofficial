package com.example.yofficial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class eval_popup extends Activity {

    TextView rateText;
    ImageView first;
    ImageView second;
    ImageView third;
    ImageView fourth;
    ImageView fifth;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String userId;

    int exp = 0;
    int self_score = 0;
    int curCookLv;
    int curCookExp;
    int flag = 0;

    String TAG = "Eval!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //배경 투명하게
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        setContentView(R.layout.activity_eval_popup);

        //UI 객체생성
        rateText = (TextView)findViewById(R.id.rateText);
        first = findViewById(R.id.first_star);
        second = findViewById(R.id.second_star);
        third = findViewById(R.id.third_star);
        fourth = findViewById(R.id.fourth_star);
        fifth = findViewById(R.id.fifth_star);



        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setImageResource(R.drawable.filled_star);
                second.setImageResource(R.drawable.empty_star);
                third.setImageResource(R.drawable.empty_star);
                fourth.setImageResource(R.drawable.empty_star);
                fifth.setImageResource(R.drawable.empty_star);
                rateText.setText("연습이 필요");
                self_score = 1;
                exp = -30;
                System.out.println(rateText.getText());
                System.out.println(self_score);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setImageResource(R.drawable.filled_star);
                second.setImageResource(R.drawable.filled_star);
                third.setImageResource(R.drawable.empty_star);
                fourth.setImageResource(R.drawable.empty_star);
                fifth.setImageResource(R.drawable.empty_star);
                rateText.setText("아직 부족함");
                self_score = 2;
                System.out.println(rateText.getText());
                System.out.println(self_score);
                exp = -10;
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setImageResource(R.drawable.filled_star);
                second.setImageResource(R.drawable.filled_star);
                third.setImageResource(R.drawable.filled_star);
                fourth.setImageResource(R.drawable.empty_star);
                fifth.setImageResource(R.drawable.empty_star);
                rateText.setText("먹어줄만 함");
                self_score = 3;
                System.out.println(rateText.getText());
                System.out.println(self_score);
                exp = 0;
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setImageResource(R.drawable.filled_star);
                second.setImageResource(R.drawable.filled_star);
                third.setImageResource(R.drawable.filled_star);
                fourth.setImageResource(R.drawable.filled_star);
                fifth.setImageResource(R.drawable.empty_star);
                rateText.setText("만족스러움");
                self_score = 4;
                System.out.println(rateText.getText());
                System.out.println(self_score);
                exp = 10;
            }
        });

        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first.setImageResource(R.drawable.filled_star);
                second.setImageResource(R.drawable.filled_star);
                third.setImageResource(R.drawable.filled_star);
                fourth.setImageResource(R.drawable.filled_star);
                fifth.setImageResource(R.drawable.filled_star);
                rateText.setText("레시피를 마스터함");
                self_score = 5;
                System.out.println(rateText.getText());
                System.out.println(self_score);
                exp = 30;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getEmail().split("@")[0];

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("users").child(userId).child("cookLevel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                curCookLv = dataSnapshot.getValue(Integer.class);
                myRef.child("users").child(userId).child("cookExp").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        curCookExp = dataSnapshot.getValue(Integer.class);
                        flag = 1;
                        Log.d(TAG, ""+ curCookLv);
                        Log.d(TAG, ""+ curCookExp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기

        while(flag!=1){

        }

        curCookExp += exp;
        if(curCookExp >= 100){
            curCookExp -= 100;
            curCookLv++;
        }

        else if(curCookExp < 0){
            curCookExp += 100;
            curCookLv--;
        }

        myRef.child("users").child(userId).child("cookExp").setValue(curCookExp);
        myRef.child("users").child(userId).child("cookLevel").setValue(curCookLv);

        Intent intent = new Intent(getApplicationContext(), JoonHongActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
