package com.example.ecommerse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerse.adapters.CartAdapter;
import com.example.ecommerse.databinding.ActivityCardBinding;
import com.example.ecommerse.model.product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class cardActivity extends AppCompatActivity {
ActivityCardBinding binding;
    CartAdapter adapter;
   ArrayList<product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
// now we use cart function to get product name and price in cart  means product add to add to cart acticity
//         for receiving recents product
        Cart cart = TinyCartHelper.getCart();

        for(Map.Entry<Item,Integer> item  : cart.getAllItemsWithQty().entrySet()) {
            product product = (product) item.getKey();
          int  quantity = item.getValue();
          product.setQuantity(quantity);

          products.add(product);
        }

//         adding sample data
//        products.add(new product("product 1 ","---","123",45,45,45,1));
//        products.add(new product("product 2 ","---","123",45,45,45,1));
//        products.add(new product("product 3 ","---","123",45,45,45,1));
        adapter = new CartAdapter(this, products, new CartAdapter.CartListner() {
            @Override
            public void onQuantitychanged() {
                binding.subtotal.setText(String.valueOf(String.format("₹%2f",cart.getTotalPrice())));
            }
        });

       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//     to  put  divide line between products
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.Cartlist.setLayoutManager(layoutManager);
        binding.Cartlist.addItemDecoration(itemDecoration);
        binding.Cartlist.setAdapter(adapter);

//         total price of all product in a cart
        binding.subtotal.setText(String.valueOf(String.format("₹%2f",cart.getTotalPrice())));
       binding.Continue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
             startActivity(new Intent(cardActivity.this,CheckoutActivity.class));
           }
       });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
      finish();
        return super.onSupportNavigateUp();
    }
}