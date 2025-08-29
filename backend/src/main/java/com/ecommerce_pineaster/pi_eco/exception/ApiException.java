package com.ecommerce_pineaster.pi_eco.exception;

public class ApiException  extends RuntimeException{
    private static final long  serialVersionUID = 1L;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

}
