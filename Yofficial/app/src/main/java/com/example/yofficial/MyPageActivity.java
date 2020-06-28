package com.example.yofficial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPageActivity extends AppCompatActivity {

    private Button myBoardsBtn;
    private Button myCommentsBtn;
    private Button myRecipeBtn;
    private Button createRecipeBtn;
    private TextView nameTV;
    private TextView emailTV;
    private TextView chefLVTV;
    private TextView cookLVTV;
    private ProgressBar chefExpPB;
    private ProgressBar cookExpPB;
    private FirebaseAuth mAuth;
    private UserInfo user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);


        myBoardsBtn = findViewById(R.id.my_boards_btn);
        myCommentsBtn = findViewById(R.id.my_comments_btn);
        myRecipeBtn = findViewById(R.id.my_recipes_btn);
        createRecipeBtn = findViewById(R.id.create_recipe_btn);
        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
        chefLVTV = findViewById(R.id.chefLv);
        cookLVTV = findViewById(R.id.cookLv);
        chefExpPB = findViewById(R.id.chefExp);
        cookExpPB = findViewById(R.id.cookExp);



        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getEmail().split("@")[0];

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserInfo.class);

                //set id, email, level exp to the xml file
                nameTV.setText(mAuth.getCurrentUser().getDisplayName());
                emailTV.setText(mAuth.getCurrentUser().getEmail());
                chefLVTV.setText("LV : "+user.getChefLevel());
                cookLVTV.setText("LV : "+user.getCookLevel());
                chefExpPB.setProgress(user.getChefExp());
                cookExpPB.setProgress(user.getCookExp());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myBoardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBoards.class);
                startActivity(intent);
            }
        });

        myCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCommentActivity.class);
                startActivity(intent);
            }
        });

        myRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyRecipeActivity.class);
                startActivity(intent);
            }
        });



        createRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateRecipeActivity.class);
                startActivity(intent);
            }
        });





    }
}
