package com.example.uasppbganjil2022.Model;

public class PesananModel {
    String id, harga, bukti, alamat, ongkir, produk;

    public PesananModel(String id, String harga, String bukti, String alamat, String ongkir ,String produk) {
        this.id = id;
        this.harga = harga;
        this.bukti = bukti;
        this.ongkir = ongkir;
        this.alamat = alamat;
        this.produk = produk;
    }

    public PesananModel() {
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getOngkir() {
        return ongkir;
    }


    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
