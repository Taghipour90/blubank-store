package com.blubank.store.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameNotFoundException extends RuntimeException {

    private final static Logger logger = LoggerFactory.getLogger(UsernameNotFoundException.class);

    public UsernameNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
