package main.backend.order;

import main.backend.item.Item;

import java.util.HashMap;

public final class OrderCartMap extends HashMap<Item, Integer> {

    @Override
    public Integer put(final Item key, final Integer value) {
        if (key == null) {
            throw new IllegalArgumentException("Item cannot be null value");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Quantity must be positive integer ONLY");
        }
        //if same item ordered twice
        //add the previous quantity with latest
        if (super.containsKey(key)) {
            return super.put(key, super.get(key) + value);
        }
        return super.put(key, value);
    }
}