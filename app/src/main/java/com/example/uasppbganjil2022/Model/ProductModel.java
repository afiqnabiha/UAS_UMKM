package com.example.uasppbganjil2022.Model;

public class ProductModel {
    String name, price, image, description,productid, ongkir;

    public ProductModel(String name, String price, String image, String description, String productid, String ongkir) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.productid = productid;
        this.ongkir = ongkir;
    }

    public ProductModel() {
    }

    public String getOngkir() {
        return ongkir;
    }

    public void setOngkir(String ongkir) {
        this.ongkir = ongkir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }
}
