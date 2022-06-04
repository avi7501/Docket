package com.example.madproj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button signup,login;

    //Variables For Google SignIn
    ImageView googleSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    //End Of Variables For Google SignIn
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

        /***-----------------Google Sign In Part-----------------***/
        // Assign variable
        googleSignIn =(ImageView) findViewById(R.id.googleSignInBtn);

        // Initialize sign in options
        // the client-id is copied form
        // google-services.json file
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("441443608933-nrps0nd18j95ugotf1mei7cc5elg6d5t.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this
                ,googleSignInOptions);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize sign in intent
                Intent intent=googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });
        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(MainActivity.this,Dashboard.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        /***----------------------End Of Google Sign Part------------------***/
    }
    /***---------------------GOOGLE SIGN--------------------------------****/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition

        if(requestCode==100)
        {
            // When request code is equal to 100
            // Initialize task

            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            Toast.makeText(this, "Data"+signInAccountTask, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "result"+signInAccountTask.isSuccessful(), Toast.LENGTH_SHORT).show();

            // check condition
            if(signInAccountTask.isSuccessful())
            {
                // When google sign in successful
                // Initialize string
                Toast.makeText(this, "Works Macha", Toast.LENGTH_SHORT).show();
                String s="Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if(googleSignInAccount!=null)
                    {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if(task.isSuccessful())
                                        {
                                            // When task is successful
                                            // Redirect to profile activity
                                            startActivity(new Intent(MainActivity.this
                                                    ,Dashboard.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            // Display Toast
                                            displayToast("Firebase authentication successful");
                                        }
                                        else
                                        {
                                            // When task is unsuccessful
                                            // Display Toast
                                            displayToast("Authentication Failed :"+task.getException()
                                                    .getMessage());
                                        }
                                    }
                                });

                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(this, "Failed Badly", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    /***------------------------------END OF GOOGLE SIGN IN--------------------------------------***/
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