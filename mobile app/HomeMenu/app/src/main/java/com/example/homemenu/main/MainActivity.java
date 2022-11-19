package com.example.homemenu.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.homemenu.R;
import com.example.homemenu.Retrofit.MyService;
import com.example.homemenu.Retrofit.RetrofitClient;
import com.example.homemenu.adapters.HttpHandler;
import com.example.homemenu.models.Common;
import com.example.homemenu.models.User;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText etd_login_phone, etd_login_password;
    Button btn_login;
    private String TAG = MainActivity.class.getSimpleName();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyService myService;
    static ArrayList<User> userArrayList = new ArrayList<>();
    User user;
    public ArrayList<User> list = new ArrayList<>();


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init

        Retrofit retrofitClient = RetrofitClient.getInstance();
        myService = retrofitClient.create(MyService.class);
        etd_login_phone = (MaterialEditText) findViewById(R.id.etd_phone);
        etd_login_password = (MaterialEditText) findViewById(R.id.etd_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        new GetUser().execute();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(etd_login_phone.getText().toString(), etd_login_password.getText().toString());

                user = new User();

                User c = new User();


                //Log.e(TAG, "user: " + c.getName());
                for(int i = 0; i < list.size(); i++)
                    if(list.get(i).getPhone().equals(etd_login_phone.getText().toString())) {
                        c = userArrayList.get(i);
                        //Toast.makeText(MainActivity.this, "" + res, Toast.LENGTH_SHORT).show();
                    }
                user = c;
                Common.currentUser = user;
                Log.e(TAG, "user: " + Common.currentUser.getName());
            }
        });
        txt_create_account = (TextView) findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.register_layout, null);
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_name)
                        .setTitle("REGISTRATION")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Register")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MaterialEditText etd_register_name = (MaterialEditText) register_layout.findViewById(R.id.etd_name);
                                MaterialEditText etd_register_email = (MaterialEditText) register_layout.findViewById(R.id.etd_email);
                                MaterialEditText etd_register_password = (MaterialEditText) register_layout.findViewById(R.id.etd_password);
                                MaterialEditText etd_register_address = (MaterialEditText) register_layout.findViewById(R.id.etd_address);
                                MaterialEditText etd_register_phone = (MaterialEditText) register_layout.findViewById(R.id.etd_phone);

                                if (TextUtils.isEmpty(etd_register_name.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(etd_register_email.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(etd_register_password.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(etd_register_address.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Address cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(etd_register_phone.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Phome cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                registerUser(etd_register_name.getText().toString(), etd_register_email.getText().toString(),
                                        etd_register_password.getText().toString(), etd_register_address.getText().toString(), etd_register_phone.getText().toString()
                                );
                                /*
                              user = new User(etd_register_name.getText().toString(),etd_register_email.getText().toString(),
                                        etd_register_password.getText().toString(),etd_register_address.getText().toString(),etd_register_phone.getText().toString()

                               );
                               */


                            }
                        }).show();
            }


        });
    }

    private void registerUser(String name, String email, String password, String address, String phone) {

        compositeDisposable.add(myService.registerUser(name, email, password, address, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {
                        Toast.makeText(MainActivity.this, "" + res, Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    private void loginUser(final String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phome cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        final String nrPhone = phone;
        compositeDisposable.add(myService.loginUser(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String res) throws Exception {
                        Toast.makeText(MainActivity.this, "" + res, Toast.LENGTH_SHORT).show();
                        if (res.equals("Login success!")) {

                            openUserActivity();

                        }
                    }
                })

        );

    }

    private void openUserActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }




    private class GetUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://app-proiect.herokuapp.com/view-feedbacks";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray users = jsonObj.getJSONArray("users");

                    // looping through All Contacts
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject c = users.getJSONObject(i);

                        String _id;
                        String name;
                        String email;
                        String address;
                        String password;
                        String phone;

                        _id = c.getString("_id");
                        name = c.getString("name");
                        email = c.getString("email");
                        password = c.getString("password");
                        address = c.getString("address");
                        phone = c.getString("phone");

                        User usr = new User();

                        usr.set_id(_id);
                        usr.setName(name);
                        usr.setEmail(email);
                        usr.setPassword(password);
                        usr.setAddress(address);
                        usr.setPhone(phone);

                        list.add(usr);


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
                userArrayList.addAll(list);


        }

    }
}
