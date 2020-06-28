package com.example.yofficial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Refreg_PopupActivity extends Activity {

    TextView txtText;
    List<VideoItem> list;
    ListView listview;
    VideoAdapter adapter;
    ArrayList<String> ReceiveArr;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private final static String TAG = "POP!";
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ArrayList<PostInfo> pList;
    Context c = this;
    ArrayList<RecipeInfo> data;
    String userId;
    int pos;
    private static final int REQUEST_CODE = 10;
    private static final int DELETE_OK = 12;



    ArrayList<RecipeInfo> toArrayList(DataSnapshot dataSnapshot){
        ArrayList<RecipeInfo> r = new ArrayList<>();

        Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();

        while(itr.hasNext()){
            r.add(itr.next().getValue(RecipeInfo.class));
        }

        return r;
    }


    ArrayList<Integer> calDistance(ArrayList<RecipeInfo> data, ArrayList<String> texts){
        ArrayList<Integer> ans = new ArrayList<>();

        int dataLen = data.size();
        int textsLen = texts.size();
        for(int i = 0; i < dataLen; i++){

            ArrayList<String> ingreds = data.get(i).getIngredientName();


            int d = ingreds.size();
            for(int j = 0; j < textsLen; j++){
                if(ingreds.contains(texts.get(j))){
                    d--;
                }
            }
            ans.add(d);
        }



        return ans;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_refreg_popup);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.pop_input_text);

        //데이터 가져오기
        Intent intent = getIntent();
        ReceiveArr = intent.getStringArrayListExtra("ArrayList"); //어레이 리스트 받아옴 (선택된 재료들)

        txtText.setText("선택된 재료\n" );
        for(int i=0;i<ReceiveArr.size();i++){ // 어레이리스트 출력
            txtText.append("[" + ReceiveArr.get(i) + "] ");
        }



        // 팝업내 리스트뷰
        listview = (ListView) findViewById(R.id.pop_listView);
        list = new ArrayList<VideoItem>();
        pList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                //make itr to ArrayList<RecipeInfo>
                data = toArrayList(dataSnapshot);
                Log.d(TAG, data.toString());

                //calculate distance (return ArrayList<Integer>)
                ArrayList<Integer> distance = calDistance(data, ReceiveArr);
                Log.d(TAG, distance.toString());
                //get 0,1,2 recipe ID

                ArrayList<String> targRecipeId = new ArrayList<String>();
                int dSize = distance.size();
                for(int i = 0; i < dSize; i++){
                    int d = distance.get(i);
                    if(d == 0){
                        targRecipeId.add(data.get(i).getRecipeId());
                    }
                }

                //list add posts whose id is recipeID
                int idSize = targRecipeId.size();
                for(int i = 0; i < idSize; i++){
                    myRef.child("posts").child(targRecipeId.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            PostInfo p = dataSnapshot.getValue(PostInfo.class);
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
                                    adapter = new VideoAdapter(c, list);
                                    listview.setAdapter(adapter);
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed");
                                    Drawable img = ContextCompat.getDrawable(c, R.drawable.fail);
                                    list.add(new VideoItem(img, p.getTitle(), "\n"+p.getUserId(), "\n" + p.getViews()+" views", p.getRecipeId()));
                                    adapter = new VideoAdapter(c, list);
                                    listview.setAdapter(adapter);

                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        /*
        list.add(new VideoItem(ContextCompat.getDrawable(this, R.drawable.ab), "3분만에 만드는 맛있는 수제햄버거", "\n맥도날드", "\n24212 views"));
        list.add(new VideoItem(ContextCompat.getDrawable(this, R.drawable.aa), "delicious gyudon", "\n홍길동", "\n84213 views"));
        list.add(new VideoItem(ContextCompat.getDrawable(this, R.drawable.citrus_image), "Cuisse de grenouille", "\n이재원", "\n11views"));
*/


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), HyunWooActivity.class);
                String recipeId = list.get(position).getRecipe_id();
                intent.putExtra("id", recipeId);
                intent.putExtra("userid", list.get(position).getV_uploader().substring(1));

                myRef.child("posts").child(recipeId).child("views").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int views = dataSnapshot.getValue(Integer.class);
                        views++;
                        myRef.child("posts").child(recipeId).child("views").setValue(views);
                        list.get(position).setView_num(Integer.toString(views) + " views");



                        userId = list.get(position).getV_uploader();
                        userId = userId.substring(1);

                        Log.d(TAG,userId);
                        myRef.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, "Enter users");
                                UserInfo u = dataSnapshot.getValue(UserInfo.class);
                                int curExp = u.getChefExp();
                                int curLv = u.getChefLevel();

                                Log.d(TAG, ""+ curExp + " " + curLv);
                                curExp += 5;
                                if(curExp >= 100){
                                    curExp -= 100;
                                    curLv++;
                                }
                                u.setChefLevel(curLv);
                                u.setChefExp(curExp);

                                Log.d(TAG, u.toString());
                                myRef.child("users").child(userId).setValue(u);
                                Log.d(TAG, "updated");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d(TAG, "Cancelled");
                                Log.d(TAG, databaseError.toString());
                            }
                        });




                        adapter = new VideoAdapter(c, list);
                        listview.setAdapter(adapter);
                        pos = position;
                        startActivityForResult(intent, REQUEST_CODE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == DELETE_OK){
            list.remove(pos);
            adapter = new VideoAdapter(c, list);
            listview.setAdapter(adapter);
        }
    }





    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
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
