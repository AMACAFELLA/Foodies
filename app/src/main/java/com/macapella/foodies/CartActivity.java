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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    String uid;
    String uName;
    String uEmail;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Context context = this;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List <ItemModel> itemModelList = new ArrayList<ItemModel>();
                            QuerySnapshot querySnapshot = task.getResult();
                            querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setPrice(Integer.parseInt(documentSnapshot.getString("price"))); itemModel.setQuantity(Integer.parseInt(documentSnapshot.getString("quantity"))); itemModelList.add(itemModel);});
                            CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter(context, itemModelList);
                            recyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(cartRecyclerViewAdapter);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void switchHome(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void switchAccount(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void checkoutNow (View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void changeQuantity(int position, int quantity) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List <ItemModel> itemModelList = new ArrayList<ItemModel>();
                            QuerySnapshot querySnapshot = task.getResult();
                            querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setPrice(Integer.parseInt(documentSnapshot.getString("price"))); itemModel.setQuantity(Integer.parseInt(documentSnapshot.getString("quantity"))); itemModelList.add(itemModel);});
                            ItemModel itemModel = new ItemModel();
                            itemModel = itemModelList.get(position);
                            itemModel.setQuantity(quantity);
                            itemModelList.add(itemModel);
                            itemModelList.forEach( item -> {

                                String itemName = item.getName();
                                Map <String, String> cartList = new HashMap<>();
                                cartList.put("name", item.getName());
                                cartList.put("price", Integer.toString(item.getPrice()));
                                cartList.put("quantity", Integer.toString(item.getQuantity()));

                                db.collection("users").document(uid).collection("cart").document(itemName)
                                        .set(cartList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("Success", "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Error", "Error writing document", e);
                                            }
                                        });
                            });

                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    public void removeItem(int position) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List <ItemModel> itemModelList = new ArrayList<ItemModel>();
                            QuerySnapshot querySnapshot = task.getResult();
                            querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setPrice(Integer.parseInt(documentSnapshot.getString("price"))); itemModel.setQuantity(Integer.parseInt(documentSnapshot.getString("quantity"))); itemModelList.add(itemModel);});
                            String toRemove = itemModelList.get(position).getName();

                            db.collection("users").document(uid).collection("cart").document(toRemove).delete();

                            itemModelList.remove(position);

                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}