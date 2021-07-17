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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        Intent intent = getIntent();
        String orderNumber = intent.getStringExtra("orderNumber");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active-orders").document(orderNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                        String email = documentSnapshot.getString("email");
                        String name = documentSnapshot.getString("name");
                        String orderNumber = documentSnapshot.getString("order-number");
                        String phone = documentSnapshot.getString("phone");
                        String status = documentSnapshot.getString("status");
                        String totalPrice = "E " + documentSnapshot.getString("totalPrice") +"0";

                        TextView emailText = (TextView) findViewById(R.id.adminOrderDetailEmail);
                        TextView nameText = (TextView) findViewById(R.id.adminOrderDetailName);
                        TextView orderNumberText = (TextView) findViewById(R.id.adminOrderDetailNumber);
                        TextView phoneText = (TextView) findViewById(R.id.adminOrderDetailPhone);
                        TextView statusText = (TextView) findViewById(R.id.adminOrderDetailStatus);
                        TextView totalPriceText = (TextView) findViewById(R.id.adminOrderDetailTotal);

                        emailText.setText(email);
                        nameText.setText(name);
                        orderNumberText.setText(orderNumber);
                        phoneText.setText(phone);
                        statusText.setText(status);
                        totalPriceText.setText(totalPrice);

                        Map<String, Object> docData = documentSnapshot.getData();
                        Map<String, Object> items = ((Map<String, Object>) docData.get("order-items"));
                        List<ItemModel> itemModelList = new ArrayList<>();

                        for (Map.Entry<String, Object> entry : items.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            ItemModel itemModel = new ItemModel();
                            itemModel.setName(key);
                            itemModel.setQuantity(Integer.parseInt(value.toString()));
                            itemModelList.add(itemModel);
                        }

                        AdminOrderDetailsRecyclerViewAdapter adminOrderDetailsRecyclerViewAdapter = new AdminOrderDetailsRecyclerViewAdapter(context, itemModelList);
                        recyclerView = (RecyclerView) findViewById(R.id.adminOrderDetailRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adminOrderDetailsRecyclerViewAdapter);
                    }
                });
    }

    public void cancelOrder(View view) {
        Intent intent = getIntent();
        String orderNumber = intent.getStringExtra("orderNumber");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> statusStart = new HashMap<String, Object>();
        statusStart.put("status", "Canceled");
        db.collection("active-orders").document(orderNumber)
                .update(statusStart);

        db.collection("active-orders").document(orderNumber)
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

                        mDatabase.child("Users").child(user).child("past-orders").child(orderNumber)
                                .updateChildren(orderData);

                        db.collection("active-orders").document(orderNumber)
                                .delete();
                    }
                });

        Intent newIntent = new Intent(context, AdminActivity.class);
        startActivity(newIntent);
    }
}