package com.heb.product.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/**
 * Class that determines if a field can have a min/max values
 * Notifies that ProductMongoService whether to use a single or min/max value
 * Required a separate date class due to issues parsing with the MultiRequest class
 */
public class MultiRequestDate {
    public Boolean isMulti;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public Date value;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public Date min;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    public Date max;
}