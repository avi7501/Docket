package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    RecyclerView recyclerView;
    private DatabaseReference myRef;
    SearchView searchView;
    ImageView gif;
    ImageView userProfileImage,acc1,accdp;
    FirebaseAuth firebaseAuth;

    //Variables
    private ArrayList<CourseData> courseList;
    private RecycleAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView=findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setQueryHint("Search here");
        recyclerView=findViewById(R.id.searchR);
        gif=(ImageView)findViewById(R.id.gif);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        accdp =findViewById(R.id.accdp);

        //ArrayList
        courseList = new ArrayList<>();
        ImageView home=(ImageView)findViewById(R.id.homebtn);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i =new Intent(Search.this,Dashboard.class);
                Bundle bundle = getIntent().getExtras();
                i.putExtra("isGAuth",bundle.getBoolean("isGAuth"));
                startActivity(i);
            }
        });
        ImageView acc =(ImageView)findViewById(R.id.accbtn);
        acc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i =new Intent(Search.this,AccSettings.class);
                Bundle bundle = getIntent().getExtras();
                i.putExtra("isGAuth",bundle.getBoolean("isGAuth"));
                startActivity(i);
            }
        });

        //Clear Array List
        clearAll();
        Bundle bundle = getIntent().getExtras();
        boolean isGAuth = bundle.getBoolean("isGAuth");
        System.out.println("Auth Value :"+isGAuth);
        /* Setting UserName And Profile */
        if(isGAuth)
        {
            if(firebaseUser != null)
            {

                Glide.with(Search.this)
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
                    Glide.with(Search.this)
                            .load(R.drawable.def_prof)
                            .into(accdp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Get Data  Method
        GetDataFromFirebase();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                if(newText.equals("")){
                    recyclerView.setVisibility(View.INVISIBLE);
                   gif.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
//        searchRecycle();
    }

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
    private void filterList(String Text) {
        ArrayList<CourseData> filteredList=new ArrayList<>();
        for(CourseData course :courseList){
            if(course.getCourse_name().toLowerCase().contains(Text.toLowerCase())){
                filteredList.add(course);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"No data found",Toast.LENGTH_LONG).show();
        }
        else{
            recyclerAdapter.setfilteredList(filteredList);
            recyclerView.setVisibility(View.VISIBLE);
            gif.setVisibility(View.INVISIBLE);


        }

    }
}