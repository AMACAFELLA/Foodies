package com.macapella.foodies;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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


public class HomeActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Map <String, String> cartList = new HashMap<>();
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context = this;

        //
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List <ItemModel> itemModelList = new ArrayList<>();
                            QuerySnapshot querySnapshot = task.getResult();
                            querySnapshot.forEach(documentSnapshot -> {ItemModel itemModel = new ItemModel(); itemModel.setName(documentSnapshot.getString("name")); itemModel.setPrice(Integer.parseInt(documentSnapshot.getString("price"))); itemModel.setQuantity(0); itemModel.setDescription(documentSnapshot.getString("description")); itemModel.setImg(documentSnapshot.getString("img")); itemModelList.add(itemModel);});
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, itemModelList);
                            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(recyclerViewAdapter);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
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

    public void addToCart(String name, Integer price, Integer quantity) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String itemName = name;
        Integer itemPrice = price;

        if (quantity > 0) {
            db.collection("users").document(uid).collection("cart").document(itemName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    if (documentSnapshot.get("quantity") == null) {

                                        Integer itemQuantity = quantity;

                                        ItemModel itemModel = new ItemModel();
                                        itemModel.setName(itemName);
                                        itemModel.setPrice(itemPrice);
                                        itemModel.setQuantity(itemQuantity);

                                        if (itemQuantity > 0) {
                                            cartList.put("name", itemModel.getName());
                                            cartList.put("price", Integer.toString(itemModel.getPrice()));
                                            cartList.put("quantity", Integer.toString(itemModel.getQuantity()));
                                        }

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

                                    } else if ((documentSnapshot.get("quantity").toString() != null)){
                                        db.collection("users").document(uid).collection("cart").document(itemName)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot doc = task.getResult();

                                                        Integer oldQuantity = Integer.parseInt(doc.getData().get("quantity").toString());



                                                        Integer itemQuantity = quantity + oldQuantity;

                                                        ItemModel itemModel = new ItemModel();
                                                        itemModel.setName(itemName);
                                                        itemModel.setPrice(itemPrice);
                                                        itemModel.setQuantity(itemQuantity);

                                                        if (itemQuantity > 0) {
                                                            cartList.put("name", itemModel.getName());
                                                            cartList.put("price", Integer.toString(itemModel.getPrice()));
                                                            cartList.put("quantity", Integer.toString(itemModel.getQuantity()));
                                                        }

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
                                                    }
                                                });
                                    }
                                } else {
                                    Integer itemQuantity = quantity;

                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setName(itemName);
                                    itemModel.setPrice(itemPrice);
                                    itemModel.setQuantity(itemQuantity);

                                    if (itemQuantity > 0) {
                                        cartList.put("name", itemModel.getName());
                                        cartList.put("price", Integer.toString(itemModel.getPrice()));
                                        cartList.put("quantity", Integer.toString(itemModel.getQuantity()));
                                    }

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
                                }
                            }
                        }
                    });
        }
    }
}