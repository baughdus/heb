package com.heb.product.services;

import com.heb.product.models.Product;
import com.heb.product.requests.ProductRequest;

import java.util.List;

public interface ProductMongoService {

    List<Product> getProducts(ProductRequest request);

}
