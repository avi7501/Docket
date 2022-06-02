package com.example.madproj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transparentStatusBarAndNavigation();
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setEnterTransition(null);
        signup=(Button) findViewById(R.id.signup);
        login=(Button) findViewById(R.id.login);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }
    public void onClick(View v){
        if(v.equals(signup)){
            Intent i = new Intent(MainActivity.this,SignUp.class);
            startActivity(i);
        }
        if(v.equals(login)){
            Intent i = new Intent(MainActivity.this,Login.class);
            startActivity(i);
        }
    }
    @SuppressLint("ObsoleteSdkInt")

    private void transparentStatusBarAndNavigation(){
        if(Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,true);
        }
        if(Build.VERSION.SDK_INT>=19){
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
        if(Build.VERSION.SDK_INT>=21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }
    private void setWindowFlag(int i,boolean b){
        Window win = getWindow();
        WindowManager.LayoutParams winParams= win.getAttributes();
        if(b){
            winParams.flags |= i;
        }
        else{
            winParams.flags &= ~i;
        }
        win.setAttributes(winParams);

    }
}