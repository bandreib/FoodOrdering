package com.example.homemenu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemenu.Interface.ItemClickListener;
import com.example.homemenu.R;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.Restaurant;
import com.example.homemenu.models.User;

import java.util.ArrayList;



public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantHolder> {

    private ArrayList<Restaurant> mData;
    private Activity mACtivity;
    private ItemClickListener itemClickListener;
 //   private SelectedRestaurant selectedRestaurant;
    //private ItemClickListener itemClickListener;


    public RestaurantAdapter(ArrayList<Restaurant> data, Activity activity, ItemClickListener itemClickListener) {
        this.mData = data;
        this.mACtivity = activity;
        this.itemClickListener = itemClickListener;

    }

    @Override
    public RestaurantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant, parent, false);
        return new RestaurantHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(RestaurantHolder holder, int position) {
        Restaurant restaurant = mData.get(position);
        User user = Common.currentUser;
        holder.setName(restaurant.getName());
        holder.setAddress(restaurant.getAddress());
       // holder.setRestaurantImageView(restaurant.getImageURL());
        //holder.userNameTextView.setText(user.getName());

        Glide.with(mACtivity)
                .load(restaurant.getImageURL())
                .into(holder.restaurantImageView);
        //Picasso.with(mACtivity.getBaseContext()).load(restaurant.getImageURL()).into(holder.restaurantImageView);
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }





    /* public interface SelectedRestaurant{

        void selectedRestaurant(Restaurant restaurant);

    }*/

    public class RestaurantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView restaurantImageView;
        TextView restaurantNameTextView;
        TextView restaurantAddressTextView;
       // TextView userNameTextView;
        ItemClickListener itemClickListener;

        public RestaurantHolder(View itemView,ItemClickListener itemClickListener) {
            super(itemView);

            restaurantImageView = (ImageView) itemView.findViewById(R.id.imageview_restaurant);
            restaurantNameTextView = (TextView) itemView.findViewById(R.id.textview_restaurant_name);
            restaurantAddressTextView = (TextView) itemView.findViewById(R.id.restaurant_address_textview);
            this.itemClickListener = itemClickListener;
           // userNameTextView = (TextView) itemView.findViewById(R.id.textview_user_name);

            itemView.setOnClickListener(this);

        }

        public void setName(String name) {
            restaurantNameTextView.setText(name);
        }

        public void setAddress(String address) {
            restaurantAddressTextView.setText(address);
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(getAdapterPosition());

        }
    }
}
