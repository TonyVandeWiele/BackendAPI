package com.hepl.backendapi.utils;

import com.hepl.backendapi.dto.generic.UserDTO;
import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.exception.InvalidValueException;
import jakarta.validation.ConstraintViolationException;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

public class UtilsClass {

    public static void validateQuantityInRange(int quantity, StockEntity stockEntity) {
        int min = stockEntity.getStockMin();
        int max = stockEntity.getStockMax();

        if (quantity < min) {
            throw new InvalidValueException(
                    "The final quantity (" + quantity + ") is below the minimum allowed stock level (" + min + ")."
            );
        }

        if (quantity > max) {
            int current = stockEntity.getQuantity();
            String message;

            if (quantity > current) {
                message = "Requested stock quantity (" + quantity + ") exceeds current stock (" + current + "). Not enough items in stock.";
            } else {
                message = "The final quantity (" + quantity + ") exceeds the maximum allowed stock level (" + max + "). Cannot overstock.";
            }

            throw new InvalidValueException(message);
        }
    }



}
