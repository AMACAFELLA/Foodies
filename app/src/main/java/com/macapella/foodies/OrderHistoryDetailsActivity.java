package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for showing the details of a selected order from a user's order history past completed orders list
 * Shows general customer information associated with an order and the items that were ordered
 * Is started from the recyclerview adapter of the Order History activity
 */

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        /*
        Gets an order number from the intent getExtra
        Uses this order number to retrieve information on the order from Firebase
        First gets the customer information of the order and passes the information to the activity layout
        Next gets the information of the items on the order and passes their information to a recyclerview adapter
         */
        Intent intent = getIntent();
        String orderNumber = intent.getStringExtra("orderNumber");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Users").child(mAuth.getCurrentUser().getUid()).child("past-orders").child(orderNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String orderNumber = dataSnapshot.child("orderNumber").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String total = "E " + dataSnapshot.child("totalPrice").getValue().toString() +"0";

                        TextView nameText = (TextView) findViewById(R.id.orderDetailName);
                        TextView phoneText = (TextView) findViewById(R.id.orderDetailPhone);
                        TextView emailText = (TextView) findViewById(R.id.orderDetailEmail);
                        TextView numberText = (TextView) findViewById(R.id.orderDetailNumber);
                        TextView statusText = (TextView) findViewById(R.id.orderDetailStatus);
                        TextView totalText = (TextView) findViewById(R.id.orderDetailTotal);

                        nameText.setText(name);
                        phoneText.setText(phone);
                        emailText.setText(email);
                        numberText.setText(orderNumber);
                        statusText.setText(status);
                        totalText.setText(total);


                    }
                });


        mData.child("Users").child(mAuth.getCurrentUser().getUid()).child("past-orders").child(orderNumber).child("order-items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        List<ItemModel> itemModelList = new ArrayList<>();
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            String value = child.getValue().toString();
                            ItemModel itemModel = new ItemModel();
                            itemModel.setName(key);
                            itemModel.setQuantity(Integer.parseInt(value));
                            itemModelList.add(itemModel);
                        }
                        OrderDetailRecyclerViewAdapter orderDetailRecyclerViewAdapter = new OrderDetailRecyclerViewAdapter(context, itemModelList);
                        recyclerView = (RecyclerView) findViewById(R.id.orderDetailRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(orderDetailRecyclerViewAdapter);
                    }
                });

    }


}