package test.module.customer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.DataValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

@RunWith(JUnitParamsRunner.class)
public final class TestNumericValidation {

    private String[] getInvalidNumberInput() {
        return new String[] {
                "123abc"    //Test case 001, input contains alphabet
                ,""        //Test case 002, input is empty string
                ," "       //Test case 003, input is white space
                ,"123!@#%"//Test case 004, input contains symbols
        };
    }


    @Test
    @Parameters(method = "getInvalidNumberInput")
    public void testInvalidIntegerInput(final String data) {
        assertFalse(DataValidation.tryParseIntInRange(data, -100, 200));
        assertFalse(DataValidation.tryParseFloatInRange(data, -100, 200));
        assertFalse(DataValidation.tryParseLongInRange(data, -100, 200));
    }


    //equivalence partition test

    //test integer input ONLY
    private Object[] getTestIntegerData() {
        return new Object[] {
                new Object[] {"-101", false}, //Test case 001, parsable but less than minimum value of range
                new Object[] {"201", false},  //Test case 002, parsable but larger than maximum value of range
                new Object[] {"123", true}    //Test case 003, parsable and within range
        };
    }

    @Test
    @Parameters(method = "getTestIntegerData")
    public void testValidIntegerInput(final String data, final boolean actual) {
        assertSame(DataValidation.tryParseIntInRange(data, -100, 200), actual);
    }

    //test float input ONLY
    private Object[] getTestFloatData() {
        return new Object[] {
                new Object[] {"99.9", false},  //Test case 001, parsable but less than minimum value of range
                new Object[] {"300.1", false}, //Test case 002, parsable but larger than maximum value of range
                new Object[] {"200.5", true}   //Test case 003, parsable and within range
        };
    }

    @Test
    @Parameters(method = "getTestFloatData")
    public void testValidFloatInput(final String data, final boolean actual) {
        assertSame(DataValidation.tryParseFloatInRange(data, 100f, 300f), actual);
    }

    //test long input ONLY
    private Object[] getTestLongData() {
        return new Object[] {
                new Object[] {"123412341234", false}, //Test case 001, parsable but less than minimum value of range
                new Object[] {"123412341241", false}, //Test case 002, parsable but larger than maximum value of range
                new Object[] {"123412341236", true}   //Test case 003, parsable and within range
        };
    }

    @Test
    @Parameters(method = "getTestLongData")
    public void testValidLongInput(final String data, final boolean actual) {
        assertSame(DataValidation.tryParseLongInRange(data, 123412341235L, 123412341240L), actual);
    }
}