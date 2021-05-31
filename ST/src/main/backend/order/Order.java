package main.backend.order;

import main.backend.item.Item;
import main.backend.member.Member;
import main.backend.member.Customer;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;

public class Order {

    private static final int ADDITIONAL_CHARGE = 3;

    private final int numberOfItemsOrdered;
    private final long orderID;
    private final Map<Item, Integer> itemsOrdered;
    private final float deliveryCharge;
    private final float totalItemPrice;
    private ORDER_STATUS orderStatus;
    private final boolean hasAdditionalCharge;
    private final Customer customer;

    // create immutable object to reduce bug occurrence
    public Order(final OrderCartMap itemsOrdered, final Customer customer, final ORDER_STATUS orderStatus, final Map<String, Float> areaDeliveryRateMap) {
        if (itemsOrdered == null || itemsOrdered.isEmpty()) {
            throw new IllegalArgumentException("Order cart cannot be empty/null");
        }
        this.itemsOrdered = Collections.unmodifiableMap(itemsOrdered);
        this.customer = customer;
        //will throw NullPointerException is area is not in the Map
        this.deliveryCharge = areaDeliveryRateMap.get(customer.getAddress().getArea().toLowerCase());

        final float totalPrice = this.generateTotalItemPrice(customer);
        this.hasAdditionalCharge = totalPrice < 25;
        this.totalItemPrice = totalPrice + (this.hasAdditionalCharge ? ADDITIONAL_CHARGE : 0);

        this.orderStatus = orderStatus;
        this.orderID = new Timestamp(System.currentTimeMillis()).getTime();

        this.numberOfItemsOrdered = this.itemsOrdered.size();
    }

    public void changePendingToSuccess() {
        if (this.orderStatus == ORDER_STATUS.SUCCESSFUL) {
            throw new IllegalStateException("Order Status is already successful!");
        }
        this.orderStatus = ORDER_STATUS.SUCCESSFUL;
    }

    @Override
    public int hashCode() {
        int orderHashCode = this.customer.getRegisteredNumber() * 31;
        orderHashCode += this.itemsOrdered.keySet().hashCode() * 31;
        orderHashCode += this.orderStatus == ORDER_STATUS.SUCCESSFUL ? 31 : 0;
        return orderHashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof Order)) { return false; }

        // by the time it reach here, obj is instance of order
        return obj.hashCode() == this.hashCode();
    }

    //getter
    public float getTotalPrice() { return totalItemPrice; }
    public float getTotalPriceAddDeliveryCharge() { return this.totalItemPrice + this.deliveryCharge; }
    public boolean hasAdditionalCharge() { return this.hasAdditionalCharge; }
    public float getDeliveryCharge() { return this.deliveryCharge; }
    public long getOrderID() { return this.orderID; }
    public int getNumberOfDifferentItemsOrdered() { return this.numberOfItemsOrdered; }
    public ORDER_STATUS getOrderStatus() { return this.orderStatus; }
    public Map<Item, Integer> getItemsOrdered() { return this.itemsOrdered; }
    public int getRegisteredNumber() { return this.customer.getRegisteredNumber(); }

    private float generateTotalItemPrice(final Customer customer) {

        float totalPrice = 0f;
        for (final Map.Entry<Item, Integer> entryItem : this.itemsOrdered.entrySet()) {
            final float totalItemPrice;

            final float itemPrice = customer.isMember() ? entryItem.getKey().getMemberPrice() : entryItem.getKey().getNonMemberPrice();

            totalItemPrice = itemPrice * entryItem.getValue();

            totalPrice += totalItemPrice * entryItem.getKey().getPromotional().getDiscountRatio();
        }

        totalPrice = (float) ((float)Math.round(totalPrice * 100.0) / 100.0);

        return totalPrice;
    }

    @Override
    public String toString() {
        if (this.customer.isMember()) {
            final Member member = (Member)this.customer;
            return "User Details\n" + "-".repeat(40)
                    + "\nName             : " + member.getName()
                    + "\nRegistered Number: " + member.getRegisteredNumber()
                    + "\nContact          : " + member.getContactNumber()
                    + "\n" + member.getAddress();
        } else {
            return "User Details\n" + "-".repeat(40)
                    + "\nName             : " + this.customer.getName()
                    + "\nRegistered Number: " + this.customer.getRegisteredNumber()
                    + "\n" + this.customer.getAddress();
        }
    }

    public enum ORDER_STATUS {SUCCESSFUL, PENDING}
}