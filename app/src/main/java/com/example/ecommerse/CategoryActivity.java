package com.example.ecommerse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecommerse.adapters.ProductAdapter;
import com.example.ecommerse.databinding.ActivityCategoryBinding;
import com.example.ecommerse.model.product;
import com.example.ecommerse.utils.constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    ProductAdapter productAdapter;
    ArrayList<product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        products =new ArrayList<>();
        productAdapter = new ProductAdapter(this,products);

        int catId = getIntent().getIntExtra("catId",0);
        String categortName = getIntent().getStringExtra("categoryName");
       getSupportActionBar().setTitle(categortName);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getProduct(catId);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
      finish();
        return super.onSupportNavigateUp();
    }

    void getProduct (int catId ){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = constants.GET_PRODUCTS_URL + "?category_id=8"+catId;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("success")){
                        JSONArray productArray = object.getJSONArray("products");
                        for(int i=0; i<productArray.length(); i++){
                            JSONObject childobj = productArray.getJSONObject(i);
                            product product = new product(
                                    childobj.getString("name"),
                                    constants.PRODUCTS_IMAGE_URL + childobj.getString("image"),
                                    childobj.getString("status"),
                                    childobj.getDouble("price"),
                                    childobj.getDouble("price_discount"),
                                    childobj.getInt("stock"),
                                    childobj.getInt("id")
                            );
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}