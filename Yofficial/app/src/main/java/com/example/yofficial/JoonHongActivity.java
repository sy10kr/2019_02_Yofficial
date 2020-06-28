package com.example.yofficial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class JoonHongActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity ac = this;
    private final static String TAG = "Joon!";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joonhong);
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, mAuth.getCurrentUser().getUid());
        Log.d(TAG, mAuth.getCurrentUser().getEmail());
        tv = findViewById(R.id.textView);
        tv.setText("\n    Hi "+ mAuth.getCurrentUser().getDisplayName()+"!!\n    Welcome to Yofficial");


        //if user is not in users make new userInfo instance and save it into db
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Log.d(TAG, "get database reference");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.d(TAG, "Enter into on data change");
                //Log.d(TAG, mAuth.getCurrentUser().getEmail());

                if (mAuth.getCurrentUser() != null) {
                    String userEmail = mAuth.getCurrentUser().getEmail();
                    String userId = userEmail.split("@")[0];
                    Log.d(TAG, userId);


                    if(!dataSnapshot.child("users").child(userId).exists()){
                        Log.d(TAG, "not exists");
                        UserInfo u = new UserInfo();
                        u.setId(userId);
                        u.setChefExp(0);
                        u.setChefLevel(0);
                        u.setCookExp(0);
                        u.setCookLevel(0);

                        myRef.child("users").child(u.getId()).setValue(u);
                    }
                    else{
                        Log.d(TAG, "Exists");
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //else do nothing.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.startmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.signoutbtn:

                mAuth.signOut();

                if(mAuth.getCurrentUser() == null){

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("903484886790-84aqko9kngrqeohq586c1jujt0ide1vb.apps.googleusercontent.com")
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(ac, gso);
                    mGoogleSignInClient.signOut();
                }

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);


            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void mypage(View view){
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        startActivity(intent);
    }



    // onButton1Clicked에 대한 로직

    public void onButton1Clicked(View view){
        // LENGTH_LONG : 길게 화면에 나타남
        // LENGTH_SHORT : 짧게 화면에 나타남
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
        startActivity(intent);
        //Intent intent = new Intent(
             //   getApplicationContext(), // 현재 화면의 제어권자
               // VideoListActivity.class); // 다음 넘어갈 클래스 지정
        //intent.putExtra();
       // startActivity(intent); // 다음 화면으로 넘어간다

    }

    public void onButton2Clicked(View view){
        // LENGTH_LONG : 길게 화면에 나타남
        // LENGTH_SHORT : 짧게 화면에 나타남
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                VideoListActivity.class); // 다음 넘어갈 클래스 지정
        //intent.putExtra();
        startActivity(intent); // 다음 화면으로 넘어간다

    }

    public void onButton5Clicked(View view){//커뮤니티 버튼
        // LENGTH_LONG : 길게 화면에 나타남
        // LENGTH_SHORT : 짧게 화면에 나타남
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
        startActivity(intent);


    }

    public void onButton4Clicked(View view){
        // LENGTH_LONG : 길게 화면에 나타남
        // LENGTH_SHORT : 짧게 화면에 나타남
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                RefregActivity.class); // 다음 넘어갈 클래스 지정
        //intent.putExtra();
        startActivity(intent); // 다음 화면으로 넘어간다

    }

    public void onButton3Clicked(View view){
        // LENGTH_LONG : 길게 화면에 나타남
        // LENGTH_SHORT : 짧게 화면에 나타남
        Toast.makeText(getApplicationContext(), "버튼이 눌렸습니다!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                getApplicationContext(), // 현재 화면의 제어권자
                RecommendActivity.class); // 다음 넘어갈 클래스 지정
        //intent.putExtra();
        startActivity(intent); // 다음 화면으로 넘어간다
    }
}
