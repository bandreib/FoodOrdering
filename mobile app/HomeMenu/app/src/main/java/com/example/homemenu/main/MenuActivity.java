package com.example.homemenu.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.homemenu.Interface.ItemClickListener;
import com.example.homemenu.R;
import com.example.homemenu.adapters.HttpHandler;
import com.example.homemenu.adapters.MenuAdapter;
import com.example.homemenu.adapters.RestaurantAdapter;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.FinalOrder;
import com.example.homemenu.models.Meniu;
import com.example.homemenu.models.OrderItem;
import com.example.homemenu.models.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements ItemClickListener {
    TextView txt_rest_nume;
    private ArrayList<Meniu> meniuList;
    static private ArrayList<Restaurant> mRestaurantCollection;
    private static final String TAG = "MenuActivity";
    private RecyclerView mMenuRecyclerView;
    private MenuAdapter mAdapter;
    ElegantNumberButton elegantNumberButton;
    private String quantity;
    private FinalOrder finalOrder;
    private ArrayList<OrderItem> orderItems;
    private Double totalAmount = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        meniuList = new ArrayList<>();
        Intent intent = getIntent();
        int value = intent.getIntExtra("position",0);
        meniuList = Common.currentRestaurant.get(value).getMeniuList();
        //Log.d(TAG, "onClick: clicked."+value);
        //Toast.makeText(this, ""+meniuList.size(), Toast.LENGTH_SHORT).show();
        init();



        finalOrder.setOrderItems(orderItems);
        finalOrder.setAddressCustomer(Common.currentUser.getAddress());
        Log.d(TAG, "onCreate: "+orderItems.size());




        //finalOrder.setTotalAmount(String.valueOf(totalAmount));
        Common.currentFinalOrder = finalOrder;
        Common.currentTotalAmount = String.valueOf(totalAmount);






    }

    private void init(){
        mMenuRecyclerView = (RecyclerView)findViewById(R.id.recycler_item);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMenuRecyclerView.setHasFixedSize(true);
        mAdapter = new MenuAdapter(meniuList,this,this);
        mMenuRecyclerView.setAdapter(mAdapter);
        orderItems = new ArrayList<>();
        finalOrder = new FinalOrder();



    }


    @Override
    public void onClick(int position) {
        Common.currentMeniu = meniuList.get(position);
        elegantNumberButton = findViewById(R.id.number_button);
        quantity = "1";
        elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = elegantNumberButton.getNumber();
                Log.d(TAG, "onClick: "+quantity);
            }
        });
        double value = Double.parseDouble(Common.currentMeniu.getPrice());
        int multiply = Integer.parseInt(quantity);
        totalAmount += multiply*value;
        OrderItem orderItem = new OrderItem();
        orderItem.setFood(Common.currentMeniu.getFood());
        orderItem.setQuantity(quantity);
        orderItem.setPrice(Common.currentMeniu.getPrice());


        Toast.makeText(this, "Add "+Common.currentMeniu.getFood()+" to cart", Toast.LENGTH_SHORT).show();
            quantity="1";
            elegantNumberButton.setNumber("1");
            orderItems.add(orderItem);
        //Log.d(TAG, "Total amount: "+totalAmount);



}
}
