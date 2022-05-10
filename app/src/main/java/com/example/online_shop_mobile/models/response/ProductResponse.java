package com.example.online_shop_mobile.models.response;

import com.example.online_shop_mobile.models.Product;
import com.example.online_shop_mobile.models.User;

import java.util.List;

public class ProductResponse {
    private List<Product> products;

    public ProductResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
