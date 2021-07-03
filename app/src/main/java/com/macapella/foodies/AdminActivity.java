package com.macapella.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_user =  findViewById(R.id.edit_user);
        Button btn = findViewById(R.id.adminButton);
        DAOAdmin dao = new DAOAdmin();
        btn.setOnClickListener(v -> {
            User ad = new User(edit_name.getText().toString(),edit_user.getText().toString());
            dao.add(ad).addOnSuccessListener(suc ->
            {
                Toast.makeText(this,"Record is update", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er->
                    {
                        Toast.makeText(this, ""+er.getMessage(),Toast.LENGTH_SHORT).show();

                    });
        });
    }
}