package test.module.order;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.order.Order;
import main.backend.DataValidation;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnitParamsRunner.class)
public final class TestOrderStatusFromString {

    private final static DataValidation DATA_VALIDATION = new DataValidation();

    private Object[] getOrderStatusValidInput() {
        return new Object[] {
            new Object[] {Order.ORDER_STATUS.SUCCESSFUL, "N"},//Test Case 001, enter N
            new Object[] {Order.ORDER_STATUS.SUCCESSFUL, "n"},//Test Case 002, enter n
            new Object[] {Order.ORDER_STATUS.PENDING, "L"},   //Test Case 003, enter L
            new Object[] {Order.ORDER_STATUS.PENDING, "l"}    //Test Case 004, enter l
        };
    }


    @Test
    @Parameters(method = "getOrderStatusValidInput")
    public void testOrderStatusFromValidInput(final Order.ORDER_STATUS order_status, final String input) {
        assertSame(DATA_VALIDATION.getOrderStatus(input), order_status);
    }


    private String[] getOrderStatusInvalidInput() {
        return new String[] {
                "",    //Test Case 005, empty string
                " ",   //Test Case 006, white space
                "abc", //Test Case 007, input contains alphabets
                "123", //Test Case 008, input contains numbers
                "!@#$%"//Test Case 009, input contains symbols
        };
    }


    @Test
    @Parameters(method = "getOrderStatusInvalidInput")
    public void testOrderStatusFromInvalidInput(final String input) {
        assertNull(DATA_VALIDATION.getOrderStatus(input));
    }
}