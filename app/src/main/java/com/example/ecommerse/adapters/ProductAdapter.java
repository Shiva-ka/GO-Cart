package com.example.ecommerse.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerse.ProductDetail;
import com.example.ecommerse.R;
import com.example.ecommerse.databinding.ItemProductBinding;
import com.example.ecommerse.model.product;

import java.util.ArrayList;

public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ProductViewholder>{

    Context context;
    ArrayList<product> products;


    public ProductAdapter(Context context , ArrayList<product>  products){
        this.context=context;
        this.products=products;
    }

    @NonNull
    @Override
    public ProductViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewholder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewholder holder, int position) {

        product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.images);
        holder.binding.labels.setText(product.getName());
        holder.binding.price.setText("₹ " + product.getPrice());

//          for product details
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, ProductDetail.class);
       intent.putExtra("name",product.getName());
       intent.putExtra("image",product.getImage());
       intent.putExtra("id",product.getId());
        intent.putExtra("price",product.getPrice());
        context.startActivity(intent);

    }
});


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class  ProductViewholder extends RecyclerView.ViewHolder{

      ItemProductBinding  binding;

        public ProductViewholder(@NonNull View itemView) {
            super(itemView);
          binding =ItemProductBinding.bind(itemView);
        }
    }
}
