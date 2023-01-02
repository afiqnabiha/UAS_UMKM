package com.example.uasppbganjil2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailPesananActivity extends AppCompatActivity {
    TextView sid, stotal, sstatus, sbukti, salamat, songkir;
    Button btn_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        String total = extras.getString("total");
        String bukti = extras.getString("bukti");
        String alamat = extras.getString("alamat");
        String ongkir = extras.getString("ongkir");
        sid = findViewById(R.id.detail_id);
        stotal = findViewById(R.id.detail_total);
        sbukti = findViewById(R.id.detail_bukti);
        songkir = findViewById(R.id.detail_ongkir);
        salamat = findViewById(R.id.detail_alamat);
        btn_upload = findViewById(R.id.btn_upload);

        sid.setText("ID Pesanan :" + id);
        stotal.setText("Total Pembayaran :" + total);
        sbukti.setText("Bukti Transfer :" + bukti);
        songkir.setText("Ongkos Kirim :" + ongkir);
        salamat.setText("Alamat :" + alamat);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPesananActivity.this, UploadActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

    }
}