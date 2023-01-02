package com.example.uasppbganjil2022;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class UpdateKonsumenActivity extends AppCompatActivity {
    EditText nama;
    Button btn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_konsumen);
        Bundle extras = getIntent().getExtras();
        nama = findViewById(R.id.admin_update_nama_konsumen);
        sharedPreferences = getSharedPreferences("aplikasi-saya", MODE_PRIVATE);

        String vnama = extras.getString("nama");
        String vid = extras.getString("konsumenid");

        nama.setText(vnama);

        btn = findViewById(R.id.btn_update_konsumen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateKonsumen(vid);
            }
        });
    }

    private void updateKonsumen(String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.URI + "/konsumen-modif",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("message").equals("success")) {
                                Toast.makeText(UpdateKonsumenActivity.this, "Konsumen diubah", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateKonsumenActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(UpdateKonsumenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateKonsumenActivity.this, "Gagal" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama", nama.getText().toString());
                params.put("konsumenid",id);

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