package test.module.customer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.DataValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public final class TestStringValidation {

    private final static DataValidation DATA_VALIDATION = new DataValidation();

    @Test
    @Parameters({
            "",          //Test case 001, empty string
            "RegEx !@#", //Test case 003, contains symbol
            "RegEx 123", //Test case 004, contain number
            " "          //Test case 002, String with contains white space
    })
    public void testInvalid(final String input) {
        assertFalse(DATA_VALIDATION.isProperStringFormat(input));
    }

    //Test case 005, String with no symbol/number, begin with alphabet
    public void testValid() { //Test case 005, string with no symbol/number, begin with alphabet
        assertTrue(DATA_VALIDATION.isProperStringFormat("Hello World"));
    }
}