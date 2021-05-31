package test.module.order;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import main.backend.order.OrderCartMap;
import main.backend.Controller;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertSame;

@RunWith(JUnitParamsRunner.class)
public final class TestOrderSameItem {

    private final static Controller CONTROLLER = new Controller();

    private Object[] getSameItemOrderedData() {
        //Test case 001, order first item with quantity 10, for 4 times
        final OrderCartMap itemsOrdered1 = new OrderCartMap();
        itemsOrdered1.put(CONTROLLER.searchItemByID(1), 10);
        itemsOrdered1.put(CONTROLLER.searchItemByID(1), 10);
        itemsOrdered1.put(CONTROLLER.searchItemByID(1), 10);
        itemsOrdered1.put(CONTROLLER.searchItemByID(1), 10);

        //Test case 002, order first and second item with quantity 1 for 2 times
        final OrderCartMap itemsOrdered2 = new OrderCartMap();
        itemsOrdered2.put(CONTROLLER.searchItemByID(1), 1);
        itemsOrdered2.put(CONTROLLER.searchItemByID(2), 1);
        itemsOrdered2.put(CONTROLLER.searchItemByID(1), 1);
        itemsOrdered2.put(CONTROLLER.searchItemByID(2), 1);

        //Test case 002, order 3 item with quantity 2 for 2 times
        final OrderCartMap itemsOrdered3 = new OrderCartMap();
        itemsOrdered3.put(CONTROLLER.searchItemByID(1), 2);
        itemsOrdered3.put(CONTROLLER.searchItemByID(2), 2);
        itemsOrdered3.put(CONTROLLER.searchItemByID(3), 2);
        itemsOrdered3.put(CONTROLLER.searchItemByID(1), 2);
        itemsOrdered3.put(CONTROLLER.searchItemByID(2), 2);
        itemsOrdered3.put(CONTROLLER.searchItemByID(3), 2);

        return new Object[] {
            new Object[] {itemsOrdered1, 1, 40},
            new Object[] {itemsOrdered2, 2, 4},
            new Object[] {itemsOrdered3, 3, 12}
        };
    }

    @Test
    @Parameters(method = "getSameItemOrderedData")
    public void testOrderSameItem(final OrderCartMap itemsOrdered, final int expectedSize, final int expectedTotalItemsOrdered) {
        assertSame(expectedSize, itemsOrdered.size());

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        itemsOrdered.values().forEach(atomicInteger::getAndAdd);

        final int actualTotalItemsOrdered = atomicInteger.get();

        assertSame(expectedTotalItemsOrdered, actualTotalItemsOrdered);

        assertSame(expectedSize, itemsOrdered.size());
    }
}