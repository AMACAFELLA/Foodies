package com.macapella.foodies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //private variables for elements on main.
    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    //When user clicks on login process bar will spin until successful or failed login
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //inilizes elements
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signin);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
                //User clicks on signin we start a new activity.
            case R.id.signin:
                //create new method
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        //user into converts to string for validation. .term() in case of extra space.
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Validate user email. Check to see if user provided an email, if not sets an error.
        if(email.isEmpty()){
            editTextEmail.setError("Enter your email!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Provide a valid email address!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Enter your password!");
            editTextPassword.requestFocus();
            return;
        }
        //Check to the if password length meets required length, if not set an error.
        if(password.length() < 6){
            editTextPassword.setError("The min password length is six characters!");
            editTextPassword.requestFocus();
            return;

        }

        //progressbar keeps spinning until the user is logged in.
        progressBar.setVisibility(View.VISIBLE);

        //Authication to sign the user in.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Check to see if user is verified
                    if(user.isEmailVerified()) {
                        //redirect to menu not sure what activity that will be.
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                    //if the user is not send a email verification link.
                    else{
                        user.sendEmailVerification();
                        //display toast that email has been sent
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                } else{
                    //display error message
                    Toast.makeText(LoginActivity.this, "Failed to login. Check your login details", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}