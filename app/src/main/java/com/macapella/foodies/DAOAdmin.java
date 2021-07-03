package com.macapella.foodies;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DAOAdmin {
    private DatabaseReference databaseReference;
    public DAOAdmin(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }
    public Task<Void> add(User ad)
    {
       return databaseReference.push().setValue(ad);
    }
    public Task<Void> update(String key, HashMap<String, Object>hashMap)
    {
        return databaseReference.child(key).updateChildren(hashMap);
    }
}
