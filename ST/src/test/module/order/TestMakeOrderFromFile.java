package test.module.order;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import main.backend.FileDataInvalidException;
import main.backend.member.Address;
import main.backend.member.Customer;
import main.backend.member.Member;
import main.backend.order.Order;
import main.backend.order.OrderCartMap;
import main.backend.Controller;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(JUnitParamsRunner.class)
public final class TestMakeOrderFromFile {
    
    
        //1. Customer ordered 3 different non-promotional item, each with same quantity, and total price < RM25
        //2. Customer ordered 3 different non-promotional item, each with different quantity, and total price > RM25
        //3. Customer ordered 2 different non-promotional item, each with same quantity, and total price < RM25
        //4. Customer ordered 2 different non-promotional item, each with same quantity, and total price < RM25
    
        //5. Customer ordered 3 different promotional item, each with different quantity, and total price > RM25
        //6. Customer ordered 2 different promotional and non-promotional item, each with same quantity, and total price > 25
        //7. Customer ordered 1 different promotional and 2 non-promotional item, each with same quantity, total price > 25
    
        //8. Customer ordered 3 different promotional and non-promotional item, each with different quantity, and total price > 25
        //9. Customer ordered 10 different non-promotional item, each with same quantity, and total price > 25
        //10. Customer ordered all 5 different promotional item, each with different quantity, and total price > RM25
    
        //11. Member ordered all 5 different promotional item, each with different quantity, and total price > RM25
        //12. Member ordered 10 different non-promotional item, each with same quantity, and total price > 25
        //13. Member ordered 2 different promotional and 3 non-promotional item, each with different quantity, total price > 25
    
        //14. Member ordered 1 different promotional and 2 non-promotional item, each with different quantity, total price > 25
        //15. Member ordered 2 different promotional and non-promotional item, each with same quantity, and total price > 25
    
        //16. Member ordered 3 different non-promotional item, each with same quantity, and total price < RM25
        //17. Member ordered 3 different non-promotional item, each with same quantity, and total price > RM25
    
        //18. Member ordered 3 different non-promotional item, each with different quantity, and total price > RM25
        //19. Member ordered 2 different non-promotional item, each with same quantity, and total price < RM25
        //20. Member ordered 2 different non-promotional item, each with same quantity, and total price < RM25


    private final static String MAKE_ORDER_DATA_TXT = "textFiles/testFileData/testMakeOrderData.txt";
    private final static String EXPECTED_ORDER_RESULT_TXT = "textFiles/testFileData/testMakeOrderExpectedResult.txt";

    private static Controller CONTROLLER;
    private static Member MEMBER;
    private static Customer CUSTOMER;
    private static List<OrderCartMap> ORDER_CART_MAP_LIST;
    private static int HALF_SIZE, INDEX;

    @BeforeClass
    public static void setup() {
        CONTROLLER = new Controller();
        ORDER_CART_MAP_LIST = readOrderFromFile();
        final Address ADDRESS = mock(Address.class);
        MEMBER = new Member(null, null, 0, ADDRESS, null);
        CUSTOMER = new Customer(null, 0, ADDRESS);
        HALF_SIZE = ORDER_CART_MAP_LIST.size() / 2;
        INDEX = 0;
    }

    private static List<OrderCartMap> readOrderFromFile() {
        final List<OrderCartMap> orderCartMapList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(MAKE_ORDER_DATA_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final OrderCartMap orderItem = new OrderCartMap();
                final String[] rawData = line.split("/");
                for (final String data : rawData) {
                    final String[] cleanData = data.split(",");
                    final int itemID = Integer.parseInt(cleanData[0]);
                    final int quantityOrder = Integer.parseInt(cleanData[1]);
                    orderItem.put(CONTROLLER.searchItemByID(itemID), quantityOrder);
                }
                orderCartMapList.add(orderItem);
            }

            reader.close();
            return Collections.unmodifiableList(orderCartMapList);

        } catch (final IOException e) { throw new FileDataInvalidException("Error reading " + MAKE_ORDER_DATA_TXT); }
    }


    private Object[] readExpectedResultFromFile() {
        final List<Object> objectList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(EXPECTED_ORDER_RESULT_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {

                final String[] rawData = line.split("/");

                final String area = rawData[0];
                final int numberOfDifferentItems = Integer.parseInt(rawData[1]);
                final float deliveryCharge = Float.parseFloat(rawData[2]);
                final float totalPrice = Float.parseFloat(rawData[3]);
                final boolean hasAdditionalCharge = Boolean.parseBoolean(rawData[4]);

                final Object[] object = new Object[] {
                        area,
                        numberOfDifferentItems,
                        deliveryCharge,
                        totalPrice,
                        hasAdditionalCharge
                };
                objectList.add(object);
            }

            reader.close();
            return objectList.toArray();

        } catch (final IOException e) { throw new FileDataInvalidException("Error reading " + EXPECTED_ORDER_RESULT_TXT); }
    }



    @Test
    @Parameters(method = "readExpectedResultFromFile")
    public void testMakeOrder(final String area,
                              final int numberOfDifferentItems,
                              final float deliveryCharge,
                              final float totalPrice,
                              final boolean hasAdditionalCharge) {
        final OrderCartMap orderCartMap = ORDER_CART_MAP_LIST.get(INDEX);

        final Customer testCustomer = INDEX < HALF_SIZE ? CUSTOMER : MEMBER;

        when(testCustomer.getAddress().getArea()).thenReturn(area);

        final Order order = new Order(orderCartMap, testCustomer, Order.ORDER_STATUS.SUCCESSFUL, CONTROLLER.getAreaDeliveryRateMap().getAreaDeliveryRateMap());

        assertEquals(numberOfDifferentItems, order.getNumberOfDifferentItemsOrdered());

        assertEquals(deliveryCharge, order.getDeliveryCharge(), 0f);

        assertEquals(totalPrice, order.getTotalPrice(), 0f);

        assertSame(hasAdditionalCharge, order.hasAdditionalCharge());

        final float expectedTotalOrderPrice = totalPrice + deliveryCharge;

        assertEquals(expectedTotalOrderPrice, order.getTotalPriceAddDeliveryCharge(), 0f);

        //TEST to check if order status is really successful
        assertSame(Order.ORDER_STATUS.SUCCESSFUL, order.getOrderStatus());

        INDEX++;
    }
}