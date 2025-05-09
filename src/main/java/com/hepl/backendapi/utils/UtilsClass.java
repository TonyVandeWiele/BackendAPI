package com.hepl.backendapi.utils;

import com.hepl.backendapi.entity.dbtransac.StockEntity;
import com.hepl.backendapi.exception.InvalidValueException;

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
