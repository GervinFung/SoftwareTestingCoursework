package test;

import main.backend.Controller;
import main.backend.member.Address;
import main.backend.member.Member;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;
import main.backend.payment.Payment;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class IntegrationTest {

    @Test
    public void testIntegrationBetweenMemberAndOrder() {
        final Controller controller = new Controller();
        final Address address = new Address("Melaka", "Melaka Tengah", "Asahan", "Taman Tensan 2", 31910,  102);
        final Member member = new Member("Tieu Zi Yee", "123456", 180000, address, "01155484654");

        final OrderCartMap itemsOrdered = new OrderCartMap();
        itemsOrdered.put(controller.searchItemByID(1), 10);
        itemsOrdered.put(controller.searchItemByID(2), 10);
        itemsOrdered.put(controller.searchItemByID(3), 10);
        itemsOrdered.put(controller.searchItemByID(4), 10);

        final Order expectedOrder = new Order(itemsOrdered, member, Order.ORDER_STATUS.SUCCESSFUL, controller.getAreaDeliveryRateMap().getAreaDeliveryRateMap());
        final Order actualOrder = member.createOrder(itemsOrdered, controller.getAreaDeliveryRateMap());

        final int numberOfDifferentItems = 4;
        final float deliveryCharge = 4f, totalPrice = 3405f;
        final boolean hasAdditionalCharge = false;

        //TEST if 2 orders are equal to each other
        assertEquals(expectedOrder, actualOrder);

        assertEquals(numberOfDifferentItems, actualOrder.getNumberOfDifferentItemsOrdered());

        assertEquals(deliveryCharge, actualOrder.getDeliveryCharge(), 0f);

        assertEquals(totalPrice, actualOrder.getTotalPrice(), 0f);

        assertSame(hasAdditionalCharge, actualOrder.hasAdditionalCharge());

        final float expectedTotalOrderPrice = totalPrice + deliveryCharge;

        assertEquals(expectedTotalOrderPrice, actualOrder.getTotalPriceAddDeliveryCharge(), 0f);

        //TEST to check if order status is really successful
        assertSame(Order.ORDER_STATUS.SUCCESSFUL, actualOrder.getOrderStatus());

        final Payment payment = member.createPayment(actualOrder, Payment.PAYMENT_TYPE.ONLINE_PAYMENT, 1234123412L);
        assertTrue(payment.isSuccess());
        assertEquals(Payment.SUCCESSFUL, payment.getPaymentMessage());
    }
}
