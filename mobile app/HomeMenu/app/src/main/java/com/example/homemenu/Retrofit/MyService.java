package com.example.homemenu.Retrofit;

import com.example.homemenu.models.OrderItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("address") String address,
                                    @Field("phone") String phone);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("phone") String phone, @Field("password") String password);


    @POST("placeOrder")
    @FormUrlEncoded
    Observable<String> placeOrder(@Field("order")JSONObject order);


}
