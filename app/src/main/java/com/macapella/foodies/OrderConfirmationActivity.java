package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderConfirmationActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("active-order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                        String order = task.getResult().getValue().toString();
                        db.collection("active-orders").document(order)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        Map<String, Object> orderData = documentSnapshot.getData();
                                        String email = orderData.get("email").toString();
                                        String name = orderData.get("name").toString();
                                        String orderNumber = "Order #" + orderData.get("order-number").toString();
                                        String phone = orderData.get("phone").toString();
                                        float totalPriceFloat = Float.parseFloat(orderData.get("totalPrice").toString());
                                        String totalPrice = String.format("%.02f", totalPriceFloat);
                                        String address = orderData.get("latitude").toString() + ", " + orderData.get("longitude").toString();
                                        Map<String, Object> items = ((Map<String, Object>) orderData.get("order-items"));
                                        List<ItemModel> itemModelList = new ArrayList<ItemModel>();
                                        for (Map.Entry<String,Object> entry : items.entrySet()) {
                                            ItemModel itemModel = new ItemModel();
                                            itemModel.setName(entry.getKey());
                                            int quantity = Integer.parseInt(entry.getValue().toString());
                                            itemModel.setQuantity(quantity);
                                            itemModelList.add(itemModel);
                                        }

                                        TextView nameText = (TextView) findViewById(R.id.orderConfirmName);
                                        TextView emailText = (TextView) findViewById(R.id.orderConfirmEmail);
                                        TextView orderNumberText = (TextView) findViewById(R.id.orderConfirmNumber);
                                        TextView phoneText = (TextView) findViewById(R.id.orderConfirmPhone);
                                        TextView addressText = (TextView) findViewById(R.id.orderConfirmAddress);
                                        TextView priceText = (TextView) findViewById(R.id.orderConfirmTotal);

                                        nameText.setText(name);
                                        emailText.setText(email);
                                        orderNumberText.setText(orderNumber);
                                        phoneText.setText(phone);
                                        addressText.setText(address);
                                        priceText.setText(totalPrice);


                                        ConfirmationRecyclerViewAdapter confirmationRecyclerViewAdapter = new ConfirmationRecyclerViewAdapter(context, itemModelList);
                                        recyclerView = (RecyclerView) findViewById(R.id.confirmRecyclerView);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                        recyclerView.setAdapter(confirmationRecyclerViewAdapter);
                                    }
                                });
                    }
                });
    }

    public void switchCart(View view) {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void switchAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void switchHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}