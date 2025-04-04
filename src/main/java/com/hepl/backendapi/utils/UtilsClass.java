package com.hepl.backendapi.utils;

import com.hepl.backendapi.entity.dbtransac.StockEntity;
import jakarta.validation.ConstraintViolationException;

import java.util.Collections;

public class UtilsClass {

    public static void validateQuantityInRange(int quantity, StockEntity stockEntity) {
        int min = stockEntity.getStockMin();
        int max = stockEntity.getStockMax();

        if (quantity < min || quantity > max) {
            throw new ConstraintViolationException("Quantity must be between " + min + " and " + max, Collections.emptySet());
        }
    }


}
