package com.example.uasppbganjil2022.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uasppbganjil2022.Model.PesananModel;
import com.example.uasppbganjil2022.R;

import java.util.List;

public class PesananAdapter extends BaseAdapter {
    Activity activity;
    List<PesananModel> pesanan_item;
    private LayoutInflater inflater;

    public PesananAdapter(Activity activity, List<PesananModel> pesanan_item) {
        this.activity = activity;
        this.pesanan_item = pesanan_item;
    }

    @Override
    public int getCount() {
        return pesanan_item.size();
    }

    @Override
    public Object getItem(int i) {
        return pesanan_item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.item_pesanan, null);

        TextView pesanan_id = (TextView) view.findViewById(R.id.pesanan_id);
        TextView pesanan_total = (TextView) view.findViewById(R.id.pesanan_harga);
        TextView pesanan_bukti = (TextView) view.findViewById(R.id.pesanan_bukti);
        TextView pesanan_alamat = (TextView) view.findViewById(R.id.pesanan_alamat);
        TextView pesanan_ongkir = (TextView) view.findViewById(R.id.pesanan_ongkir);
        TextView pesanan_produk = (TextView) view.findViewById(R.id.pesanan_produk);

        PesananModel data = pesanan_item.get(i);

        pesanan_id.setText(data.getId());
        pesanan_total.setText(data.getHarga());
        pesanan_bukti.setText(data.getBukti());
        pesanan_alamat.setText(data.getAlamat());
        pesanan_ongkir.setText(data.getOngkir());
        pesanan_produk.setText(data.getProduk());
        return view;
    }
}
