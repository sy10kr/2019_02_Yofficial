package com.example.yofficial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    List<VideoItem> list;
    ListView listview;
    VideoAdapter adapter;
    MenuItem mSearch;
    Context c = this;
    int views;

    private final static String TAG = "VideoActivity!";


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<PostInfo> pList;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String userId;
    private ChildEventListener ce;
    private static final int REQUEST_CODE = 10;
    private static final int DELETE_OK = 12;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v_list);

        //editsearch = (EditText)findViewById(R.id.editSearch);


        listview = (ListView) findViewById(R.id.listview1);
        list = new ArrayList<VideoItem>();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        pList = new ArrayList<>();


        list.clear();
        pList.clear();
        ce = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("jun", "ChildAdded");
                PostInfo p = dataSnapshot.getValue(PostInfo.class);
                Log.d("jun", p.getTitle());
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

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        myRef.child("posts").addChildEventListener(ce);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 아이템 버튼 작동
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(VideoListActivity.this ,list.get(position). getV_title(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), HyunWooActivity.class);
                String recipeId = list.get(position).getRecipe_id();
                intent.putExtra("id", recipeId);
                intent.putExtra("userid", list.get(position).getV_uploader().substring(1));

                myRef.child("posts").child(recipeId).child("views").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        views = dataSnapshot.getValue(Integer.class);
                        views++;
                        myRef.child("posts").child(recipeId).child("views").setValue(views).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                list.get(position).setView_num("\n" + Integer.toString(views) + " views");

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
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

        /* editsearch 사용창
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editsearch.getText().toString()
                        .toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                VideoItem item = (VideoItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getV_title() ;
                String descStr = item.getV_uploader() ;

                // TODO : use item data.
            }
        }) ; */

    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == DELETE_OK){
            list.remove(pos);
            adapter = new VideoAdapter(c, list);
            listview.setAdapter(adapter);
        }
    }




    //메뉴 생성하는 onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // 메뉴 버튼 클릭 시 조건별 검색 스피너들 출력을 위한 동적 레이아웃

        TableLayout table = findViewById(R.id.category_tableLayout);
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 150);
        params.weight = 1;

        TableRow tr1 = new TableRow(this);
        TableRow tr2 = new TableRow(this);
        tr1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tr2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));



        Spinner spinnerServings = new Spinner(this);
        spinnerServings.setLayoutParams(params);
        ArrayAdapter servingsAdapter = ArrayAdapter.createFromResource(this, R.array.data_servings, android.R.layout.simple_spinner_item);
        servingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServings.setAdapter(servingsAdapter);

        Spinner spinner_mainIng = new Spinner(this);
        spinner_mainIng.setLayoutParams(params);
        ArrayAdapter mainIng_Adapter = ArrayAdapter.createFromResource(this, R.array.data_mainIng, android.R.layout.simple_spinner_item);
        mainIng_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mainIng.setAdapter(mainIng_Adapter);

        Spinner spinner_type = new Spinner(this);
        spinner_type.setLayoutParams(params);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.data_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(typeAdapter);

        Spinner spinner_feature = new Spinner(this);
        spinner_feature.setLayoutParams(params);
        ArrayAdapter featureAdapter = ArrayAdapter.createFromResource(this, R.array.data_feature, android.R.layout.simple_spinner_item);
        featureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_feature.setAdapter(featureAdapter);

        //search_menu.xml 등록
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        mSearch=menu.findItem(R.id.search);

        //메뉴 아이콘 클릭했을 시 확장, 취소했을 시 축소
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                tr1.addView(spinnerServings);
                tr1.addView(spinner_mainIng);
                tr2.addView(spinner_type);
                tr2.addView(spinner_feature);
                table.addView(tr1);
                table.addView(tr2);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                table.removeView(tr2);
                table.removeView(tr1);
                tr2.removeView(spinner_feature);
                tr2.removeView(spinner_type);
                tr1.removeView(spinner_mainIng);
                tr1.removeView(spinnerServings);
                return true;
            }
        });


        //menuItem을 이용해서 SearchView 변수 생성
        SearchView sv=(SearchView)mSearch.getActionView();
        //확인버튼 활성화
        sv.setSubmitButtonEnabled(true);

        //SearchView의 검색 이벤트
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {


                //   TextView text = (TextView)findViewById(R.id.txtresult);
                //   text.setText(query + "를 검색합니다.");
                Log.d("search", query);
                //get all posts.
                adapter.filter(query, spinnerServings.getSelectedItem().toString(),
                        spinner_mainIng.getSelectedItem().toString(),
                        spinner_type.getSelectedItem().toString(),
                        spinner_feature.getSelectedItem().toString());


                return true;
            }

            //텍스트가 바뀔때마다 호출
            @Override
            public boolean onQueryTextChange(String newText) {
                //TextView text = (TextView)findViewById(R.id.txtsearch);
                //text.setText("검색식 : "+newText);


                if(newText.equals("")){
                    this.onQueryTextSubmit("");
                }
                return true;
            }
        });
        return true;
    }


    // 상단 탭 메뉴
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int curId = item.getItemId();
//        switch(curId) {
//            case R.id.menu_refresh:
//                Toast.makeText(this, "새로고침 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
//                break;if(newText.equals("")){
//                this.onQueryTextSubmit("");
//            }
//            case R.id.menu_search:
//                Toast.makeText(this, "검색 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_settings:
//                Toast.makeText(this, "설정 메뉴가 선택되었습니다.", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
