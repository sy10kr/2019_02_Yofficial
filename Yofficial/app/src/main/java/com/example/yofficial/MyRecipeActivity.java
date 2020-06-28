package com.example.yofficial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyRecipeActivity extends AppCompatActivity {
    List<VideoItem> list;
    ListView listview;
    VideoAdapter adapter;
    MenuItem mSearch;
    Context c = this;

    private final static String TAG = "MyRecipe!";
    private static final int REQUEST_CODE = 10;
    private static final int DELETE_OK = 12;
    private int pos;


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<PostInfo> pList;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String userId;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrecipe);

        listview = (ListView) findViewById(R.id.recipeview);
        list = new ArrayList<VideoItem>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        pList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getEmail().split("@")[0];


        myRef.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                while(itr.hasNext()){
                    PostInfo p = itr.next().getValue(PostInfo.class);
                    Log.d(TAG, "p : " + p.getUserId() + " user : " + userId);
                    if(p.getUserId().compareTo(userId) == 0){
                        Log.d(TAG, "ENTER");
                        pList.add(p);

                        storage = FirebaseStorage.getInstance();
                        storageRef = storage.getReference().child("images/" + p.getRecipeId() +".jpg");
                        storageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Log.d(TAG, "Succeeded");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                BitmapDrawable img = new BitmapDrawable(getResources(), bitmap);
                                list.add(new VideoItem(img, p.getTitle(), "\n"+p.getUserId(), "\n" + p.getViews()+" views", p.getRecipeId()));
                                Log.d("jun", "listAdded" + list.size());
                                adapter = new VideoAdapter(c, list);
                                listview.setAdapter(adapter);
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Failed");
                                Drawable img = ContextCompat.getDrawable(c, R.drawable.fail);
                                list.add(new VideoItem(img, p.getTitle(), "\n"+p.getUserId(), "\n" + p.getViews()+" views", p.getRecipeId()));
                                Log.d("jun", "listAdded" + list.size());
                                adapter = new VideoAdapter(c, list);
                                listview.setAdapter(adapter);

                            }
                        });




                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyRecipeActivity.this ,list.get(position). getV_title(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), HyunWooActivity.class);

                String recipeId = list.get(position).getRecipe_id();
                intent.putExtra("id", recipeId);
                intent.putExtra("userid", list.get(position).getV_uploader().substring(1));
                pos = position;

                myRef.child("posts").child(recipeId).child("views").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int views = dataSnapshot.getValue(Integer.class);
                        views++;
                        myRef.child("posts").child(recipeId).child("views").setValue(views);
                        list.get(position).setView_num(Integer.toString(views) + " views");
                        adapter = new VideoAdapter(c, list);
                        listview.setAdapter(adapter);
                        startActivityForResult(intent, REQUEST_CODE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == DELETE_OK){
            list.remove(pos);
            adapter = new VideoAdapter(c, list);
            listview.setAdapter(adapter);
        }
    }

}
