package com.example.uasppbganjil2022;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.uasppbganjil2022.Adapter.ProductAdapter;
import com.example.uasppbganjil2022.Model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SharedPreferences mPreferences;
    SharedPreferences sharedPreferences;
    ListView list;
    SwipeRefreshLayout swipe;
    List<ProductModel> productlist = new ArrayList<ProductModel>();
    ProductAdapter productAdapter;
    TextView text_total;
    CardView cardView;
    ImageView button_clear_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView = findViewById(R.id.cardView2);
        text_total = findViewById(R.id.text_total);
        button_clear_cart = findViewById(R.id.delete_cart);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = sharedPreferences.getString("product_id", "");
                Intent i = new Intent(MainActivity.this, PesanActivity.class);
                i.putExtra("productid", value);
                startActivity(i);
            }
        });
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);
        text_total.setText(sharedPreferences.getString("total", "0"));
        if (sharedPreferences.getString("access_token", null) == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        swipe = findViewById(R.id.swipe);
        list = findViewById(R.id.list_view);

        productAdapter = new ProductAdapter(MainActivity.this, productlist);
        list.setAdapter(productAdapter);
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                productlist.clear();
                productAdapter.notifyDataSetChanged();
                callVolley();
            }
        });
        button_clear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipe.setRefreshing(true);
                updateCart("", "", "", "remove");
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String product = productlist.get(i).getProductid();
                final String ongkir = productlist.get(i).getOngkir();
                final String harga = productlist.get(i).getPrice();
                swipe.setRefreshing(true);
                updateCart(product, ongkir, harga, "update");
            }
        });
    }

    private void updateCart(String produk, String ongkir, String harga, String method) {
        if (method.equals("update")) {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("product_id", produk);
            editor.putString("ongkir", ongkir);
            editor.putString("harga", harga);
            editor.commit();
            swipe.setRefreshing(false);
            text_total.setText(sharedPreferences.getString("harga", "0"));
        } else if (method.equals("remove")) {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("product_id", null);
            editor.putString("ongkir", null);
            editor.putString("harga", null);
            editor.commit();
            text_total.setText(sharedPreferences.getString("harga", "0"));
            swipe.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.call_center:
                showcallcenter();
                return true;
            case R.id.sms_center:
                showsmscenter();
                return true;
            case R.id.location:
                showlocation();
                return true;
            case R.id.update_user:
                startActivity(new Intent(MainActivity.this, UpdatePasswordActivity.class));
                return true;
            case R.id.logout:
                this.logout();
                return true;
            case R.id.pesanan:
                startActivity(new Intent(MainActivity.this, PesananActivity.class));
                return true;
            case R.id.administrator_btn:
                startActivity(new Intent(MainActivity.this, AdministratorActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showlocation() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void logout() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showsmscenter() {
        Intent intent = new Intent(this, SmsCenterActivity.class);
        startActivity(intent);
    }

    private void showcallcenter() {
        Intent intent = new Intent(this, CallCenterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        productlist.clear();
        productAdapter.notifyDataSetChanged();
        callVolley();
    }

    public void callVolley() {
        productlist.clear();
        productAdapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

//        Make JSON Request
        JsonArrayRequest jArr = new JsonArrayRequest(Url.URI + "/product", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Parse Json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        ProductModel item = new ProductModel();

                        item.setProductid(obj.getString("id"));
                        item.setImage(obj.getString("gambar"));
                        item.setName(obj.getString("nama"));
                        item.setPrice(obj.getString("harga"));
                        item.setDescription(obj.getString("deskripsi"));
                        item.setOngkir(obj.getString("ongkir"));

                        productlist.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productAdapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }
}