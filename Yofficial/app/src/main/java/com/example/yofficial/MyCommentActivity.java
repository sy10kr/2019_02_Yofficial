package com.example.yofficial;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Iterator;

public class MyCommentActivity extends AppCompatActivity{

    ArrayList<CommentItem> list;
    ListView listview;
    CommentAdapter adapter;
    MenuItem mSearch;
    Context c = this;
    String text;
    Activity activity;

    private final static String TAG = "MyComment!";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mycomments);
        activity = this;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        Log.d(TAG, "get reference");

        list = new ArrayList<>();
        Log.d(TAG, "get list");

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getEmail().split("@")[0];


        listview = (ListView) findViewById(R.id.comment_list_view);

        adapter = new CommentAdapter(c, list);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                if(dataSnapshot.child("comments").exists()){
                    Iterator<DataSnapshot> itr = dataSnapshot.child("comments").getChildren().iterator();

                    Log.d(TAG, "After get iterator");
                    while(itr.hasNext()){
                        Log.d(TAG, "hasNext");
                        DataSnapshot d = itr.next();
                        Log.d(TAG, d.toString());
                        Log.d(TAG, d.getKey());
                        Log.d(TAG, ""+dataSnapshot.child("comments").child(d.getKey()).getChildrenCount());

                        Iterator<DataSnapshot> inneritr = dataSnapshot.child("comments").child(d.getKey()).getChildren().iterator();
                        while(inneritr.hasNext()){
                            CommentItem c = inneritr.next().getValue(CommentItem.class);
                            Log.d(TAG, "Convert to comment item");
                            Log.d(TAG, c.comment_data);

                            if(c.user_id.compareTo(userId) == 0){
                                list.add(c);
                            }

                        }

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

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertdialog.setMessage("댓글을 삭제하시겠습니까?");


                // 확인버튼
                alertdialog.setNegativeButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(list.get(position).user_id.compareTo(userId) == 0){
                            String c_id = list.get(position).comment_id;

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if(dataSnapshot.child("comments").exists()){
                                        Iterator<DataSnapshot> itr = dataSnapshot.child("comments").getChildren().iterator();


                                        while(itr.hasNext()){

                                            DataSnapshot d = itr.next();

                                            Log.d(TAG, d.getKey());
                                            Log.d(TAG, ""+dataSnapshot.child("comments").child(d.getKey()).getChildrenCount());

                                            Iterator<DataSnapshot> inneritr = dataSnapshot.child("comments").child(d.getKey()).getChildren().iterator();
                                            while(inneritr.hasNext()){
                                                DataSnapshot targ = inneritr.next();
                                                if(targ.getValue(CommentItem.class).comment_id.compareTo(c_id) == 0){
                                                    targ.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            list.remove(position);
                                                            adapter.setCommentList(list);
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MyCommentActivity.this ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }


                                            }

                                        }

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

                        }
                        else{
                            Toast.makeText(MyCommentActivity.this ,"본인 댓글만 삭제 가능합니다!",Toast.LENGTH_SHORT).show();
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
}


