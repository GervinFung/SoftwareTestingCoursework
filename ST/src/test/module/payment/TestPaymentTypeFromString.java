package test.module.payment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.payment.Payment;
import main.backend.DataValidation;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnitParamsRunner.class)
public final class TestPaymentTypeFromString {

    private final static DataValidation DATA_VALIDATION = new DataValidation();

    private Object[] getPaymentMethodValidInput() {
        return new Object[] {
                new Object[] {Payment.PAYMENT_TYPE.ONLINE_PAYMENT, "O"},//Test Case 001, enter O
                new Object[] {Payment.PAYMENT_TYPE.ONLINE_PAYMENT, "o"},//Test Case 002, enter o
                new Object[] {Payment.PAYMENT_TYPE.CREDIT_CARD, "C"},   //Test Case 003, enter C
                new Object[] {Payment.PAYMENT_TYPE.CREDIT_CARD, "c"}    //Test Case 004, enter c
        };
    }


    @Test
    @Parameters(method = "getPaymentMethodValidInput")
    public void testPaymentMethodFromValidInput(final Payment.PAYMENT_TYPE payment_type, final String input) {
        assertSame(DATA_VALIDATION.getPaymentType(input), payment_type);
    }


    private String[] getPaymentMethodInvalidInput() {
        return new String[] {
                "",    //Test Case 005, empty string
                " ",   //Test Case 006, white space
                "abc", //Test Case 007, input contains alphabets
                "123", //Test Case 008, input contains numbers
                "!@#$%"//Test Case 009, input contains symbols
        };
    }

    @Test
    @Parameters(method = "getPaymentMethodInvalidInput")
    public void testPaymentMethodFromInvalidInput(final String input) {
        assertNull(DATA_VALIDATION.getPaymentType(input));
    }
}
