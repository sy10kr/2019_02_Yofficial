package com.example.yofficial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {


    private Activity ac = this;
    private final static String TAG = "start!";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, mAuth.getCurrentUser().getDisplayName());

        Button start = findViewById(R.id.startbtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoonHongActivity.class);
                startActivity(intent);
            }
        });




        Button signOut = findViewById(R.id.signout);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG, "signed out");
                if(mAuth.getCurrentUser() == null){
                    Log.d(TAG, "signed out succeed");
                    mAuth = FirebaseAuth.getInstance();

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("903484886790-84aqko9kngrqeohq586c1jujt0ide1vb.apps.googleusercontent.com")
                            .requestEmail()
                            .build();



                    mGoogleSignInClient = GoogleSignIn.getClient(ac, gso);
                    mGoogleSignInClient.signOut();
                }



                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });



    }
}
