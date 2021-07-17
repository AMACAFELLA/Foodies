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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    public RecyclerView recyclerView;
    Context context = this;


    private String userID;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        userID = user.getUid();
        mData.child("Users").child(userID).child("active-order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String orderNumber = dataSnapshot.getValue().toString();

                        db.collection("active-orders").document(orderNumber)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            String name = documentSnapshot.getString("name");
                                            String orderNumber = documentSnapshot.getString("order-number");
                                            String status = documentSnapshot.getString("status");
                                            String totalToBePaid = "E " + documentSnapshot.getString("totalPrice") + "0";

                                            TextView nameText = (TextView) findViewById(R.id.activeOrderName);
                                            TextView numberText = (TextView) findViewById(R.id.activeOrderNumber);
                                            TextView statusText = (TextView) findViewById(R.id.activeOrderStatus);
                                            TextView totalText = (TextView) findViewById(R.id.activeOrderTotal);

                                            nameText.setText(name);
                                            numberText.setText(orderNumber);
                                            statusText.setText(status);
                                            totalText.setText(totalToBePaid);
                                        } else {
                                            String name = "No current order";
                                            String orderNumber = "No current order";
                                            String status = "No current order";
                                            String totalToBePaid = "No current order";

                                            TextView nameText = (TextView) findViewById(R.id.activeOrderName);
                                            TextView numberText = (TextView) findViewById(R.id.activeOrderNumber);
                                            TextView statusText = (TextView) findViewById(R.id.activeOrderStatus);
                                            TextView totalText = (TextView) findViewById(R.id.activeOrderTotal);

                                            nameText.setText(name);
                                            numberText.setText(orderNumber);
                                            statusText.setText(status);
                                            totalText.setText(totalToBePaid);
                                        }


                                    }
                                });
                    }
                });

        mData.child("Users").child(userID).child("past-orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()){
                            List<OrderModel> orderModelList = new ArrayList<>();
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setName(child.child("name").getValue().toString());
                                orderModel.setStatus(child.child("status").getValue().toString());
                                orderModel.setTotalToPay(child.child("totalPrice").getValue().toString());
                                orderModel.setOrderNumber(child.child("orderNumber").getValue().toString());
                                orderModelList.add(orderModel);
                            }

                            OrderHistoryRecyclerViewAdapter orderHistoryRecyclerViewAdapter = new OrderHistoryRecyclerViewAdapter(context, orderModelList);
                            recyclerView = (RecyclerView) findViewById(R.id.orderHistoryRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(orderHistoryRecyclerViewAdapter);

                        }

                    }
                });
    }

}