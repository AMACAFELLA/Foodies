package com.macapella.foodies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RVActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RVAdapter adapter;
    DAOAdmin dao;
    boolean isLoading = false;
    String userID = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Assigning to resources
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvactivity);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAOAdmin();
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading = true;
                        loadData();

                    }
                }
            }
        });
    }

    private void loadData() {
        //When user swipes to refresh the page more data is added
        swipeRefreshLayout.setRefreshing(true);
        dao.get(userID).addValueEventListener(new ValueEventListener() {
            @Override
            //Load the data from users
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    User u = data.getValue(User.class);
                    u.setKey(data.getKey());
                    users.add(u);
                    userID = data.getKey();
                }
                adapter.setItems(users);
                adapter.notifyDataSetChanged();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}