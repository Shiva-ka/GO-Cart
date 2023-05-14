package com.example.ecommerse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ecommerse.databinding.ActivityProductDetailBinding;
import com.example.ecommerse.model.product;
import com.example.ecommerse.utils.constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetail extends AppCompatActivity {
ActivityProductDetailBinding binding;
product currproduct ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      String   name = getIntent().getStringExtra("name");
        String   image = getIntent().getStringExtra("image");
        int    id = getIntent().getIntExtra("id",0);
        double   price = getIntent().getDoubleExtra("price",0);

        Glide.with(this)
                .load(image)
                .into(binding.productimage);
// call api of product details id wise
        getproductDetails(id);

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cart cart = TinyCartHelper.getCart();
        binding.ADDTOCARDBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              cart.addItem(currproduct,1);
              binding.ADDTOCARDBTN.setEnabled(false);
             binding.ADDTOCARDBTN.setText("Added in cart ");
//                Toast.makeText(ProductDetail.this, "Product added to cart ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.cart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     if(item.getItemId() == R.id.cart){
         startActivity(new Intent(this,cardActivity.class));
     }
        return super.onOptionsItemSelected(item);
    }

    void  getproductDetails (int id ){
          RequestQueue queue = Volley.newRequestQueue(this);
          String url = constants.GET_PRODUCT_DETAILS_URL + id;
          StringRequest   request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                  try {
                      JSONObject object = new JSONObject(response);
                      if(object.getString("status").equals("success")){
                          JSONObject product =object.getJSONObject("product");
                       String   discription = product.getString("description");
                       binding.productdecription.setText(Html.fromHtml(discription)

                       );
//                        taking product details
                       currproduct = new product(

                                       product.getString("name"),
                                       constants.PRODUCTS_IMAGE_URL + product.getString("image"),
                                       product.getString("status"),
                                       product.getDouble("price"),
                                       product.getDouble("price_discount"),
                                       product.getInt("stock"),
                                       product.getInt("id")
                               );

                      }
                  } catch (JSONException e) {
//                      throw new RuntimeException(e);
                    e.printStackTrace();
                  }
              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

              }
          });
          queue.add(request);
    }

// when click on back button  then activity is finished
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}