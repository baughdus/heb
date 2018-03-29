package com.heb.product.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heb.product.models.MultiRequest;
import com.heb.product.models.MultiRequestDate;
import com.heb.product.models.Product;

import java.util.Date;

/**
 * Class used as the body for POST request
 */
public class ProductRequest {

    public ProductRequest() {
    }

    public ProductRequest(String description, MultiRequestDate lastSold, MultiRequest shelfLife, Product.Department department, MultiRequest price, Product.Unit unit, Integer xFor, MultiRequest cost) {
        this.description = description;
        this.lastSold = lastSold;
        this.shelfLife = shelfLife;
        this.department = department;
        this.price = price;
        this.unit = unit;
        this.xFor = xFor;
        this.cost = cost;
    }

    public String description;
    public MultiRequestDate lastSold;
    public MultiRequest shelfLife;
    public Product.Department department;
    public MultiRequest price;
    public Product.Unit unit;
    public Integer xFor;
    public MultiRequest cost;

}
