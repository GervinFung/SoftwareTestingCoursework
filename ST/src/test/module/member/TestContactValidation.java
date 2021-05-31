package test.module.member;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.DataValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public final class TestContactValidation {

    private final static DataValidation DATA_VALIDATION = new DataValidation();


    //Eg.
    //011 5548 4654, in this case will be 1 5548 4654, as we provided 01
    //012 3456 789, in this case will be 2 3456 789, as we provided 01
    //011 will have extra 1 digit


    //Test case 006, String follow the format of contact of Malaysia, given 01 is provided
    private String[] getValidContact() { return new String[] {"155484654", "23456789", "98217632", "03456789"}; }

    @Test
    @Parameters(method = "getValidContact")
    public void testValidContact(final String contact) { assertTrue(DATA_VALIDATION.isContactCorrect(contact)); }

    private String[] getInvalidContact() {
        return new String[] {
                "9870123546", //Test case 001, input contains number, and will return false
                "abcdef",     //Test case 002, input contains symbols, will return false
                "!@#$%",      //Test case 003, input contains alphabet and space, but not among the 20 areas listed, will return false
                " ",          //Test case 004, input contains white space, will return false
                ""            //Test case 005, input contains empty string, will return false
        };
    }

    @Test
    @Parameters(method = "getInvalidContact")
    public void testInvalidContact(final String contact) { assertFalse(DATA_VALIDATION.isContactCorrect(contact)); }
}