package com.heb.product.models;

/**
 * Class that determines if a field can have a min/max values
 * Notifies that ProductMongoService whether to use a single or min/max value
 * Objects are used sense a Integer/Double/Float can all be used
 */
public class MultiRequest {
    public Boolean isMulti;
    public Object value;
    public Object min;
    public Object max;
}