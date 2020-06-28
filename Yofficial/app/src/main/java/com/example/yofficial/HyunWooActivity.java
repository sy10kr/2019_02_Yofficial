package com.example.yofficial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HyunWooActivity extends AppCompatActivity {
    RecipeInfo refo;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private Activity activity;
    private Context c = this;
    private String u_id;
    private String id;

    private static final String TAG = "HyunWoo!";
    private static final int DELETE_OK = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Bundle extras = getIntent().getExtras();
        id = extras.getString("id");
        u_id = extras.getString("userid");
        Log.d("HyunWoo!", id);
        Log.d("Hyunwoo!", u_id);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.child("recipes").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Log.d(TAG, "enterDataChange");
                refo = dataSnapshot.getValue(RecipeInfo.class);
                Log.d(TAG, refo.getRecipeTitle() + " " + refo.getRecipeId());


                // drawable에 있는 이미지를 지정합니다.
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference().child("images/" + refo.getRecipeId() +".jpg");
                storageRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        Log.d(TAG, "Succeeded");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        BitmapDrawable img = new BitmapDrawable(getResources(), bitmap);



                        setContentView(R.layout.activity_hyunwoo);

                        //db정보가 있는 RecipeInfo클래스에서 정보 받아오기
                        TextView recipeTitle = (TextView)findViewById(R.id.title);
                        recipeTitle.setText(refo.getRecipeTitle());
                        TextView recipeSubTitle = (TextView)findViewById(R.id.sub_title);
                        recipeSubTitle.setText(refo.getRecipeSubTitle());
                        TextView introEdit = (TextView) findViewById(R.id.introRecipe);
                        introEdit.setText(refo.getIntroRecipe());
                        TextView serveNum = (TextView)findViewById(R.id.serveNum);
                        serveNum.setText(refo.getServings());
                        TextView difficulty = (TextView)findViewById(R.id.difficulty);
                        difficulty.setText(refo.getDifficulty());
                        TextView duraTime = (TextView)findViewById(R.id.duraTime);
                        duraTime.setText(refo.getDuraTime());
                        TextView ingredientList = (TextView)findViewById(R.id.ingredientList);

                        StringBuilder ingreds = new StringBuilder();
                        for(int i = 0; i < refo.getIngredientName().size(); i++){

                            if (i == refo.getIngredientName().size() - 1) {
                                ingreds.append(refo.getIngredientName().get(i));
                                ingreds.append(" ");
                                ingreds.append(refo.getIngredientAmount().get(i));
                            } else {
                                ingreds.append(refo.getIngredientName().get(i));
                                ingreds.append(" ");
                                ingreds.append(refo.getIngredientAmount().get(i));
                                ingreds.append(", ");
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            }

                        }

                        ingredientList.setText(ingreds.toString());


                        TextView seasoningList = (TextView)findViewById(R.id.seasoningList);

                        StringBuilder seasons = new StringBuilder();
                        for(int i = 0; i < refo.getSeasoningName().size(); i++){
                            if (i == refo.getSeasoningName().size() - 1) {
                                seasons.append(refo.getSeasoningName().get(i));
                                seasons.append(" ");
                                seasons.append(refo.getSeasoningAmount().get(i));
                            } else {
                                seasons.append(refo.getSeasoningName().get(i));
                                seasons.append(" ");
                                seasons.append(refo.getSeasoningAmount().get(i));
                                seasons.append(", ");
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            }

                        }

                        seasoningList.setText(seasons.toString());

                        //뷰, 버튼들의 선언부
                        ImageView imageView = (ImageView)findViewById(R.id.imageview);
                        ImageView yiconView = (ImageView)findViewById(R.id.yicon);
                        ImageView servings = (ImageView)findViewById(R.id.servings);
                        ImageView level = (ImageView)findViewById(R.id.level);
                        ImageView duration = (ImageView)findViewById(R.id.duration);
                        ImageView youtubeUrl = (ImageView) findViewById(R.id.youtubeUrl);



                        yiconView.setImageResource(R.drawable.yicon);

                        if (refo.getServings().equals("1인분")) {
                            servings.setImageResource(R.drawable.one_person);
                        } else {
                            servings.setImageResource(R.drawable.many_people);
                        }

                        switch (refo.getDifficulty()) {
                            case "브론즈":
                                level.setImageResource(R.drawable.bronze);
                                break;
                            case "실버":
                                level.setImageResource(R.drawable.silver);
                                break;
                            case "골드":
                                level.setImageResource(R.drawable.gold);
                                break;
                            case "다이아":
                                level.setImageResource(R.drawable.diamond);
                                break;
                            case "마스터":
                                level.setImageResource(R.drawable.master);
                                break;
                            default:
                                level.setImageResource(R.drawable.level);
                                break;
                        }
                        duration.setImageResource(R.drawable.time);



                        Log.d(TAG, "URL : " + refo.getYoutubeUrl());

                        youtubeUrl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), YouTubeActivity.class);
                                intent.putExtra("url", refo.getYoutubeUrl());
                                intent.putExtra("startTime", refo.getStartTime());
                                intent.putExtra("endTime", refo.getEndTime());
                                intent.putExtra("stepDesc", refo.getStepDescrib());
                                startActivity(intent);
                            }
                        });

                        imageView.setImageDrawable(img);


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {



                        setContentView(R.layout.activity_hyunwoo);
                        //db정보가 있는 RecipeInfo클래스에서 정보 받아오기
                        TextView recipeTitle = (TextView)findViewById(R.id.title);
                        recipeTitle.setText(refo.getRecipeTitle());
                        TextView recipeSubTitle = (TextView)findViewById(R.id.sub_title);
                        recipeSubTitle.setText(refo.getRecipeSubTitle());
                        TextView introEdit = (TextView) findViewById(R.id.introRecipe);
                        introEdit.setText(refo.getIntroRecipe());
                        TextView serveNum = (TextView)findViewById(R.id.serveNum);
                        serveNum.setText(refo.getServings());
                        TextView difficulty = (TextView)findViewById(R.id.difficulty);
                        difficulty.setText(refo.getDifficulty());
                        TextView duraTime = (TextView)findViewById(R.id.duraTime);
                        duraTime.setText(refo.getDuraTime());
                        TextView ingredientList = (TextView)findViewById(R.id.ingredientList);

                        StringBuilder ingreds = new StringBuilder();
                        for(int i = 0; i < refo.getIngredientName().size(); i++){
                            if (i == refo.getIngredientName().size() - 1) {
                                ingreds.append(refo.getIngredientName().get(i));
                                ingreds.append(" ");
                                ingreds.append(refo.getIngredientAmount().get(i));
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            } else {
                                ingreds.append(refo.getIngredientName().get(i));
                                ingreds.append(" ");
                                ingreds.append(refo.getIngredientAmount().get(i));
                                ingreds.append(", ");
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            }

                        }

                        ingredientList.setText(ingreds.toString());


                        TextView seasoningList = (TextView)findViewById(R.id.seasoningList);

                        StringBuilder seasons = new StringBuilder();
                        for(int i = 0; i < refo.getSeasoningName().size(); i++){
                            if (i == refo.getSeasoningName().size() - 1) {
                                seasons.append(refo.getSeasoningName().get(i));
                                seasons.append(" ");
                                seasons.append(refo.getSeasoningAmount().get(i));
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            } else {
                                seasons.append(refo.getSeasoningName().get(i));
                                seasons.append(" ");
                                seasons.append(refo.getSeasoningAmount().get(i));
                                seasons.append(", ");
                                Log.d(TAG, "cur SB = " + ingreds.toString());
                            }

                        }

                        seasoningList.setText(seasons.toString());

                        //뷰, 버튼들의 선언부
                        ImageView imageView = (ImageView)findViewById(R.id.imageview);
                        ImageView yiconView = (ImageView)findViewById(R.id.yicon);
                        ImageView servings = (ImageView)findViewById(R.id.servings);
                        ImageView level = (ImageView)findViewById(R.id.level);
                        ImageView duration = (ImageView)findViewById(R.id.duration);
                        ImageView youtubeUrl = (ImageView) findViewById(R.id.youtubeUrl);



                        yiconView.setImageResource(R.drawable.yicon);

                        if (refo.getServings().equals("1인분")) {
                            servings.setImageResource(R.drawable.filled_star);
                        } else {
                            servings.setImageResource(R.drawable.empty_star);
                        }

                        switch (refo.getDifficulty()) {
                            case "브론즈":
                                level.setImageResource(R.drawable.empty_star);
                                break;
                            case "실버":
                                level.setImageResource(R.drawable.filled_star);
                                break;
                            case "골드":
                                level.setImageResource(R.drawable.empty_star);
                                break;
                            case "다이아":
                                level.setImageResource(R.drawable.filled_star);
                                break;
                            case "마스터":
                                level.setImageResource(R.drawable.empty_star);
                            default:
                                level.setImageResource(R.drawable.level);
                                break;
                        }

                        switch (refo.getDuraTime()) {
                            case "5분":
                                duration.setImageResource(R.drawable.empty_star);
                                break;
                            case "10분":
                                duration.setImageResource(R.drawable.filled_star);
                                break;
                            case "15분":
                                duration.setImageResource(R.drawable.empty_star);
                                break;
                            case "30분":
                                duration.setImageResource(R.drawable.filled_star);
                                break;
                            case "1시간":
                                duration.setImageResource(R.drawable.empty_star);
                                break;
                            case "2시간 이상":
                                duration.setImageResource(R.drawable.filled_star);
                            default:
                                duration.setImageResource(R.drawable.duration);
                                break;
                        }



                        youtubeUrl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), YouTubeActivity.class);

                                intent.putExtra("url", refo.getYoutubeUrl());
                                intent.putExtra("startTime", refo.getStartTime());
                                intent.putExtra("endTime", refo.getEndTime());
                                intent.putExtra("stepDesc", refo.getStepDescrib());
                                startActivity(intent);
                            }
                        });

                        Log.d(TAG, "Failed");
                        Drawable img = ContextCompat.getDrawable(c, R.drawable.fail);
                        imageView.setImageDrawable(img);



                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        /*
        //db에서 쏴 준 정보 임시 문자열 테이블에 저장, 나중에 db연동 시 수정
        ingredients = new String[dbIng.length];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = dbIng[i];
        }
        refo.setIngredient(ingredients);

        seasonings = new String[dbSsn.length];
        for (int i = 0; i < seasonings.length; i++) {
            seasonings[i] = dbSsn[i];
        }
        refo.setSeasoning(seasonings);

         */





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        activity = this;

        switch (item.getItemId()) {
            case R.id.recipe_delete_btn:

               // 레시피 정보를 디비에서 지우기

                Log.d(TAG, "get recipe delete btn");
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);
                // 다이얼로그 메세지
                alertdialog.setMessage("\n레시피를 삭제하시겠습니까?");


                // 확인버튼
                alertdialog.setNegativeButton("확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth = FirebaseAuth.getInstance();
                        String userID = mAuth.getCurrentUser().getEmail().split("@")[0];



                        if(u_id.compareTo(userID) == 0){


                            myRef.child("posts").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    myRef.child("recipes").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(c ,"Deleted",Toast.LENGTH_SHORT).show();
                                            setResult(DELETE_OK);
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(c ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(c ,"다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                                }
                            });




                        }
                        else{
                            Toast.makeText(c ,"본인 레시피만 삭제 가능합니다!",Toast.LENGTH_SHORT).show();
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


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
