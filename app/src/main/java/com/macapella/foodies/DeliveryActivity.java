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