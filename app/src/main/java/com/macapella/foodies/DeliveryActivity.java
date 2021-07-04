package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("active-orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.size() > 0) {
                            List<OrderModel> orderModelList = new ArrayList<>();
                            querySnapshot.forEach( documentSnapshot -> {
                                OrderModel orderModel = new OrderModel();
                                orderModel.setName(documentSnapshot.get("name").toString());
                                orderModel.setPhone(documentSnapshot.get("phone").toString());
                                orderModel.setOrderNumber(documentSnapshot.get("order-number").toString());
                                orderModel.setStatus(documentSnapshot.get("status").toString());
                                orderModel.setLatitude(documentSnapshot.get("latitude").toString());
                                orderModel.setLongitude(documentSnapshot.get("longitude").toString());
                                orderModelList.add(orderModel);

                                DeliveryRecyclerViewAdapter deliveryRecyclerViewAdapter = new DeliveryRecyclerViewAdapter(context, orderModelList);
                                recyclerView = (RecyclerView) findViewById(R.id.deliveryRecyclerView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(deliveryRecyclerViewAdapter);

                            });
                        }
                    }
                });

    }

    public void goToAddress(String latitude, String longitude, Context cont) {

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        cont.startActivity(mapIntent);
    }

    public void confirmDelivery(String number) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> statusStart = new HashMap<String, Object>();
        statusStart.put("status", "Completed");
        db.collection("active-orders").document(number)
                .update(statusStart);

        db.collection("active-orders").document(number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> docData = documentSnapshot.getData();
                        String user = docData.get("user").toString();
                        Map<String, Object> items = ((Map<String, Object>) docData.get("order-items"));

                        Map<String, Object> orderData = new HashMap<>();
                        orderData.put("name", docData.get("name").toString());
                        orderData.put("email", docData.get("email").toString());
                        orderData.put("phone", docData.get("phone").toString());
                        orderData.put("totalPrice", docData.get("totalPrice").toString());
                        orderData.put("orderNumber", docData.get("order-number").toString());
                        orderData.put("status", docData.get("status").toString());
                        orderData.put("order-items", items);

                        mDatabase.child("Users").child(user).child("past-orders").child(number)
                                .updateChildren(orderData);

                        db.collection("active-orders").document(number)
                                .delete();
                    }
                });
    }

    public void startDelivery(String number) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> statusStart = new HashMap<String, Object>();
        statusStart.put("status", "Sent out for delivery");
        db.collection("active-orders").document(number)
                .update(statusStart);
    }

    public void unableToDeliver(String number) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> statusStart = new HashMap<String, Object>();
        statusStart.put("status", "Delivery attempt made: Unable to deliver");
        db.collection("active-orders").document(number)
                .update(statusStart);
    }

}