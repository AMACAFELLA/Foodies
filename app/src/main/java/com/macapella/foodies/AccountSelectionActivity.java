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

/**
 * Is responsible for providing admins/drivers the opportunity to select which account use they will continue as
 * This activity is only seen if an account has admin or driver privileges
 * A user may continue as only one of Admin, Driver, or Customer at a time
 */

public class AccountSelectionActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_selection);
    }

    /*
    Called by clicking on the Admin option
    Checks to make sure that an account has Admin privileges
    If an account has Admin privileges, takes the user to the Admin activity
    */
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

    /*
    Called by clicking on the Delivery option
    Checks to make sure that an account has Delivery privileges
    If an account has Admin privileges, takes the user to the Delivery activity
    */
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

    /*
    Called by clicking on the Customer option
    Takes the user to the Admin activity
    */
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