package com.example.madproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        transparentStatusBarAndNavigation();
//        Objects.requireNonNull(getSupportActionBar()).hide();
        ImageView image = findViewById(R.id.imageView2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                getWindow().setExitTransition(null);
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, image, Objects.requireNonNull(ViewCompat.getTransitionName(image)));
                startActivity(i, option.toBundle());
                finish();
            }
        }, 3000);
    }
}
//    @SuppressLint("ObsoleteSdkInt")
//    private void transparentStatusBarAndNavigation(){
//        if(Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21){
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,true);
//        }
//        if(Build.VERSION.SDK_INT>=19){
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            );
//        }
//        if(Build.VERSION.SDK_INT>=21){
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//        }
//    }
//    private void setWindowFlag(int i,boolean b){
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams= win.getAttributes();
//        if(b){
//            winParams.flags |= i;
//        }
//        else{
//            winParams.flags &= ~i;
//        }
//        win.setAttributes(winParams);
//
//    }
//}