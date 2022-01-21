package com.blubank.store.exception;

import com.blubank.store.controller.StoreController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderNotFoundException extends RuntimeException {

    private final static Logger logger = LoggerFactory.getLogger(OrderNotFoundException.class);

    public OrderNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
