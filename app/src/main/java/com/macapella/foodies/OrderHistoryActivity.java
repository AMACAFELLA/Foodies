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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    String uid;
    String uName;
    String uEmail;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
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
                            List<ItemModel> itemModelList = new ArrayList<ItemModel>();
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
}