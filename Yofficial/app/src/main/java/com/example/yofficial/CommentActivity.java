package com.example.yofficial;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    BoardItem board;
    ArrayList<CommentItem> list;
    ListView listview;
    CommentAdapter adapter;
    MenuItem mSearch;
    Context c = this;


    String text;
    private EditText edit_comment;
    private final static String TAG = "Comment!";
    private String board_id;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.c_list);
        activity = this;

        Intent intent = getIntent();
        board_id = intent.getExtras().getString("id");

        Log.d(TAG, board_id);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Log.d(TAG, "get reference");

        list = new ArrayList<>();
        Log.d(TAG, "get list");


        edit_comment = (EditText)findViewById(R.id.comment_edit);
        listview = (ListView) findViewById(R.id.listview1);

        adapter = new CommentAdapter(c, list);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("comments").child(board_id).exists()){
                    Log.d(TAG, ""+dataSnapshot.child("comments").child(board_id).getChildrenCount());
                    Iterator<DataSnapshot> itr = dataSnapshot.child("comments").child(board_id).getChildren().iterator();

                    while(itr.hasNext()){
                        CommentItem c = itr.next().getValue(CommentItem.class);
                        Log.d(TAG, c.comment_data);
                        list.add(c);
                    }
                    adapter.setCommentList(list);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                else{
                    Log.d(TAG, "No comments");
                    //need to insert error handle for no comments
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CommentActivity.this ,list.get(position). getUser_id(),Toast.LENGTH_LONG).show();

            }
        });

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                text =  edit_comment.getText().toString();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertdialog.setMessage("댓글을 삭제하시겠습니까?");


                // 확인버튼
                alertdialog.setNegativeButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth = FirebaseAuth.getInstance();
                        String userID = mAuth.getCurrentUser().getEmail().split("@")[0];
                        if(list.get(position).user_id.compareTo(userID) == 0){

                            String c_id = list.get(position).comment_id;
                            myRef.child("comments").child(board_id).child(c_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    list.remove(position);
                                    adapter.setCommentList(list);
                                    adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CommentActivity.this ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else{
                            Toast.makeText(CommentActivity.this ,"본인 댓글만 삭제 가능합니다!",Toast.LENGTH_SHORT).show();
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
        });
    }
    public void insert_comment(View v){
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        if(text.isEmpty()){
            Toast.makeText(getApplicationContext(), "댓글을 입력해주세요!", Toast.LENGTH_LONG).show();

        }
        else{
            Log.d(TAG, "comment is not empty!");
            String commentId = myRef.child("comments").child(board_id).push().getKey();
            mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, "get key from comments");

            String userID = mAuth.getCurrentUser().getEmail().split("@")[0];
            CommentItem c = new CommentItem(userID, strDate, text, commentId);
            Log.d(TAG, "made Comment Item");

            myRef.child("comments").child(board_id).child(commentId).setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Succeded");

                    list.add(c);
                    Log.d(TAG, "listAdded");
                    adapter.setCommentList(list);
                    listview.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "notifydatachanged");
                    edit_comment.setText("");
                    Log.d(TAG, "set Text");
                    Toast.makeText(CommentActivity.this ,"삽입",Toast.LENGTH_LONG).show();
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
}