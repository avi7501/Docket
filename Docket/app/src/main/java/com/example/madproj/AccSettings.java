package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccSettings extends AppCompatActivity implements View.OnClickListener{
    ImageView accdp;
    private ImageView logout,home,googleSignOut;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    TextView userNameDisplayText;
    ImageView userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_settings);
        logout = (ImageView) findViewById(R.id.logout);
        accdp=findViewById(R.id.navDp);
        userProfileImage=findViewById(R.id.userDp);
        userNameDisplayText=findViewById(R.id.userName);
        home=(ImageView)findViewById(R.id.homeBtn);
        home.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        googleSignInClient= GoogleSignIn.getClient(AccSettings.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from google
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Check condition
                        if(task.isSuccessful())
                        {
                            // When task is successful
                            // Sign out from firebase
                            firebaseAuth.signOut();

                            // Display Toast
                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AccSettings.this,MainActivity.class);
                            startActivity(i);

                            // Finish activity
                            finish();
                        }
                    }
                });
            }
        });

        /* Setting UserName And Profile */
        if(firebaseUser != null)
        {
            userNameDisplayText.setText(firebaseUser.getDisplayName());
            Glide.with(AccSettings.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(userProfileImage);
            Glide.with(AccSettings.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(accdp);
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.homeBtn:
                Intent i = new Intent(AccSettings.this,Dashboard.class);
                startActivity(i);
        }
    }
}