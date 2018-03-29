package com.heb.product.services.impl;

import com.heb.product.models.Product;
import com.heb.product.requests.ProductRequest;
import com.heb.product.services.ProductMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ProductMongoServiceImpl implements ProductMongoService {

    // MongoTemplate used to access the mongo data source
    @Autowired
    private MongoTemplate mongoTemplate;

    // Save all potential min/max fields to an array for further use
    private final List<String> minMaxFields = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("lastSold"); // date
                add("shelfLife"); // integer
                add("price"); // float
                add("cost"); // float
            }});

    /**
     * Dynamic Query method that utilizes the Mongo Query and Criteria so only one method is needed.
     * @param request
     * @return
     */
    @Override
    public List<Product> getProducts(ProductRequest request) {
        // Create Query object
        Query query = new Query();

        // If there's a min/max request, temporarily save the field it's requested for (ex: price is save for minPrice/maxPrice)
        String minMaxfield = null;
        // Save the temporary min object if available
        Object min = null;
        // Save the temporary max object if available
        Object max = null;

        // Use Java 8 reflection to iterate over all the fields in the ProductRequest class
        for (Field field: request.getClass().getDeclaredFields()) {
            try {
                // Check if the field isn't null and has been provided from the request
                if (field.get(request) != null) {

                    // Check if the field is a possible min value, save if so
                    if (field.getName().contains("min")) {
                        min = field.get(request);

                    // Check if the field is a possible max value, save if so
                    } else if (field.getName().contains("max")) {
                        max = field.get(request);

                        // Add to query criteria if neither min or max
                    } else {
                        // Check if field is a String class since it requires regex to search
                        if (field.getType() == String.class) {
                            query.addCriteria(Criteria.where(field.getName()).regex(".*" + field.get(request).toString() + ".*"));
                        // If field is not a String class, search for that exact value
                        } else {
                            query.addCriteria(Criteria.where(field.getName()).is(field.get(request)));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            // Check if minMaxField (base field) is not null and check if the method has moved on to the next request field (ex: minMaxField = lastSold and current 'field' = shelfLife
            if (minMaxfield != null && (!field.getName().toLowerCase().contains(minMaxfield.toLowerCase()) || field.getName().equals(request.getClass().getDeclaredFields()[request.getClass().getDeclaredFields().length - 1].getName()))) {

                // check to see if one of min or max is not null and add it to the criteria
                if (min != null || max != null) {

                    // Add to query criteria based on the save field and if both min and max value are present
                    query.addCriteria(Criteria.where(minMaxfield).gte(min != null ? min : this.convertMin(minMaxfield)).lte(max != null ? max : this.convertMax(minMaxfield)));

                    // Reset all min/max criteria
                    minMaxfield = null;
                    min = null;
                    max = null;
                }
            }

            // Check the declared array to see if this field can be a min/max field, if so save it temporarily
            if (minMaxFields.contains(field.getName())) {
                minMaxfield = field.getName();
            }
        }
        // Query using MongoTemplate to get list of resulting documents
        List<Product> products = mongoTemplate.find(query, Product.class);
        return products;
    }

    /*
     * Below two methods are used to determine the min/max values if not provided from the user
     */

    /**
     * Converts value to min value depending on if its an Integer, Double or use the Earliest Date
     * @param field
     * @return
     */
    private Object convertMin(String field) {
        if (field.toLowerCase().contains("lastsold")) {
            return new Date(Long.MIN_VALUE);
        } else if (field.toLowerCase().contains("shelflife")) {
            return 0;
        }
        return 0.00d;
    }

    /**
     * Converts value to max value depending on if its an Integer, Double or use the Current Date
     * @param field
     * @return
     */
    private Object convertMax(String field) {
        if (field.toLowerCase().contains("lastsold")) {
            return new Date();
        } else if (field.toLowerCase().contains("shelflife")) {
            return 9999;
        }
        return 999.99d;
    }
}
