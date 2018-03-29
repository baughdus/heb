package com.heb.product.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "product")
public class Product {

    public enum Department { Produce, Grocery, Pharmacy }

    public enum Unit { lb, Each }

    @Id
    public String id;

    public String description;
    public Date lastSold;
    public String shelfLife;
    public Product.Department department;
    public String price;
    public Product.Unit unit;
    public Integer xFor;
    public String cost;

    public Product() {
    }

    public Product(String id, String description, Date lastSold, String shelfLife, Department department, String price, Unit unit, Integer xFor, String cost) {
        this.id = id;
        this.description = description;
        this.lastSold = lastSold;
        this.shelfLife = shelfLife;
        this.department = department;
        this.price = price;
        this.unit = unit;
        this.xFor = xFor;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return String.format("Product[id=%s, description=%s, lastSold=%s, shelfLife=%s, department=%s, price=%s, unit=%s, xFor=%s, cost=%s]",
                id, description, lastSold, shelfLife, department, price, unit, xFor, cost);
    }
}
