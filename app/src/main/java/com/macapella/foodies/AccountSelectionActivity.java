package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AccountSelectionActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_selection);
    }

    public void onAdmin(View view) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mData.child("Users").child(mAuth.getCurrentUser().getUid()).child("admin")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Boolean admin = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        if (admin == true) {
                            Intent intent = new Intent(context, AdminActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void onDelivery(View view) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mData.child("Users").child(mAuth.getCurrentUser().getUid()).child("delivery")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Boolean delivery = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        if (delivery == true) {
                            Intent intent = new Intent(context, DeliveryActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void onCustomer(View view) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mData.child("Users").child(mAuth.getCurrentUser().getUid()).child("customer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Boolean customer = Boolean.parseBoolean(dataSnapshot.getValue().toString());
                        if (customer == true) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}