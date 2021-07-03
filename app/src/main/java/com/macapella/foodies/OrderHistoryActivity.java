package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("active-orders");
        userID = user.getUid();
        final TextView Orderstatus = (TextView) findViewById(R.id.status);
        final TextView Orderphone = (TextView) findViewById(R.id.orderPhone);
        final TextView Orderlocation = (TextView) findViewById(R.id.orderLocation);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User historyOrder = snapshot.getValue(User.class);

                if(historyOrder != null){
                    String name = historyOrder.fullname;
                    String email = historyOrder.email;
                    String phone = historyOrder.phone;

                    Orderstatus.setText("Welcome, " + name);
                    Orderphone.setText("Name: " + name);
                    Orderlocation.setText("Email" + email);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(OrderHistoryActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}