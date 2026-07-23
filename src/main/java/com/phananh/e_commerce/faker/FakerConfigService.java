package com.phananh.e_commerce.faker;

import org.springframework.stereotype.Component;

@Component
public class FakerConfigService {

    private boolean enabled = false;
    private int users = 30;
    private int brands = 10;
    private int categories = 15;
    private int products = 50;
    private int orders = 100;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getBrands() {
        return brands;
    }

    public void setBrands(int brands) {
        this.brands = brands;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}

