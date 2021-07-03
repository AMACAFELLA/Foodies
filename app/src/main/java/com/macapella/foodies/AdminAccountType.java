package com.macapella.foodies;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAccountType {
    private DatabaseReference reference;
    public AdminAccountType(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference("Users");
    }
    public Task<Void> add(User u)
    {
        return reference.push().setValue(u);
    }
}
