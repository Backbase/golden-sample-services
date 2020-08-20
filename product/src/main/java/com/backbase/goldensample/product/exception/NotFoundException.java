package com.backbase.goldensample.product.exception;

public class NotFoundException extends Exception {
    private long id;
    public NotFoundException(long id) {
        super(String.format("Item is not found with id : '%s'", id));
    }
}
