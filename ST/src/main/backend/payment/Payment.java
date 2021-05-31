package main.backend.payment;

import main.backend.order.Order;

import java.util.Arrays;

public class Payment {

    public final static String SUCCESSFUL = "Paid & Ready for Delivery";
    public final static String FAIL = "Pending for Payment";

    private final PAYMENT_TYPE payment_type;
    private final Order order;
    private final long accountNumber;
    private boolean isSuccess;

    // create immutable object to reduce bug occurrence
    public Payment(final Order order, final PAYMENT_TYPE payment_type, final long accountNumber) {
        if (order.getOrderStatus() == Order.ORDER_STATUS.PENDING) {
            throw new IllegalArgumentException("Payment should be created ONLY when order status is SUCCESSFUL!");
        }
        if (Arrays.stream(PAYMENT_TYPE.values()).noneMatch(payment_type1 -> payment_type1 == payment_type)) {
            throw new IllegalArgumentException("Payment Type Invalid!");
        }
        this.isSuccess = true;
        this.order = order;
        this.payment_type = payment_type;
        this.accountNumber = accountNumber;
    }

    public void setSuccess(final boolean success) {
        this.isSuccess = success;
    }


    public void generatePaymentReceiptToCustomerAddress() {
        throw new UnsupportedOperationException("Not ready for implementation");
    }

    public String getPaymentMessage() {
        return this.isSuccess ? SUCCESSFUL : FAIL;
    }

    public enum PAYMENT_TYPE {
        CREDIT_CARD {
            @Override
            public String toString() {
                return "Credit Card";
            }

        }, ONLINE_PAYMENT {
            @Override
            public String toString() { return "Online Payment"; }
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }


    @Override
    public String toString() {
        return this.order.getOrderID() + "/" + this.accountNumber;
    }

    public String receipt() {
        return "\n" + this.order.toString() + this.getPaymentMessage() + "\n\n" + this.generatePaymentReceipt();
    }

    private String generatePaymentReceipt() {
        return "Payment Details\n" + "-".repeat(40)
                + "\nOrder ID            :" + this.order.getOrderID()
                + "\nTotal Price(RM)     :" + String.format("%.02f", this.order.getTotalPriceAddDeliveryCharge())
                + "\nDelivery Charge(RM) :" + String.format("%.02f", this.order.getDeliveryCharge())
                + "\nPayment Method      :" + this.payment_type.toString() + "(" + this.accountNumber + ")"
                + "\nPayment Status      :" + this.order.getOrderStatus().toString();
    }
}