package com.example.ecommerse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecommerse.databinding.ActivityPaymentactivitiesBinding;
import com.example.ecommerse.utils.constants;

public class Paymentactivities extends AppCompatActivity {

    ActivityPaymentactivitiesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPaymentactivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String ordercode = getIntent().getStringExtra("orderCode");
           binding.webview.setMixedContentAllowed(true);
           binding.webview.loadUrl(constants.PAYMENT_URL + ordercode);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
      finish();
        return super.onSupportNavigateUp();
    }
}