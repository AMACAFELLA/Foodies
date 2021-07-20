package com.macapella.foodies;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOAdmin {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private DatabaseReference databaseReference;
    public DAOAdmin(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        //FirebaseDatabase db = FirebaseDatabase.getInstance();
        //databaseReference = db.getReference(User.class.getSimpleName());
    }
    //Adding user to the firebase data using the reference path so accounts add are in the same instance.
    public Task<Void> add(User u)
    {
       return reference.push().setValue(u);
    }
    //Updates the data of the user for that particular reference in firebase
    public Task<Void> update(String userID, HashMap<String, Object>hashMap)
    {
        return reference.child(userID).updateChildren(hashMap);
    }
    //Deletes the user reference and all the data from firebase
    public Task<Void> delete(String userID)
    {
       return reference.child(userID).removeValue();
    }
    public Query get(String userID)
    {
        //Admin would scroll down to see the first 8 and load data from firebase to retrieve the next 8 data.
        if(userID == null)
        {
            return reference.orderByKey().limitToFirst(8);
        }
        return reference.orderByKey().startAfter(userID).limitToFirst(8);
    }
}
