package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {
    // Variables Used To Get The Data
    private EditText userLoginEmailInput,userLoginPasswordInput;
    private ImageView signInButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        transparentStatusBarAndNavigation();
        //Intializing Variables
        mAuth = FirebaseAuth.getInstance();
        userLoginEmailInput = findViewById(R.id.userLoginEmail);
        userLoginPasswordInput = findViewById(R.id.userLoginPassword);
        signInButton = (ImageView) findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        //Setting OnClick Listeners
        signInButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loginBtn:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        //Getting Input From Text Box
        String userEmail = userLoginEmailInput.getText().toString().trim();
        String userPassword = userLoginPasswordInput.getText().toString().trim();
        //Validation
        if(userEmail.isEmpty()){
            userLoginEmailInput.setError("Email Required");
            userLoginEmailInput.requestFocus();
            return;
        }
        if(userPassword.isEmpty()){
            userLoginPasswordInput.setError("Password Required");
            userLoginPasswordInput.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            userLoginEmailInput.setError("Please Enter A Valid Email");
            userLoginEmailInput.requestFocus();
            return;
        }
        if(userPassword.length() < 6)
        {
            userLoginPasswordInput.setError("Minimum Number Of Characters In Password Should Be 6");
            userLoginPasswordInput.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        //Signing In
        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);
                    //Checking Whether If The Email Is Verified
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Login.this,Dashboard.class);
                        startActivity(i);
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check Your Email To Verify Your Account", Toast.LENGTH_LONG ).show();
                    }
                }else{
                    Toast.makeText(Login.this, "Failed To Login Please Check Your Credentials", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}