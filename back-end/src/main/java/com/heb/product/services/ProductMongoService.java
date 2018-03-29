package com.heb.product.services;

import com.heb.product.models.Product;
import com.heb.product.requests.ProductRequest;

import java.util.List;

/**
 * MongoDB Service used for creating and querying against Mongo
 */
public interface ProductMongoService {
    /**
     * Dynamic Query method that utilizes the Mongo Query and Criteria so only one method is needed.
     * @param request
     * @return
     */
    List<Product> getProducts(ProductRequest request);
}
