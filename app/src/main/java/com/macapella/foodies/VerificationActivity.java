package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("fullname").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    EditText editText = (EditText) findViewById(R.id.orderName);
                    editText.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    EditText editText = (EditText) findViewById(R.id.orderEmail);
                    editText.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    EditText editText = (EditText) findViewById(R.id.orderPhoneNumber);
                    editText.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("coordinates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    TextView textView = (TextView) findViewById(R.id.orderAddress);
                    String longitude = task.getResult().child("longitude").getValue().toString();
                    String latitude = task.getResult().child("latitude").getValue().toString();
                    String coordinates = latitude + ", " + longitude;
                    textView.setText(coordinates);
                }
            }
        });


        Context context = this;


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ItemModel> itemModelList = new ArrayList<ItemModel>();
                            QuerySnapshot querySnapshot = task.getResult();
                            querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setPrice(Integer.parseInt(documentSnapshot.getString("price"))); itemModel.setQuantity(Integer.parseInt(documentSnapshot.getString("quantity"))); itemModelList.add(itemModel);});
                            OrderRecyclerViewAdapter orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(context, itemModelList);
                            recyclerView = (RecyclerView) findViewById(R.id.verificationRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(orderRecyclerViewAdapter);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void toPayment (View view) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("coordinates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    LatLng coordinates = new LatLng(Double.parseDouble(task.getResult().child("latitude").getValue().toString()), Double.parseDouble(task.getResult().child("longitude").getValue().toString()));


                    Map <String, Object> orderTempInfo = new HashMap<>();
                    EditText orderName = (EditText) findViewById(R.id.orderName);
                    EditText orderEmail = (EditText) findViewById(R.id.orderEmail);
                    EditText orderPhone = (EditText) findViewById(R.id.orderPhoneNumber);
                    orderTempInfo.put("tempName", orderName.getText().toString());
                    orderTempInfo.put("tempEmail", orderEmail.getText().toString());
                    orderTempInfo.put("tempPhone", orderPhone.getText().toString());
                    orderTempInfo.put("latitude", coordinates.latitude);
                    orderTempInfo.put("longitude", coordinates.longitude);
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .set(orderTempInfo);
                }
            }
        });


        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }
}