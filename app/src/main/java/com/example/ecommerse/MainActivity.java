package com.example.ecommerse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecommerse.adapters.CategoryAdapter;
import com.example.ecommerse.adapters.ProductAdapter;
import com.example.ecommerse.databinding.ActivityMainBinding;
import com.example.ecommerse.model.Category;
import com.example.ecommerse.model.product;
import com.example.ecommerse.utils.constants;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

   ProductAdapter productAdapter;
   ArrayList<product> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);

                intent.putExtra("query",text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        initCategories();
        initProducts();
        initslider();

    }



    public void initslider(){
//        binding.carousel.addData(new CarouselItem("data:image/jpeg;base64,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","some caption here "));
//   binding.carousel.addData(new CarouselItem("https://t4.ftcdn.net/jpg/02/61/01/87/360_F_261018762_f15Hmze7A0oL58Uwe7SrDKNS4fZIjLiF.jpg","some description"));
//  binding.carousel.addData(new CarouselItem("https://m.media-amazon.com/images/G/31/camera/seshnai/BAU/Dec/Camera/CLP/Topoffers/Tiles_440x540_1._CB617861308_.jpg","some description"));
//  binding.carousel.addData(new CarouselItem("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyaB1j5-7XIvXz_BxuuzljTTTII_PvEl96Hw&usqp=CAU","some description"));

        getRecentsoffers();
    }

    void initCategories(){

        categories = new ArrayList<>();
//     sample data
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#18ab4e","some description ",1));
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#fb0504","some description ",1));
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#4186ff","some description ",1));
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#BF360C","some description ",1));
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#ff870e","some description ",1));
//        categories.add(new Category("sports & outdoors ","https://tutorials.mianasad.com/ecommerce/uploads/category/1651894605161.png","#ff6f52","some description ",1));

        categoryAdapter = new CategoryAdapter(this,categories);

        getcategories();

        GridLayoutManager  layoutManager = new GridLayoutManager(this,4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);

    }


    void getcategories(){
        RequestQueue queue = Volley.newRequestQueue(this);
         StringRequest request = new StringRequest(Request.Method.GET, constants.GET_CATEGORIES_URL, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

//                 Log.e("err",response);
                 try {
                     Log.e("err",response);

                     JSONObject Mainobj = new JSONObject(response);
                  if(Mainobj.getString("status").equals("success"))  {
                      JSONArray  categoriesArray = Mainobj.getJSONArray("categories");
                      for(int i=0; i<categoriesArray.length();i++){
                          JSONObject object = categoriesArray.getJSONObject(i);
                          Category category = new Category(
                            object.getString("name"),
                            constants.CATEGORIES_IMAGE_URL +object.getString("icon"),
                                  object.getString("color"),
                                  object.getString("brief"),
                                  object.getInt("id")

                          );
                          categories.add(category);
                      }
                      categoryAdapter.notifyDataSetChanged();
                  }
                  else{

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

    void getRecentProduct (){
       RequestQueue  queue = Volley.newRequestQueue(this);
      String url = constants.GET_PRODUCTS_URL + "?count=8";
       StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

           @Override
           public void onResponse(String response) {
               try {
                   JSONObject object = new JSONObject(response);
                   if(object.getString("status").equals("success")){
                       JSONArray productArray = object.getJSONArray("products");
                       for(int i=0; i<productArray.length(); i++){
                           JSONObject childobj = productArray.getJSONObject(i);
                         product  product = new product(
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

    void getRecentsoffers (){
        RequestQueue queue =Volley.newRequestQueue(this);
//         in new android studio we use lamda expression
        StringRequest request = new StringRequest(Request.Method.GET,constants.GET_OFFERS_URL,response -> {


            try {
                JSONObject    object = new JSONObject(response);
                if(object.getString("status").equals("success")){
                    JSONArray offerArray = object.getJSONArray("news_infos");
                    for(int i=0; i< offerArray.length(); i++){
                        JSONObject childobj = offerArray.getJSONObject(i);
                     binding.carousel.addData(new CarouselItem(
                         constants.NEWS_IMAGE_URL   +  childobj.getString("image"),
                    childobj.getString("title") )
                     );
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {});
        queue.add(request);
    }

    void initProducts (){
        products =new ArrayList<>();
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));
//        products.add(new product("korean  loose short cowboy outwear",
//                "https://tutorials.mianasad.com/ecommerce/uploads/product/1650597798314.jpg"
//                ,"",12,12,1,1));



        getRecentProduct();
        productAdapter = new ProductAdapter(this,products);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.productlist.setLayoutManager(layoutManager);
        binding.productlist.setAdapter(productAdapter);

    }
}