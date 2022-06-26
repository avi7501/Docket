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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccSettings extends AppCompatActivity implements View.OnClickListener{
    ImageView accdp;
    private ImageView logout,home,googleSignOut;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    TextView userNameDisplayText;
    ImageView userAccountProfileImage;
    //Firebase
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_settings);
        logout = (ImageView) findViewById(R.id.logout);
        accdp=findViewById(R.id.navDp);
        userAccountProfileImage=findViewById(R.id.userDp);
        userNameDisplayText=findViewById(R.id.userName);
        home=(ImageView)findViewById(R.id.homeBtn);
        home.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        //Database
        myRef = FirebaseDatabase.getInstance().getReference();
        //Testing
        Bundle bundle = getIntent().getExtras();
        boolean isGAuth = bundle.getBoolean("isGAuth");
        //End Of Testing
        googleSignInClient= GoogleSignIn.getClient(AccSettings.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from google

                if(isGAuth)
                {
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
                }else
                {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getBaseContext(), "Successfully Logged Out", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AccSettings.this,MainActivity.class);
                    startActivity(i);
                }

            }
        });

        /* Setting UserName And Profile */
        if(isGAuth)
        {
            if(firebaseUser != null)
            {
                userNameDisplayText.setText(firebaseUser.getDisplayName());
                Glide.with(AccSettings.this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(userAccountProfileImage);
                Glide.with(AccSettings.this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(accdp);
            }
        }
        else
        {
            Glide.with(AccSettings.this)
                    .load(R.drawable.def_prof)
                    .into(userAccountProfileImage);
            Glide.with(AccSettings.this)
                    .load(R.drawable.def_prof)
                    .into(accdp);
            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userNameDisplayText.setText(snapshot.child("user_name").getValue().toString());
                    System.out.println("Testing : "+snapshot.child("user_name").getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.homeBtn:
                Bundle bundle = getIntent().getExtras();
                Intent i = new Intent(AccSettings.this,Dashboard.class);
                i.putExtra("isGAuth",bundle.getBoolean("isGAuth"));
                startActivity(i);
        }
    }
}