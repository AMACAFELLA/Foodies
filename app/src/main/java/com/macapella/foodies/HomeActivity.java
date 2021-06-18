package com.macapella.foodies;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public List <ItemModel> itemModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //For Testing Only
        ItemModel itemModel = new ItemModel();
        itemModel.setName("Burger");
        itemModel.setPrice("E 65");
        itemModel.setDescription("Meat on bun.");
        itemModel.setImg("https://www.thespruceeats.com/thmb/l4w6PvMqsz1EjueCAh_foPmYafM=/3456x3456/smart/filters:no_upscale()/garlic-burger-patties-333503-hero-01-e4df660ff27b4e5194fdff6d703a4f83.jpg");
        itemModelList.add(itemModel);
        //

        //For Testing Only
        ItemModel itemModel1 = new ItemModel();
        itemModel1.setName("Pasta");
        itemModel1.setPrice("E 65");
        itemModel1.setDescription("Thin buns.");
        itemModel1.setImg("https://hips.hearstapps.com/delish/assets/17/36/1504715566-delish-fettuccine-alfredo.jpg");
        itemModelList.add(itemModel1);
        //

        //For Testing Only
        ItemModel itemModel2 = new ItemModel();
        itemModel2.setName("Pizza");
        itemModel2.setPrice("E 65");
        itemModel2.setDescription("Meat on thin bun.");
        itemModel2.setImg("https://www.vegrecipesofindia.com/wp-content/uploads/2020/11/pizza-recipe.jpg");
        itemModelList.add(itemModel2);
        //

        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, itemModelList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}