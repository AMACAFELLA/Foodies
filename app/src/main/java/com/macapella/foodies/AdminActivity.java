package com.macapella.foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        final EditText edit_name = findViewById(R.id.edit_name);
        final EditText edit_phone =  findViewById(R.id.edit_phone);
        final EditText edit_email = findViewById(R.id.edit_email);
        final EditText edit_customer =  findViewById(R.id.edit_customer);
        String a = edit_customer.getText().toString();
        Boolean ad = Boolean.valueOf(a);



        Button btn = findViewById(R.id.adminButton);
        Button btn_account = findViewById(R.id.adminData);
        btn_account.setOnClickListener(v ->
        {
            Intent intent = new Intent(AdminActivity.this, RVActivity.class);
            startActivity(intent);
        });
        DAOAdmin dao = new DAOAdmin();
        User u_edit = (User)getIntent().getSerializableExtra("EDIT");
        if(u_edit !=null)
        {
            btn.setText("UPDATE");
            edit_name.setText(u_edit.getFullname());
            edit_phone.setText(u_edit.getPhone());
            edit_email.setText(u_edit.getAccount());
            edit_customer.setText(u_edit.getAccount());
            btn_account.setVisibility(View.GONE);
        }
        else
        {
            btn.setText("SUBMIT");
            btn_account.setVisibility(View.VISIBLE);
        }
        btn.setOnClickListener(v -> {
            User u = new User(edit_name.getText().toString(),edit_phone.getText().toString(), edit_email.getText().toString(), edit_customer.getText().toString());
            if(u_edit == null) {
                dao.add(u).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "User was added", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();

                });
            }
            else
            {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("fullname", edit_name.getText().toString());
                hashMap.put("phone", edit_phone.getText().toString());
                hashMap.put("email", edit_email.getText().toString());

                dao.update(u_edit.getKey(),hashMap).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this,"User was updated", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er->
                {
                    Toast.makeText(this, ""+er.getMessage(),Toast.LENGTH_SHORT).show();

                });
            }
        });
    }
}