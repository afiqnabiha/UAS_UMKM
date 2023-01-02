package com.example.uasppbganjil2022;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uasppbganjil2022.Adapter.ProductAdapter;
import com.example.uasppbganjil2022.Model.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KelolaProdukActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    SharedPreferences sharedPreferences;
    ListView list;
    SwipeRefreshLayout swipe;
    List<ProductModel> productlist = new ArrayList<ProductModel>();
    ProductAdapter productAdapter;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_produk);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);
        productAdapter = new ProductAdapter(KelolaProdukActivity.this, productlist);
        swipe = findViewById(R.id.swipe_kelolaproduk);
        list = findViewById(R.id.admin_list_product);
        fab = findViewById(R.id.admin_produk_add);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(KelolaProdukActivity.this, TambahProdukActivity.class));
        });
        list.setAdapter(productAdapter);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                hapus(productlist.get(i).getProductid());
                return false;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name =productlist.get(i).getName();
                String description =productlist.get(i).getDescription();
                String ongkir =productlist.get(i).getOngkir();
                String productid =productlist.get(i).getProductid();
                String price =productlist.get(i).getPrice();
                String image =productlist.get(i).getImage();

                Intent intent = new Intent(KelolaProdukActivity.this, UpdateProdukActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("description",description);
                intent.putExtra("ongkir",ongkir);
                intent.putExtra("productid",productid);
                intent.putExtra("price",price);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });
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
    }


    @Override
    public void onRefresh() {
        productlist.clear();
        productAdapter.notifyDataSetChanged();
        callVolley();
    }
    private void hapus(String produkid) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/produk-delete",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                Toast.makeText(KelolaProdukActivity.this, "Produk dihapus", Toast.LENGTH_SHORT).show();
                                callVolley();
                            } else {
                                Toast.makeText(KelolaProdukActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(KelolaProdukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KelolaProdukActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("produkid", produkid);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + sharedPreferences.getString("access_token", null));
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void callVolley() {
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
                Toast.makeText(KelolaProdukActivity.this, "Error! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }

}