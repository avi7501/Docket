package com.example.madproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    //variables Used To Get The Data
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Intializing Variables
        logout = findViewById(R.id.signOut);
        //Adding OnClickListener
        logout.setOnClickListener(this);

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