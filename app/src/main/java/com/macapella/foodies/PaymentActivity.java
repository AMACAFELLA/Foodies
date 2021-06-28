package com.macapella.foodies;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        MomoPayment momoPayment = new MomoPayment();
        MomoPayment.mainFun();
    }
}