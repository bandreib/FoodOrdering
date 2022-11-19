package com.example.homemenu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemenu.R;
import com.example.homemenu.models.Meniu;
import com.example.homemenu.models.OrderItem;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    private ArrayList<OrderItem> mOrderItem;
    private Activity mActivity;

    public CartAdapter(ArrayList<OrderItem> mOrderItem, Activity mActivity) {
        this.mOrderItem = mOrderItem;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart,parent,false);
        return new CartAdapter.CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        OrderItem orderItem = mOrderItem.get(position);
        holder.setFood(orderItem.getFood());
        holder.setPrice(orderItem.getPrice());
        holder.setQuantity(orderItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        if(mOrderItem == null)
            return 0;

        return  mOrderItem.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder{
        TextView cartFoodTextView;
        TextView cartQuantityTextView;
        TextView cartPriceTextView;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            cartFoodTextView = (TextView)itemView.findViewById(R.id.cart_item_name);
            cartQuantityTextView = (TextView)itemView.findViewById(R.id.cart_item_quantity);
            cartPriceTextView = (TextView)itemView.findViewById(R.id.cart_item_price);
        }

        public void setFood(String food){
            cartFoodTextView.setText(food);
        }

        public void setQuantity(String quantity){
            cartQuantityTextView.setText(quantity);
        }

        public void setPrice(String price){cartPriceTextView.setText(price);}
    }

}
