package com.example.ecommerse.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerse.R;
import com.example.ecommerse.databinding.ItemCardBinding;
import com.example.ecommerse.databinding.QuantityBinding;
import com.example.ecommerse.model.product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

   Context context;
   ArrayList<product> products;

   CartListner cartListner;
    Cart cart;
   public interface CartListner{
       public void onQuantitychanged();


   }

   public  CartAdapter(Context context, ArrayList<product> products,CartListner cartListner){
       this.context = context;
       this.products=products;
       this.cartListner=cartListner;
        cart = TinyCartHelper.getCart();
   }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
      product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.imagect);
        holder.binding.namec.setText(product.getName());
        holder.binding.pricecart.setText("₹"+product.getPrice());
        holder.binding.quantity.setText(product.getQuantity()+ "item(s)");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuantityBinding    quantityBinding= QuantityBinding.inflate(LayoutInflater.from(context));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(quantityBinding.getRoot())
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

              quantityBinding.productName.setText(product.getName());
             quantityBinding.productStock.setText("Stock"+product.getStock());
             quantityBinding.quantity.setText(String.valueOf(product.getQuantity()));

             int Stock = product.getStock();

              quantityBinding.plusBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      int quantity = product.getQuantity();
                      quantity++;

                      if(quantity>product.getStock()){
                          Toast.makeText(context, "Max stock available: "+ product.getStock(), Toast.LENGTH_SHORT).show();
                          return;
                      }
                      else {
                          product.setQuantity(quantity);
                          quantityBinding.quantity.setText(String.valueOf(quantity));
                      }
                      notifyDataSetChanged();
                      cart.updateItem(product,product.getQuantity());
                      cartListner.onQuantitychanged();
                  }
              });

                quantityBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = product.getQuantity();
                        if(quantity>1)
                        quantity--;
                        product.setQuantity(quantity);
                        quantityBinding.quantity.setText(String.valueOf(quantity));

                        notifyDataSetChanged();
                        cart.updateItem(product,product.getQuantity());
                        cartListner.onQuantitychanged();
                    }
                });
                quantityBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    dialog.dismiss();
//                    notifyDataSetChanged();
//                    cart.updateItem(product,product.getQuantity());
//                    cartListner.onQuantitychanged();
                    }
                });

                dialog.show();
            }
        });
//
   }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
       ItemCardBinding binding;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCardBinding.bind(itemView);
        }
    }
}
