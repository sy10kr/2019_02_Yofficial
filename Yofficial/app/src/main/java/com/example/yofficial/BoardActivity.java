package com.example.yofficial;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class BoardActivity extends AppCompatActivity {

    String id;
    BoardItem item;
    TextView title;
    TextView info;
    TextView body;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private BoardActivity ba = this;

    private Activity activity = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity);


        Intent intent = getIntent();
        id =intent.getExtras().getString("id");



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("boards").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                item = dataSnapshot.getValue(BoardItem.class);
                title = (TextView)findViewById(R.id.board_title);
                info = (TextView)findViewById(R.id.board_info);
                body = (TextView)findViewById(R.id.board_data);
                title.setText(item.board_title);
                info.setText(item.board_uploader + " | " + item.board_date);
                body.setText(item.board_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void see_comment(View v){
        Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
        intent.putExtra("id", item.board_id);
        startActivity(intent);
    }

    public void delete_board(View v){
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getEmail().split("@")[0];

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
        // 다이얼로그 메세지
        alertdialog.setMessage("게시글을 삭제하시겠습니까?");


        // 확인버튼
        alertdialog.setNegativeButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(item.board_uploader.compareTo(userID) == 0){

                    myRef.child("boards").child(item.board_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            myRef.child("comments").child(item.board_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ba.finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(BoardActivity.this ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BoardActivity.this ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    Toast.makeText(BoardActivity.this ,"본인 게시글만 삭제 가능합니다!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 취소버튼
        alertdialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // 메인 다이얼로그 생성
        AlertDialog alert = alertdialog.create();
        // 아이콘 설정
        alert.setIcon(R.drawable.yofficial);
        // 타이틀
        alert.setTitle("알림!");
        // 다이얼로그 보기
        alert.show();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_btn1:
                playBtn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void playBtn(){
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getEmail().split("@")[0];

        if (item.board_uploader.compareTo(userID) == 0) {
            Intent intent = new Intent(getApplicationContext(), BoardEditActivity.class);
            intent.putExtra("id", item.board_id);
            intent.putExtra("title" ,title.getText());
            intent.putExtra("body", body.getText());
            startActivity(intent);

        } else {
            Toast.makeText(BoardActivity.this ,"본인 게시글만 수정 가능합니다!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("boards").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                item = dataSnapshot.getValue(BoardItem.class);
                title = (TextView)findViewById(R.id.board_title);
                info = (TextView)findViewById(R.id.board_info);
                body = (TextView)findViewById(R.id.board_data);
                title.setText(item.board_title);
                info.setText(item.board_uploader + " | " + item.board_date);
                body.setText(item.board_data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
