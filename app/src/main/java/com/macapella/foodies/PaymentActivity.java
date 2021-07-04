package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class PaymentActivity extends AppCompatActivity {

    Context context = this;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    public void onConfirm(View view)  {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String orderName = documentSnapshot.get("tempName").toString();
                        String orderEmail = documentSnapshot.get("tempEmail").toString();
                        String tempPhone = documentSnapshot.get("tempPhone").toString();
                        String latitude = documentSnapshot.get("latitude").toString();
                        String longitude = documentSnapshot.get("longitude").toString();
                        String totalPrice = documentSnapshot.get("totalPrice").toString();


                        db.collection("order-count").document("order-count")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                        int orderCount = Integer.parseInt(documentSnapshot1.get("order-count").toString());
                                        orderCount += 1;
                                        String orderString = "Order " + Integer.toString(orderCount);

                                        Map<String, Object> order = new HashMap<>();
                                        order.put("order-number", orderString);
                                        order.put("name", orderName);
                                        order.put("email", orderEmail);
                                        order.put("phone", tempPhone);
                                        order.put("latitude", latitude);
                                        order.put("longitude", longitude);
                                        order.put("status", "Pending");
                                        order.put("totalPrice", totalPrice);
                                        order.put("user", mAuth.getCurrentUser().getUid());
                                        Map<String, Object> orderItems = new HashMap<>();

                                        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("cart")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                        List<ItemModel> itemModelList = new ArrayList<ItemModel>();
                                                        QuerySnapshot querySnapshot = task.getResult();
                                                        querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setQuantity(Integer.parseInt(documentSnapshot.getString("quantity"))); itemModelList.add(itemModel);});
                                                        itemModelList.forEach( item -> {


                                                            orderItems.put(item.getName(), item.getQuantity());
                                                            order.put("order-items", orderItems);

                                                            db.collection("active-orders").document(orderString)
                                                                    .set(order);

                                                        });
                                                    }
                                                });
                                        Map<String, Object> orderNumber = new HashMap<>();
                                        orderNumber.put("order-count", orderCount);
                                        db.collection("order-count").document("order-count")
                                                .update(orderNumber);

                                        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("cart")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                                                        queryDocumentSnapshots.forEach(doc -> {
                                                            doc.getReference().delete();
                                                        });
                                                    }
                                                });
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("active-order")
                                                .setValue(orderString);

                                        Intent intent = new Intent(context, OrderConfirmationActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                });

    }
}