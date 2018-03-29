package com.heb.product.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heb.product.models.Product;

import java.util.Date;

public class ProductRequest {

    public ProductRequest() {
    }

    public ProductRequest(String description,
                          Date lastSold, Date minLastSold, Date maxLastSold,
                          Integer shelfLife, Integer minShelfLife, Integer maxShelfLife,
                          Product.Department department,
                          Double price, Double minPrice, Double maxPrice,
                          Product.Unit unit,
                          Integer xFor,
                          Double cost, Double minCost, Double maxCost) {
        this.description = description;
        this.lastSold = lastSold;
        this.minLastSold = minLastSold;
        this.maxLastSold = maxLastSold;
        this.shelfLife = shelfLife;
        this.minShelfLife = minShelfLife;
        this.maxShelfLife = maxShelfLife;
        this.department = department;
        this.price = price;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.unit = unit;
        this.xFor = xFor;
        this.cost = cost;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }

    public String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date lastSold;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date minLastSold;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date maxLastSold;

    public Integer shelfLife;
    public Integer minShelfLife;
    public Integer maxShelfLife;

    public Product.Department department;

    public Double price;
    public Double minPrice;
    public Double maxPrice;

    public Product.Unit unit;

    public Integer xFor;

    public Double cost;
    public Double minCost;
    public Double maxCost;

}
