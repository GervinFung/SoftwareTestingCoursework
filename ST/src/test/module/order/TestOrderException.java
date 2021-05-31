package test.module.order;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.member.Address;
import main.backend.member.Customer;
import main.backend.member.Member;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;
import main.backend.Controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)

public final class TestOrderException {

    //BeforeClass dont work well with parameterised test, so initialise the variable this way for once
    private static final Address ADDRESS = mock(Address.class);
    private final static Controller CONTROLLER = new Controller();
    private final Member member = new Member(null, null, 0, ADDRESS, null);
    private final Customer customer = new Customer(null, 0, ADDRESS);



    private Object[] getInvalidOrderArea() {
        final OrderCartMap orderItem = new OrderCartMap();

        orderItem.put(CONTROLLER.searchItemByID(14), 1);
        orderItem.put(CONTROLLER.searchItemByID(15), 1);
        orderItem.put(CONTROLLER.searchItemByID(16), 1);

        return new Object[] {
                //test customer invalid area
                new Object[] {this.customer, orderItem, ""},       //Test Case 001, enter empty string
                new Object[] {this.customer, orderItem, "123456"}, //Test case 002, enter numbers
                new Object[] {this.customer, orderItem, "!@$%"},   //Test case 003, enter symbol

                //test member invalid area
                new Object[] {this.member, orderItem, "abcdefg"},          //Test Case 004, enter random alphabet
                new Object[] {this.member, orderItem, "Ujong Pasir!@#$"},  //Test case 005, enter Ujong Pasir!@#$
        };
    }

    @Test(expected = NullPointerException.class)
    @Parameters(method = "getInvalidOrderArea")
    public void testCustomerInvalidOrder(final Customer customer, final OrderCartMap orderItem, final String area) {
        when(customer.getAddress().getArea()).thenReturn(area);
        customer.createOrder(orderItem, CONTROLLER.getAreaDeliveryRateMap());
    }


    private Integer[] getOrderInvalidIDData() {
        // ID range from 1 to 20 only
        return new Integer[] {
                -1,  //Test case 006, enter -1
                21   //Test case 007, enter 21
        };
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getOrderInvalidIDData")
    public void testInvalidIDData(final int ID) {
        new OrderCartMap().put(CONTROLLER.searchItemByID(ID), 1);
    }




    private Object[] getEmptyOrNullOrder() {

        return new Object[] {
                new Object[] {this.customer, new OrderCartMap()}, //Test case 008, enter customer object, and empty Order
                new Object[] {this.customer, null},               //Test case 009, enter customer object, and null Order
                new Object[] {this.member, new OrderCartMap()},   //Test case 010, enter member object, and empty Order
                new Object[] {this.member, null},                 //Test case 011, enter member object, and null Order
        };
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "getEmptyOrNullOrder")
    public void testInvalidOrderCart(final Customer customer, final OrderCartMap itemsOrdered) {
        when(customer.getAddress().getArea()).thenReturn("Alor Gajah");
        customer.createOrder(itemsOrdered, CONTROLLER.getAreaDeliveryRateMap());
    }




    //Test case 012, choose random item, enter 0 quantity
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuantity() {
        new OrderCartMap().put(CONTROLLER.searchItemByID(14), 0);
    }

    private Object[] testChangePendingToSuccess() {
        final OrderCartMap orderItem = new OrderCartMap();
        orderItem.put(CONTROLLER.searchItemByID(14), 1);
        return new Object[] {orderItem, "Alor Gajah"};
    }

    //Test case 013, choose random item, enter 1 quantity, enter order status as SUCCESSFUL, call method that change order status to successful
    @Test(expected = IllegalStateException.class)
    @Parameters(method = "testChangePendingToSuccess")
    public void testSuccessOrderIllegalStateException(final OrderCartMap orderCartMap, final String area) {
        when(this.member.getAddress().getArea()).thenReturn(area);
        final Order order = this.member.createOrder(orderCartMap, Order.ORDER_STATUS.SUCCESSFUL, CONTROLLER.getAreaDeliveryRateMap());
        //order is successful, should throw error when call again
        order.changePendingToSuccess();
    }

    //Test case 014, choose random item, enter 1 quantity, enter order status as PENDING, call method that change order status to successful
    @Test()
    @Parameters(method = "testChangePendingToSuccess")
    public void testPendingOrderNoIllegalStateException(final OrderCartMap orderCartMap, final String area) {
        when(this.member.getAddress().getArea()).thenReturn(area);
        final Order order = this.member.createOrder(orderCartMap, Order.ORDER_STATUS.PENDING, CONTROLLER.getAreaDeliveryRateMap());
        //order is PENDING, should not throw error
        order.changePendingToSuccess();
    }
}