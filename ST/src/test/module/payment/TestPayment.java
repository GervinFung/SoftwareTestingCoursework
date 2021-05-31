package test.module.payment;

import main.backend.order.Order;
import main.backend.payment.Payment;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TestPayment {

    //Test case 001, make payment with successful order status

    private final static Order MOCK_ORDER = Mockito.mock(Order.class);

    @Test
    public void testSuccessfulOrderStatus() {
        Mockito.when(MOCK_ORDER.getOrderStatus()).thenReturn(Order.ORDER_STATUS.SUCCESSFUL);
        new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.ONLINE_PAYMENT, 0);
    }

    //Test case 002, make payment with pending order status

    @Test(expected = IllegalArgumentException.class)
    public void testPendingOrderStatus() {
        Mockito.when(MOCK_ORDER.getOrderStatus()).thenReturn(Order.ORDER_STATUS.PENDING);
        new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.ONLINE_PAYMENT, 0);
    }



    @Test
    public void testValidPaymentType() {
        //Test case 003, make payment with ONLINE_PAYMENT paymentType
        Mockito.when(MOCK_ORDER.getOrderStatus()).thenReturn(Order.ORDER_STATUS.SUCCESSFUL);
        new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.ONLINE_PAYMENT, 0);

        //Test case 004, make payment with CREDIT_CARD paymentType
        Mockito.when(MOCK_ORDER.getOrderStatus()).thenReturn(Order.ORDER_STATUS.SUCCESSFUL);
        new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.CREDIT_CARD, 0);
    }


    //Test case 005, make payment with INVALID paymentType

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPaymentType() {
        Mockito.when(MOCK_ORDER.getOrderStatus()).thenReturn(Order.ORDER_STATUS.SUCCESSFUL);
        new Payment(MOCK_ORDER, null, 1L);
    }



    @Test
    public void testGetCorrespondingMessage() {
        final Payment payment = Mockito.spy(new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.CREDIT_CARD, 0));

        //Test case 006, test payment is successful, will return success message
        assertTrue(payment.isSuccess());
        assertEquals(Payment.SUCCESSFUL, payment.getPaymentMessage());

        //Test case 007, test payment is failed, will return fail message
        payment.setSuccess(false);
        assertEquals(Payment.FAIL, payment.getPaymentMessage());
    }


    //Test case 008, test calling method of payment  that is not yet for implementation
    @Test(expected = UnsupportedOperationException.class)
    public void testCallingUnimplementedMethod() {
        final Payment payment = Mockito.spy(new Payment(MOCK_ORDER, Payment.PAYMENT_TYPE.CREDIT_CARD, 0));
        payment.generatePaymentReceiptToCustomerAddress();
    }
}