package com.example.homemenu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homemenu.Interface.ItemClickListener;
import com.example.homemenu.R;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.Meniu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder>{
    private ArrayList<Meniu> mMenu;
    private Activity mActivity;
    private ItemClickListener itemClickListener;


    public MenuAdapter(ArrayList<Meniu> mMenu, Activity mActivity,ItemClickListener itemClickListener) {
        this.mMenu = mMenu;
        this.mActivity = mActivity;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu,parent,false);
        return new MenuHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        Meniu meniu = mMenu.get(position);
        Common.currentMeniu = meniu;

        holder.setFood(meniu.getFood());
        holder.setDescription(meniu.getDescription());
        holder.setPrice(meniu.getPrice());
        Glide.with(mActivity)
                .load("https://img.techpowerup.org/200429/214-2148603-you-eat-ready-to-eat-food-icon.jpg")
                .into(holder.menuImageView);


    }

    @Override
    public int getItemCount() {
        if(mMenu == null)
            return 0;

        return  mMenu.size();
    }


    public class MenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView menuFoodTextView;
        TextView menuDescriptionTextView;
        TextView menuPriceTextView;
        ImageView menuImageView;
        ItemClickListener itemClickListener;

        public MenuHolder( View itemView,ItemClickListener itemClickListener) {
            super(itemView);
            menuFoodTextView = (TextView) itemView.findViewById(R.id.textview_menu_food);
            menuDescriptionTextView = (TextView) itemView.findViewById(R.id.textview_menu_description);
            menuPriceTextView = (TextView) itemView.findViewById(R.id.textview_menu_price);
            menuImageView = (ImageView) itemView.findViewById(R.id.imageview_menu);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        public void setFood(String food) {
            menuFoodTextView.setText(food);
        }

        public void setDescription(String description) {
            menuDescriptionTextView.setText(description);
        }

        public void setPrice(String price) {
            menuPriceTextView.setText(price+" RON");
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(getAdapterPosition());

        }
    }
}
