package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AccountActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;
    private Button orderHist;
    private Button adminP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        logout = (Button)findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }
        });
        orderHist = (Button) findViewById(R.id.orderHist);
        orderHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, OrderHistoryActivity.class));
            }
        });
        adminP = (Button) findViewById(R.id.adminPage);
        adminP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AdminActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView welcomeTextView = (TextView) findViewById(R.id.welcome);
        final TextView Accountname = (TextView) findViewById(R.id.Accountname);
        final TextView Accountemail = (TextView) findViewById(R.id.Accountemail);
        final TextView Accountphone = (TextView) findViewById(R.id.Accountphone);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.fullname;
                    String email = userProfile.email;
                    String phone = userProfile.phone;

                    welcomeTextView.setText("Welcome, " + name);
                    Accountname.setText("Name: " + name);
                    Accountemail.setText("Email" + email);
                    Accountphone.setText("Phone Number: " + phone);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}