package com.example.homemenu.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homemenu.Interface.ItemClickListener;
import com.example.homemenu.R;
import com.example.homemenu.adapters.HttpHandler;
import com.example.homemenu.adapters.RestaurantAdapter;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.Meniu;
import com.example.homemenu.models.Restaurant;
import com.example.homemenu.models.UserOrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ItemClickListener{

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView mRestaurantRecyclerView;
    private RestaurantAdapter mAdapter;
    static private ArrayList<Restaurant> mRestaurantCollection;
    private String TAG = HomeActivity.class.getSimpleName();
    TextView userNameTextView;
    NavigationView navigation;
    private static ArrayList<UserOrder> userOrderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.currentFinalOrder == null){
                    Snackbar.make(view, "Nu exista nicio comanda ! Alege acum un restaurant!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                Intent intent = new Intent(HomeActivity.this,Cart.class);
                startActivity(intent);
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_menu, R.id.nav_cart, R.id.nav_orders,
                R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigation = (NavigationView) findViewById(R.id.nav_view);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_menu:
                        startActivity(new Intent(HomeActivity.this,MenuActivity.class));
                        break;
                    case R.id.nav_cart:
                        if(Common.currentFinalOrder == null)
                            Toast.makeText(HomeActivity.this, "Nu exista nicio comanda", Toast.LENGTH_SHORT).show();
                        else
                            startActivity(new Intent(HomeActivity.this,Cart.class));
                        break;
                    case R.id.nav_orders:
                        startActivity(new Intent(HomeActivity.this,MyOrders.class));
                        break;
                    case R.id.nav_log_out:
                        startActivity(new Intent(HomeActivity.this,MainActivity.class));
                        break;
                }
                return  false;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        userNameTextView = (TextView)headerView.findViewById(R.id.textview_user_name);
        userNameTextView.setText(Common.currentUser.getName());
        Log.e(TAG, "user: " + Common.currentUser.getPhone());




        init();
        new GetRestaurants().execute();

        Common.currentRestaurant = mRestaurantCollection;
        Common.currenUserOrders = userOrderArrayList;

    }


    private void init() {
        mRestaurantRecyclerView = (RecyclerView) findViewById(R.id.recycler_menu);
        mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantRecyclerView.setHasFixedSize(true);
        mRestaurantCollection = new ArrayList<>();
        mAdapter = new RestaurantAdapter(mRestaurantCollection, this,this);
        mRestaurantRecyclerView.setAdapter(mAdapter);
        Common.currentFinalOrder = null;
        userOrderArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(int position) {

        Restaurant rest = mRestaurantCollection.get(position);
        Common.currentRestaurantId = rest.getId();
        Intent intent = new Intent(this,MenuActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }


    private class GetRestaurants extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://app-licenta-server.herokuapp.com/getRestaurantforAndroid";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray restaurants = jsonObj.getJSONArray("restaurants");

                    // looping through All Contacts
                    for (int i = 0; i < restaurants.length(); i++) {
                        JSONObject c = restaurants.getJSONObject(i);
                        String id;
                        String name;
                        String address;
                        String imageURL;
                        ArrayList<Meniu> meniuArrayList = new ArrayList<>();
                        id = c.getString("_id");
                        name = c.getString("name");
                        address = c.getString("address");
                        imageURL = c.getString("image");
                        JSONArray x = c.getJSONArray("menu");
                        for(int j = 0; j<x.length();j++){
                            JSONObject n = x.getJSONObject(j);
                            String food = n.getString("food");
                            String description = n.getString("description");
                            String price = n.getString("price");

                            Meniu m ;
                            m = new Meniu(food,description,price);
                            meniuArrayList.add(m);
                        }
                        Restaurant restaurant = new Restaurant();
                        restaurant.setId(id);
                        restaurant.setName(name);
                        restaurant.setAddress(address);
                        restaurant.setImageURL(imageURL);
                        restaurant.setMeniuList(meniuArrayList);
                        mRestaurantCollection.add(restaurant);
                    }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



}

