package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Is responsible for providing the user access to Admin information and actions
 * This activity is only seen if an account has admin privileges
 * Provides a list of the active orders, where those order can be clicked on for further details and actions
 * Contains a button that takes the user to an Account management tool
 */

public class AdminActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        /*
        Firebase methods:
        Retrieves the information of all active orders in Firestore
        Passes information to the recyclerview adapter
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active-orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<OrderModel> orderModelList = new ArrayList<>();
                        querySnapshot.forEach(documentSnapshot -> {
                            OrderModel orderModel = new OrderModel();
                            orderModel.setOrderNumber(documentSnapshot.getString("order-number"));
                            orderModel.setName(documentSnapshot.getString("name"));
                            orderModel.setStatus(documentSnapshot.getString("status"));
                            orderModel.setTotalToPay(documentSnapshot.getString("totalPrice"));
                            orderModelList.add(orderModel);
                        });
                        AdminOrdersRecyclerViewAdapter adminOrdersRecyclerViewAdapter = new AdminOrdersRecyclerViewAdapter(context, orderModelList);
                        recyclerView = (RecyclerView) findViewById(R.id.adminOrdersRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adminOrdersRecyclerViewAdapter);
                    }
                });

        //button to redirect to the RVActivity
        Button btn = findViewById(R.id.adminButton);
        Button btn_account = findViewById(R.id.adminData);
        btn_account.setOnClickListener(v ->
        {
            Intent intent = new Intent(AdminActivity.this, RVActivity.class);
            startActivity(intent);
        });

    }
}