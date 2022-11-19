package com.example.homemenu.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.homemenu.R;
import com.example.homemenu.Retrofit.MyService;
import com.example.homemenu.Retrofit.RetrofitClient;
import com.example.homemenu.adapters.CartAdapter;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Cart extends AppCompatActivity {
    private RecyclerView mCartRecycleView;
    private CartAdapter mCartAdapter;
    TextView totalAmount;
    private static final String TAG = "Cart";
    Double total_Amaount = 0.0;
    Button btnPlaceOrder;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyService myService;
    Retrofit retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        ArrayList<OrderItem>list;
        list = Common.currentFinalOrder.getOrderItems();
        //Log.d(TAG, "onCreate: "+list.get(0).getPrice());

        for(OrderItem i : list){
            int multiply = Integer.parseInt(i.getQuantity());
            double price = Double.parseDouble(i.getPrice());
            total_Amaount += multiply*price;
          //  Log.d(TAG, "totalAmount: "+total_Amaount);
        }
        Common.currentFinalOrder.setTotalAmount(String.valueOf(totalAmount));
        totalAmount.setText(String.valueOf(total_Amaount)+" RON");

        Log.d(TAG, "totalAmount: "+Common.currentFinalOrder.getAddressCustomer());

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.currentFinalOrder.getTotalAmount().equals("0")){
                    Toast.makeText(Cart.this, "Nu exista nicio comanda !", Toast.LENGTH_SHORT).show();
                }
                else if(Common.currentFinalOrder != null){
                    placeOrder();
                }

                else Toast.makeText(Cart.this, "Nu exista nicio comanda !", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void init() {
        mCartRecycleView = findViewById(R.id.listCart);
        mCartRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mCartRecycleView.setHasFixedSize(true);
        mCartAdapter = new CartAdapter(Common.currentFinalOrder.getOrderItems(),this);
        mCartRecycleView.setAdapter(mCartAdapter);
        totalAmount = findViewById(R.id.totalAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        //totalAmount.setText(Common.currentFinalOrder.getTotalAmount());
        //totalAmount.setText(Common.currentTotalAmount);

        retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);
    }

    public void placeOrder(){
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(Cart.this);
        alterDialog.setTitle("One more step ! Confirm address for your order !");
        alterDialog.setMessage("Current address is: "+Common.currentFinalOrder.getAddressCustomer()+"\n"+

                "Do you want to get your order to another address?");
       // alterDialog.setMessage("Do you want to get your other to another address?");


        alterDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alterDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setAnotherAddress();

            }
        });

        alterDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendOrderToServer();
                //Toast.makeText(Cart.this, "Done, Order placed !", Toast.LENGTH_SHORT).show();
                //Toast.makeText(Cart.this, ""+Common.currentUser.getAddress(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(Cart.this,HomeActivity.class));
                Common.currentFinalOrder = null;
            }
        });

        alterDialog.show();
    }

    private void sendOrderToServer() {
        JSONObject order = new JSONObject();
        try {
            order.put("userId",Common.currentUser.get_id());
            order.put("restaurantId",Common.currentRestaurantId);
            JSONArray menuArray = new JSONArray();
            ArrayList<OrderItem> orderItemList = Common.currentFinalOrder.getOrderItems();
            for(OrderItem i : orderItemList){
                JSONObject itemOrder = new JSONObject();
                itemOrder.put("food",i.getFood());
                itemOrder.put("quantity",i.getQuantity());
                itemOrder.put("price",i.getPrice());
                menuArray.put(itemOrder);
            }
            order.put("order",menuArray);
            order.put("status","ordered");
            order.put("address",Common.currentFinalOrder.getAddressCustomer());
            order.put("totalAmount",total_Amaount);
            order.put("phone",Common.currentUser.getPhone());
            Date date = new Date();
            order.put("dateOrder",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        compositeDisposable.add(myService.placeOrder(order)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {
                        Toast.makeText(Cart.this, "" + res, Toast.LENGTH_SHORT).show();
                        Common.currentFinalOrder = null;
                    }
                })
        );



    }

    private void setAnotherAddress() {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(Cart.this);
        alterDialog.setTitle("Set address for delivery !");
        alterDialog.setMessage("Enter your address");
        final EditText etdAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        etdAddress.setLayoutParams(layoutParams);
        alterDialog.setView(etdAddress);
        alterDialog.setIcon(R.drawable.ic_location_on_black_24dp);

        alterDialog.setNeutralButton("Confirm ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Common.currentFinalOrder.setAddressCustomer(etdAddress.getText().toString());
                //Toast.makeText(Cart.this, "new address: "+Common.currentFinalOrder.getAddressCustomer(), Toast.LENGTH_SHORT).show();
                sendOrderToServer();
                startActivity(new Intent(Cart.this, HomeActivity.class));
            }
        });
        alterDialog.show();
    }

}
