package com.macapella.foodies;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //assigning fields to resources
        emailEditText = (EditText) findViewById(R.id.email);
        resetPasswordButton = (Button) findViewById(R.id.resetPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        //Button reset password
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword(){
        //Validate email
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Enter your email.");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            //If email matched the email in database user will be sent to users account to rest password, else error.
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Email sent, check you email to rest your password.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPassword.this, "Something went wrong! Try again.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}