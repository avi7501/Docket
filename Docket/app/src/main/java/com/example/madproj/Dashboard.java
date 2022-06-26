package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    //variables Used To Get The Data
//    private Button logout,googleSignOut;
    // Widgets
    RecyclerView recyclerView;

    //Firebase
    private DatabaseReference myRef;

    //Variables
    private ArrayList<CourseData> courseList;
    private RecycleAdapter recyclerAdapter;
    private Context mContext;
    TextView userNameDisplayText;
    ImageView userProfileImage,acc1,accdp;


    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    //Variables To Use Bundle Data
    String userNameToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //Intializing Variables
//        logout = findViewById(R.id.signOut);
//        googleSignOut = findViewById(R.id.googleSignOutBtn);
//        //Adding OnClickListener
//        logout.setOnClickListener(this);
        acc1=(ImageView) findViewById(R.id.button8);
        acc1.setOnClickListener(this);
        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        //Intializing Variable To Display UserName
        userNameDisplayText = findViewById(R.id.userDisplayName);
        userProfileImage = findViewById(R.id.userProfileImage);
        accdp =findViewById(R.id.imageView9);

        /*--Intializing Variables For Card Look--*/
        recyclerView = findViewById(R.id.courseListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Firebase
        myRef = FirebaseDatabase.getInstance().getReference();

        //ArrayList
        courseList = new ArrayList<>();

        //Clear Array List
        clearAll();

        //Get Data  Method
        GetDataFromFirebase();

        /*--End Of Variable Intialization--*/
        //GOOGLE SIGN OUT
        // Initialize sign in client
//        googleSignInClient= GoogleSignIn.getClient(Dashboard.this
//                , GoogleSignInOptions.DEFAULT_SIGN_IN);
//        googleSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Sign out from google
//                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Check condition
//                        if(task.isSuccessful())
//                        {
//                            // When task is successful
//                            // Sign out from firebase
//                            firebaseAuth.signOut();
//
//                            // Display Toast
//                            Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(Dashboard.this,MainActivity.class);
//                            startActivity(i);
//
//                            // Finish activity
//                            finish();
//                        }
//                    }
//                });
//            }
//        });
        //testing

        Bundle bundle = getIntent().getExtras();
//        Login logObj = new Login();
        boolean isGAuth = bundle.getBoolean("isGAuth");
        System.out.println("Auth Value :"+isGAuth);
        /* Setting UserName And Profile */
        if(isGAuth)
        {
            if(firebaseUser != null)
            {
                userNameDisplayText.setText(firebaseUser.getDisplayName());
                Glide.with(Dashboard.this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(userProfileImage);
                Glide.with(Dashboard.this)
                        .load(firebaseUser.getPhotoUrl())
                        .into(accdp);
//
            }
        }
        else
        {
            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userNameDisplayText.setText(snapshot.child("user_name").getValue().toString());
                    System.out.println("Testing : "+snapshot.child("user_name").getValue().toString());
                    Glide.with(Dashboard.this)
                            .load(R.drawable.def_prof)
                            .into(userProfileImage);
                    Glide.with(Dashboard.this)
                            .load(R.drawable.def_prof)
                            .into(accdp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

//        else
//        {
//            myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    System.out.println("Testing : "+snapshot.child("user_name").getValue().toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button8:
                Intent it = new Intent(Dashboard.this,AccSettings.class);
                Bundle bundle = getIntent().getExtras();
                it.putExtra("isGAuth",bundle.getBoolean("isGAuth"));
                startActivity(it);
                break;
//            case R.id.signOut:
//                //Calling SignOut User Method
//                signOutUser();
//                break;
        }
    }

//    private void signOutUser() {
//        FirebaseAuth.getInstance().signOut();
//        Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(Dashboard.this,MainActivity.class);
//        startActivity(i);
//    }

    /*-- Methods Used For Card View --*/
    private void GetDataFromFirebase() {
        Query query = myRef.child("CourseList");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    CourseData courseDetails = new CourseData();
                    courseDetails.setCourse_name(snapshot1.child("courseName").getValue().toString());
                    courseDetails.setCourse_video_thumbnail_url(snapshot1.child("videoThumbnailUrl").getValue().toString());
                    courseDetails.setCourse_video_url(snapshot1.child("videoUrl").getValue().toString());
                    courseDetails.setOrganiser(snapshot1.child("organiser").getValue().toString());
                    courseDetails.setCourse_description(snapshot1.child("courseDescription").getValue().toString());
                    courseList.add(courseDetails);
                }
                recyclerAdapter = new RecycleAdapter(getApplicationContext(), courseList);
                recyclerView.setAdapter( recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void clearAll()
    {
        if(courseList != null)
        {
            courseList.clear();
            if(recyclerAdapter != null)
            {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        courseList = new ArrayList<>();
    }
    /*--End Of Methods Used For Card View--*/
}