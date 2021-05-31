package main.backend.order;

import java.util.ArrayList;
import java.util.List;

public final class OrderList {

    // create immutable list to reduce bug occurrence
    private final List<Order> orderList;

    public OrderList() { this.orderList = new ArrayList<>(); }

    public void add(final Order order) {
        this.orderList.add(order);
    }

    public void remove(final Order order) {
        this.orderList.remove(order);
    }

    public List<Order> getOrderList() { return this.orderList; }
}