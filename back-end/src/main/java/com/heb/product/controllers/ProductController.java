package com.heb.product.controllers;

import com.heb.product.models.Product;
import com.heb.product.repositories.ProductMongoRepository;
import com.heb.product.requests.ProductRequest;
import com.heb.product.services.ProductMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductMongoRepository mongoRepository;

    @Autowired
    private ProductMongoService mongoService;

    /**
     * GET Rest endpoint for retrieving all the records in the database
     * @return List<Product>
     */
    @GetMapping("/mongo")
    public List<Product> getMongo() {
        return mongoRepository.findAll();
    }

    /**
     * POST Rest endpoint that requires teh ProductRequest model in the body to progress
     * If proper body is presented, moves onto the ProductMongoService to properly query.
     * @param product
     * @return List<Product>
     */
    @PostMapping("/mongo")
    public List<Product> searchMongo(@RequestBody ProductRequest product) {
        return mongoService.getProducts(product);
    }

}
