package com.example.homemenu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemenu.R;
import com.example.homemenu.models.OrderItem;
import com.example.homemenu.models.UserOrder;

import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{
    private ArrayList<UserOrder> mUserOrder;
    private Activity mActivity;

    public OrderAdapter(ArrayList<UserOrder> mUserOrder, Activity mActivity) {
        this.mUserOrder = mUserOrder;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myorders,parent,false);
        return new OrderAdapter.OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        UserOrder userOrder = mUserOrder.get(position);
        holder.setRestaurantName(userOrder.getRestaurantName());
        holder.setStatus(userOrder.getStatusOrder());
        holder.setAddres(userOrder.getOrderAddress());
        holder.setAmount(userOrder.getOrderAmount());
        holder.setDate(String.valueOf(userOrder.getOrderDate()));
    }

    @Override
    public int getItemCount() {
        if(mUserOrder == null)
            return 0;

        return  mUserOrder.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        TextView orderRestaurantName;
        TextView orderStatus;
        TextView orderAddress;
        TextView orderAmount;
        TextView orderDate;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderRestaurantName = (TextView)itemView.findViewById(R.id.order_restaurant_name);
            orderStatus = (TextView)itemView.findViewById(R.id.order_status);
            orderAddress = (TextView)itemView.findViewById(R.id.order_address);
            orderAmount = (TextView)itemView.findViewById(R.id.order_amount);
            orderDate= (TextView)itemView.findViewById(R.id.order_date);
        }

        public void setRestaurantName(String name){
            orderRestaurantName.setText(name);
        }
        public void setStatus(String status){
            orderStatus.setText(status);
        }
        public void setAddres(String address){
            orderAddress.setText(address);
        }
        public void setAmount(String amount){
            orderAmount.setText(amount);
        }
        public void setDate(String date){
            orderDate.setText(date);
        }
    }

}
