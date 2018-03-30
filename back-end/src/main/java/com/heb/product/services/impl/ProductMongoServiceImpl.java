package com.heb.product.services.impl;

import com.heb.product.models.MultiRequest;
import com.heb.product.models.MultiRequestDate;
import com.heb.product.models.Product;
import com.heb.product.requests.ProductRequest;
import com.heb.product.services.ProductMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class ProductMongoServiceImpl implements ProductMongoService {

    // MongoTemplate used to access the mongo data source
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Dynamic Query method that utilizes the Mongo Query and Criteria so only one method is needed.
     * @param request
     * @return
     */
    @Override
    public List<Product> getProducts(ProductRequest request) {
        // Create a MongoDB Query object
        Query query = new Query();

        // Use Java 8 reflection to get all fields from the ProductRequest class
        for (Field field: request.getClass().getDeclaredFields()) {
            try {
                // Check if the current field is of type MultiRequest
                if (field.getType() == MultiRequest.class) {

                    // Cast field into a MultiRequest object
                    MultiRequest multiRequest = (MultiRequest) field.get(request);
                    if (multiRequest != null) {
                        // Check if the request is a max/min
                        if (multiRequest.isMulti) {
                            // Add Criteria to the Query
                            query.addCriteria(Criteria.where(field.getName())
                                    .lte(multiRequest.max != null ? multiRequest.max : findMax(multiRequest.min))
                                    .gte(multiRequest.min != null ? multiRequest.min : findMin(multiRequest.max)));
                        } else {
                            // Add Criteria to the Query
                            query.addCriteria(Criteria.where(field.getName()).is(multiRequest.value));
                        }
                    }

                // Check if the current field is of the type MultiRequestDate
                // (needed due to dates being handled differently than numbers)
                } else if (field.getType() == MultiRequestDate.class) {

                    // Cast field into a MultiRequestDate object
                    MultiRequestDate multiRequest = (MultiRequestDate) field.get(request);
                    if (multiRequest != null) {
                        // Check if the request is a max/min
                        if (multiRequest.isMulti) {
                            // Add Criteria to the Query
                            query.addCriteria(Criteria.where(field.getName())
                                    .lte(multiRequest.max != null ? multiRequest.max : new Date())
                                    .gte(multiRequest.min != null ? multiRequest.min : new Date(Long.MIN_VALUE)));
                        } else {
                            // Add Criteria to the Query
                            query.addCriteria(Criteria.where(field.getName()).is(multiRequest.value));
                        }
                    }
                // If request isn't of a max/min field, make sure it isn't null
                } else if (field.get(request) != null) {
                    // Check if current field is a String (regex is required for Strings in MongoDB)
                    if (field.getType() == String.class) {
                        // Add Criteria to the Query with wildcards on each end of the String
                        query.addCriteria(Criteria.where(field.getName()).regex(".*" + field.get(request).toString() + ".*"));
                    } else {
                        // Add Criteria to the Query
                        query.addCriteria(Criteria.where(field.getName()).is(field.get(request)));
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }

        // Use the created query with the Mongo Template
        List<Product> products = mongoTemplate.find(query, Product.class);

        // Return the found results
        return products;
    }

    /*
     * Below two methods are used to determine the min/max values if not provided from the user
     */

    /**
     * Converts value to min value depending on if its an Integer, Double or use the Earliest Date
     * @param min
     * @return
     */
    private Object findMax(Object min) {
        if (min instanceof Double) {
            return 999.99d;
        } else if (min instanceof Integer) {
            return 9999;
        } else {
            return new Date();
        }
    }

    /**
     * Converts value to max value depending on if its an Integer, Double or use the Current Date
     * @param max
     * @return
     */
    private Object findMin(Object max) {
        if (max instanceof Double) {
            return 0.00d;
        } else if (max instanceof Integer) {
            return 0;
        } else {
            return new Date(Long.MIN_VALUE);
        }
    }

}
