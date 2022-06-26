package com.example.madproj;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<CourseData> courseList;

    public RecycleAdapter(Context mContext, ArrayList<CourseData> courseList) {
        this.mContext = mContext;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.goToCourse.setTag(position);
        holder.goToCourse.setText(courseList.get(position).getCourse_name());
        holder.textView.setText("By : "+courseList.get(position).getOrganiser());
        Glide.with(mContext).load(courseList.get(position).getCourse_video_thumbnail_url()).into(holder.imageView);
//        String CourseName = holder.textView.getText().toString();
//        System.out.println("Course Name : "+CourseName);
        holder.goToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting position of the card
                Integer cardPosition = (Integer) view.getTag();
                //extracting data from array list to transfer it to video player
                String courseName = courseList.get(cardPosition).getCourse_name();
                String videoId =courseList.get(cardPosition).getCourse_video_url().split("=",2)[1];
                String courseOrganization = courseList.get(cardPosition).getOrganiser();
//                String courseDesc=courseList.get(cardPosition)
                Intent intent = new Intent(mContext,VideoPlayer.class);
                Bundle bundle = new Bundle();
                bundle.putString("courseName",courseName);
                bundle.putString("videoId",videoId);
                bundle.putString("courseOrganization",courseOrganization);
                intent.putExtra("allCourseData",bundle);
                view.getContext().startActivity(intent);
//                System.out.println("CourseName : "+courseName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        //testing
        Button goToCourse;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.courseImageView);
            textView = itemView.findViewById(R.id.organiser);
            //testing
            goToCourse = itemView.findViewById(R.id.goToCourse);
        }
    }
}

