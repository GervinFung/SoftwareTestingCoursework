package test.module.customer;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.order.AreaDeliveryRateMap;
import main.backend.DataValidation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(JUnitParamsRunner.class)
public final class TestAreaValidation {

    private final static DataValidation DATA_VALIDATION = new DataValidation();
    private final static AreaDeliveryRateMap AREA_DELIVERY_RATE_MAP = new AreaDeliveryRateMap();


    //Test case 006, input is among the 20 areas listed
    private String[] getValidArea() { return new String[] {"Alor Gajah", "Asahan", "Bukit Beruang", "Kuala Sungai Baru", "Merlimau"}; }

    @Test
    @Parameters(method = "getValidArea")
    public void testValidArea(final String area) { assertTrue(DATA_VALIDATION.isAreaCorrect(area, AREA_DELIVERY_RATE_MAP.getAreaDeliveryRateMap())); }

    private String[] getInvalidArea() {
        return new String[] {
                "Alor Gajah123",    //Test case 001, input contains number
                "Alor Gajah !@#$%", //Test case 002, input contains symbols
                "Area Sungai Long", //Test case 003, input contains alphabet and space, but not among the 20 areas listed
                " ",                //Test case 004, input contains white space
                ""                  //Test case 005, input contains empty string
        };
    }

    @Test
    @Parameters(method = "getInvalidArea")
    public void testInvalidArea(final String area) { assertFalse(DATA_VALIDATION.isAreaCorrect(area, AREA_DELIVERY_RATE_MAP.getAreaDeliveryRateMap())); }
}