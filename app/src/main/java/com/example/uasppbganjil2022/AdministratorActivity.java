package com.example.uasppbganjil2022;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdministratorActivity extends AppCompatActivity {

    Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        button1 = findViewById(R.id.btn_produk);
        button2 = findViewById(R.id.btn_konsumen);
        button3 = findViewById(R.id.btn_laporan);

        button1.setOnClickListener(view -> {
            startActivity(new Intent(AdministratorActivity.this, KelolaProdukActivity.class));
        });
        button2.setOnClickListener(view -> {
            startActivity(new Intent(AdministratorActivity.this, KelolaKonsumenActivity.class));
        });
        button3.setOnClickListener(view -> {
            startActivity(new Intent(AdministratorActivity.this, LaporanActivity.class));
        });
    }
}