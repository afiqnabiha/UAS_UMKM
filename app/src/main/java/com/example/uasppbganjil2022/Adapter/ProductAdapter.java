package com.example.uasppbganjil2022.Adapter;

import android.app.Activity;
import android.content.Context;

import com.example.uasppbganjil2022.MainActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uasppbganjil2022.Model.ProductModel;
import com.example.uasppbganjil2022.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    Activity activity;
    List<ProductModel> product_item;
    private LayoutInflater inflater;
    MainActivity main = new MainActivity();


    public ProductAdapter(Activity activity, List<ProductModel> product_item) {
        this.activity = activity;
        this.product_item = product_item;
    }

    @Override
    public int getCount() {
        return product_item.size();
    }

    @Override
    public Object getItem(int i) {
        return product_item.get(i);
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
            view = inflater.inflate(R.layout.item_product, null);

        ImageView product_image = (ImageView) view.findViewById(R.id.product_image);
        TextView product_name = (TextView) view.findViewById(R.id.product_name);
        TextView product_price = (TextView) view.findViewById(R.id.product_price);
        TextView product_description = (TextView) view.findViewById(R.id.product_description);
        TextView product_id = (TextView) view.findViewById(R.id.product_id);
        TextView product_ongkir = (TextView) view.findViewById(R.id.product_ongkir);

        ProductModel data = product_item.get(i);

        Glide.with(view).load(data.getImage()).into(product_image);
        product_name.setText(data.getName());
        product_price.setText("Rp " + data.getPrice());
        product_description.setText(data.getDescription());
        product_id.setText(data.getProductid());
        product_ongkir.setText(data.getOngkir());

        product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity.getApplicationContext(), data.getDescription(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
