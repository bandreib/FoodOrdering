package com.example.homemenu.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.homemenu.R;
import com.example.homemenu.adapters.HttpHandler;
import com.example.homemenu.adapters.OrderAdapter;
import com.example.homemenu.adapters.RestaurantAdapter;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.Meniu;
import com.example.homemenu.models.Restaurant;
import com.example.homemenu.models.UserOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MyOrders extends AppCompatActivity {
    private static final String TAG = "MyOrders";

    private RecyclerView ViewMyOrders;
    private OrderAdapter mAdapter;
    private static ArrayList<UserOrder> userOrderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        new GetOrders().execute();


        init();





    }

    private void init() {
        ViewMyOrders = (RecyclerView) findViewById(R.id.myOrders);
        ViewMyOrders.setLayoutManager(new LinearLayoutManager(this));
        ViewMyOrders.setHasFixedSize(true);
        userOrderArrayList = new ArrayList<>();
        mAdapter = new OrderAdapter(userOrderArrayList, this);
        ViewMyOrders.setAdapter(mAdapter);

    }
    private class GetOrders extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://app-licenta-server.herokuapp.com/getUserOrders";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray restaurants = jsonObj.getJSONArray("orders");

                    // looping through All Contacts
                    for (int i = 0; i < restaurants.length(); i++) {
                        JSONObject c = restaurants.getJSONObject(i);
                        Log.d(TAG, "userId: "+Common.currentUser.get_id());
                        if(Common.currentUser.get_id().equals( c.getString("userId"))){
                            boolean ok = Common.currentUser.get_id().equals( c.getString("userId"));
                            Log.d(TAG, "doInBackground: "+ok);
                            String restaurantName="";
                            String statusOrder;
                            String orderAddress;
                            String orderAmount;
                            Date orderDate;
                            String restaurantId = c.getString("restaurantId");
                            for(Restaurant r : Common.currentRestaurant){
                                if(r.getId().equals(restaurantId)){
                                    restaurantName = r.getName();
                                    Log.d(TAG, "restaurant name: "+restaurantName);

                                }
                            }
                            statusOrder = c.getString("status");
                            orderAddress = c.getString("address");
                            orderAmount = c.getString("totalAmount");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            orderDate = format.parse(c.getString("dateOrder"));
                            UserOrder userOrder = new UserOrder();
                            userOrder.setRestaurantName(restaurantName);
                            userOrder.setStatusOrder(statusOrder);
                            userOrder.setOrderAddress(orderAddress);
                            userOrder.setOrderAmount(orderAmount+" RON");
                            userOrder.setOrderDate(orderDate);
                            userOrderArrayList.add(userOrder);

                        }
                        Log.d(TAG, "size: "+userOrderArrayList.size());

                    }
                    Collections.sort(userOrderArrayList, new Comparator<UserOrder>() {
                        @Override
                        public int compare(UserOrder or1, UserOrder or2) {
                            if (or1.getOrderDate() == null || or2.getOrderDate() == null)
                                return 0;
                            return or2.getOrderDate().compareTo(or1.getOrderDate());
                        }
                    });
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();


        }
    }



    
}
