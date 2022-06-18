package com.example.madproj;

public class CourseData {
    //Model Class
    String course_name;
    String course_video_thumbnail_url;
    String course_video_url;
    String organiser;


    //Constructor
    public CourseData()
    {

    }

    public CourseData(String course_name, String course_video_thumbnail_url,String course_video_url,String organiser) {
        this.course_name = course_name;
        this.course_video_thumbnail_url = course_video_thumbnail_url;
        this.course_video_url = course_video_url;
        this.organiser = organiser;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_video_thumbnail_url() {
        return course_video_thumbnail_url;
    }

    public void setCourse_video_thumbnail_url(String course_video_thumbnail_url) {
        this.course_video_thumbnail_url = course_video_thumbnail_url;
    }
    public String getCourse_video_url(){return course_video_url;}

    public  void setCourse_video_url(String course_video_url){this.course_video_url=course_video_url;}

    public String getOrganiser(){return organiser;}
    public void setOrganiser(String organiser){this.organiser = organiser;}
}
