package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    //variables Used To Get The Data
    private Button logout,googleSignOut;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Intializing Variables
        logout = findViewById(R.id.signOut);
        googleSignOut = findViewById(R.id.googleSignOutBtn);
        //Adding OnClickListener
        logout.setOnClickListener(this);

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        //GOOGLE SIGN OUT
        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(Dashboard.this
                , GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignOut.setOnClickListener(new View.OnClickListener() {
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
                            Intent i = new Intent(Dashboard.this,MainActivity.class);
                            startActivity(i);

                            // Finish activity
                            finish();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signOut:
                //Calling SignOut User Method
                signOutUser();
                break;
        }
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Dashboard.this,MainActivity.class);
        startActivity(i);
    }
}