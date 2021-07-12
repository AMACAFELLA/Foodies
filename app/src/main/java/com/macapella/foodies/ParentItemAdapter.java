package com.macapella.foodies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParentItemAdapter
        extends RecyclerView
        .Adapter<ParentItemAdapter.ParentViewHolder> {

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private final RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();
    private final List<ParentItem> itemList;
    private final Context mContext;

    ParentItemAdapter(Context mContext, List<ParentItem> itemList)
    {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i)
    {

        // Here we inflate the corresponding
        // layout of the parent item
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.category_item,
                        viewGroup, false);

        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ParentViewHolder parentViewHolder,
            int position)
    {

        // Create an instance of the ParentItem
        // class for the given position
        ParentItem parentItem
                = itemList.get(position);

        // For the created instance,
        // get the title and set it
        // as the text for the TextView
        parentViewHolder
                .categoryName
                .setText(parentItem.getParentItemTitle());

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.

        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                parentViewHolder
                        .categoryRecyclerView
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method

        layoutManager
                .setInitialPrefetchItemCount(
                        parentItem
                                .getChildItemList()
                                .size());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        RecyclerViewAdapter childItemAdapter
                = new RecyclerViewAdapter(mContext,
                parentItem
                        .getChildItemList());
        parentViewHolder
                .categoryRecyclerView
                .setLayoutManager(layoutManager);
        parentViewHolder
                .categoryRecyclerView
                .setAdapter(childItemAdapter);
        parentViewHolder
                .categoryRecyclerView
                .setRecycledViewPool(viewPool);
    }

    // This method returns the number
    // of items we have added in the
    // ParentItemList i.e. the number
    // of instances we have created
    // of the ParentItemList
    @Override
    public int getItemCount()
    {

        return itemList.size();
    }

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView
    static class ParentViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView categoryName;
        private final RecyclerView categoryRecyclerView;

        ParentViewHolder(final View itemView)
        {
            super(itemView);

            categoryName
                    = itemView
                    .findViewById(
                            R.id.categoryName);
            categoryRecyclerView
                    = itemView
                    .findViewById(
                            R.id.categoryRecyclerView);
        }
    }
}

