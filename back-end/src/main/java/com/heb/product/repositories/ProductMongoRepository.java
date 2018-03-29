package com.heb.product.repositories;

import com.heb.product.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductMongoRepository extends MongoRepository<Product, String> { }
