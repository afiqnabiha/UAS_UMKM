package com.example.uasppbganjil2022;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahProdukActivity extends AppCompatActivity {
    EditText nama, deskripsi, gambar, harga, ongkir;
    Button button1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);
        nama = findViewById(R.id.admin_add_nama_produk);
        deskripsi = findViewById(R.id.admin_add_deskripsi_produk);
        gambar = findViewById(R.id.admin_add_gambar_produk);
        harga = findViewById(R.id.admin_add_harga_produk);
        ongkir = findViewById(R.id.admin_add_ongkir_produk);
        button1 = findViewById(R.id.admin_addproduk_btn);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);

        button1.setOnClickListener(view -> {
            addproduk();
        });
    }

    private void addproduk() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/produk-add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                Toast.makeText(TambahProdukActivity.this, "Produk ditambahkan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(TambahProdukActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(TambahProdukActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TambahProdukActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", nama.getText().toString());
                params.put("deskripsi", deskripsi.getText().toString());
                params.put("ongkir", ongkir.getText().toString());
                params.put("harga", harga.getText().toString());
                params.put("gambar", gambar.getText().toString());

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
}