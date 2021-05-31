package main.backend.member;

import main.backend.order.AreaDeliveryRateMap;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;

public final class Member extends Customer {

    private final String phoneNumber, password;

    // create immutable object to reduce bug occurrence
    public Member(final String name, final String password, final int registeredNumber, final Address address, final String phoneNumber) {
        super(name, registeredNumber, address);
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getContactNumber() { return this.phoneNumber; }
    public String getPassword() { return this.password; }

    @Override
    public boolean isMember() { return true; }

    public Order createOrder(final OrderCartMap itemsOrdered, final Order.ORDER_STATUS orderStatus, final AreaDeliveryRateMap areaDeliveryRateMap) {
        return super.makeOrder(itemsOrdered, orderStatus, areaDeliveryRateMap);
    }

    public String memberFormatToFile() {
        return this.getRegisteredNumber() + "/" + this.getName() + "/" + this.getPassword() + "/" + this.getAddress().addressFormatToFile() + "/" + this.phoneNumber;
    }
}