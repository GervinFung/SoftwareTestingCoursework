package main.backend.member;

import main.backend.order.AreaDeliveryRateMap;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;
import main.backend.payment.Payment;

public class Customer {

    // create immutable object to reduce bug occurrence
    private final int registeredNumber;
    private final String name;
    private final Address address;

    public Customer(final String name, final int registeredNumber, final Address address) {
        this.registeredNumber = registeredNumber;
        this.name = name;
        this.address = address;
    }

    public Customer(final String name, final Address address) { this(name, 31 * (name.hashCode() + address.hashCode()), address); }

    public boolean isMember() { return false; }

    protected final Order makeOrder(final OrderCartMap itemsOrdered, final Order.ORDER_STATUS orderStatus, final AreaDeliveryRateMap areaDeliveryRateMap) {
        return new Order(itemsOrdered, this, orderStatus, areaDeliveryRateMap.getAreaDeliveryRateMap());
    }

    public Order createOrder(final OrderCartMap itemsOrdered, final AreaDeliveryRateMap areaDeliveryRateMap) {
        return this.makeOrder(itemsOrdered, Order.ORDER_STATUS.SUCCESSFUL, areaDeliveryRateMap);
    }

    //getter
    @Override
    public final int hashCode() { return this.getRegisteredNumber(); }
    public final int getRegisteredNumber() { return this.registeredNumber; }
    public final String getName() { return this.name; }
    public final Address getAddress() { return address; }

    public final Payment createPayment(final Order order, final Payment.PAYMENT_TYPE payment_type, final long accountNumber) {
        return new Payment(order, payment_type, accountNumber);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof Customer)) { return false; }

        return ((Customer)obj).getRegisteredNumber() == this.getRegisteredNumber();
    }
}