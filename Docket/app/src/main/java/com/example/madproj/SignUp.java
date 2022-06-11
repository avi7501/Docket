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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    // mauth globally declared
    private FirebaseAuth mAuth;
    //variables for user registration input
    private EditText userNameInput,userEmailInput,userPasswordInput,confirmPasswordInput;
    private ProgressBar progressBar;
    private RadioButton termsAndCondition;
    ImageView submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        transparentStatusBarAndNavigation();

        //Email And Password Authentication
        mAuth = FirebaseAuth.getInstance();

        //linking Backend Variables With Front End Fields
        userNameInput = findViewById(R.id.userName);
        userEmailInput = findViewById(R.id.userEmail);
        userPasswordInput =  findViewById(R.id.userPassowrd);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        progressBar = findViewById(R.id.progressBar);
        submitButton = (ImageView) findViewById(R.id.submitBtn);
        termsAndCondition = findViewById(R.id.radioButton);
        //Adding Click Listener To Button
        submitButton.setOnClickListener(this);
    }
//    @SuppressLint("ObsoleteSdkInt")
//
//    private void transparentStatusBarAndNavigation(){
//        if(Build.VERSION.SDK_INT>=19 && Build.VERSION.SDK_INT<21){
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    |WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,true);
//        }
//        if(Build.VERSION.SDK_INT>=19){
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            );
//        }
//        if(Build.VERSION.SDK_INT>=21){
//            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.submitBtn:
                registerUser();
                break;
        }
    }

    private void registerUser() {
     // Getting The Contents From Input
    String user_name = userNameInput.getText().toString().trim();
    String user_email = userEmailInput.getText().toString().trim();
    String user_password = userPasswordInput.getText().toString().trim();
    String confirm_password = confirmPasswordInput.getText().toString().trim();

    //Validation
        if(user_name.isEmpty()){
            userNameInput.setError("Full Name Required");
            userNameInput.requestFocus();
            return;
        }
        if (user_email.isEmpty())
        {
            userEmailInput.setError("Email Is Required");
            userEmailInput.requestFocus();
            return;
        }
        if(user_password.isEmpty())
        {
            userPasswordInput.setError("Password Is Required");
            userPasswordInput.requestFocus();
            return;
        }
        if(confirm_password.isEmpty())
        {
            confirmPasswordInput.setError("Password Is Required");
            confirmPasswordInput.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            userEmailInput.setError("Please Provide Valid Email");
            userEmailInput.requestFocus();
            return;
        }
        if(user_password.length() < 6)
        {
            userPasswordInput.setError("Minimum Password Length Should Be 6 Characters");
            userPasswordInput.requestFocus();
            return;
        }
        if(user_password.equals(confirm_password) == false)
        {
            confirmPasswordInput.setError("Confirm Password Is Not Matching With The User Password");
            confirmPasswordInput.requestFocus();
            return;
        }
        if(termsAndCondition.isChecked() == false)
        {
            termsAndCondition.setError("Please Agree To Terms And Condition");
            termsAndCondition.requestFocus();
            return;
        }

        //Registering The User
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(user_email,user_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User user = new User(user_name,user_email,user_password);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(SignUp.this, "User Has Been Successfully Registered", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Intent i = new Intent(SignUp.this,Login.class);
                                                startActivity(i);
                                            }else{
                                                Toast.makeText(SignUp.this, "Failed To Register! Try Again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else
                        {
                            Toast.makeText(SignUp.this, "Failed To Register! Try Again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}