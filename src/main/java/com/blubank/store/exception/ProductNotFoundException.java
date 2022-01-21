package com.blubank.store.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductNotFoundException extends RuntimeException {

    private final static Logger logger = LoggerFactory.getLogger(ProductNotFoundException.class);

    public ProductNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
