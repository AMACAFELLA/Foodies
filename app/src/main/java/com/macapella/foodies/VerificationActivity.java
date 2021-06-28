package com.macapella.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {

    public Map<String, Object> order = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String name = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("fullname").get().toString();
        String email = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("email").get().toString();
        String phoneNumber = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").get().toString();
        String longitude = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("coordinates").child("longitude").;

    }

    public void toPayment (View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}