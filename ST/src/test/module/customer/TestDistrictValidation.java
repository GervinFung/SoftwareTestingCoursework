package test.module.customer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.DataValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnitParamsRunner.class)
public final class TestDistrictValidation {

    private final static DataValidation DATA_VALIDATION = new DataValidation();

    //Test case 006, input contains alphabet and space, and is among the 3 districts listed
    private String[] getValidDistrict() { return new String[] {"alor gajah", "Alor Gajah", "melaka tengah", "Central Melaka", "JASIN"}; }

    @Test
    @Parameters(method = "getValidDistrict")
    public void testValidDistrict(final String district) { assertTrue(DATA_VALIDATION.isDistrictCorrect(district)); }

    private String[] getInvalidDistrict() {
        return new String[] {
                "melaka tengah123",       //Test case 001, District input contains number
                "melaka tengah !@#$%",    //Test case 002, District input contains symbols
                "District melaka tengah", //Test case 003, District input contains alphabet and space, but not among the 3 districts listed
                " ",                      //Test case 004, District input contains white space
                ""                        //Test case 005, District input contains empty string
        };
    }

    @Test
    @Parameters(method = "getInvalidDistrict")
    public void testInvalidDistrict(final String district) { assertFalse(DATA_VALIDATION.isDistrictCorrect(district)); }
}