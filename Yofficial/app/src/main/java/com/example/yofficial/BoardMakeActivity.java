package com.example.yofficial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardMakeActivity extends AppCompatActivity {

    private EditText edit_title;
    private EditText edit_body;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private final static String TAG = "board!";

    BoardItem item;
    String b_id;
    String b_uploader;
    String title;
    String body;

    Button post;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_board_activity);

        edit_title = findViewById(R.id.board_t_input);
        edit_body = findViewById(R.id.board_b_input);
        post = findViewById(R.id.board_upload);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = edit_title.getText().toString();
                body = edit_body.getText().toString();
                if(title.isEmpty()){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                else if(body.isEmpty()){
                    Toast.makeText(getApplicationContext(), "본문을 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                else{

                    upload_board();

                }
            }
        });



    }

    public void upload_board(){
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        b_uploader = mAuth.getCurrentUser().getEmail().split("@")[0];
        b_id = myRef.child("boards").push().getKey();
        item = new BoardItem(b_id, title, b_uploader, strDate, body);
        Log.d(TAG, "StartUploading");
        myRef.child("boards").child(b_id).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Succeeded!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed");
                Toast.makeText(getApplicationContext(), "다시 한번 시도해주세요!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
