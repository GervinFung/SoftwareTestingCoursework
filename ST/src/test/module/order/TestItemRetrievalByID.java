package test.module.order;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import main.backend.FileDataInvalidException;
import main.backend.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnitParamsRunner.class)
public final class TestItemRetrievalByID {

    private final static String ITEM_RETRIEVAL_TXT = "textFiles/testFileData/testItemRetrieval.txt";

    private final static Controller CONTROLLER = new Controller();

    //this test apply equivalence partition test, since 1-20 is valid ID, 0 and 21 will be invalid
    //reading all 20 ID is to ensure that the ID is indeed 1-20

    //test case 001, read all 20 item index of list(0-19) and valid item ID(1-20) from file
    private Object[] getItemByIndexAndID() {
        final List<Object> itemList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(ITEM_RETRIEVAL_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split(",");
                final int index = Integer.parseInt(data[0]);
                final int ItemID = Integer.parseInt(data[1]);
                final Object[] object = new Object[] {index, ItemID};
                itemList.add(object);
            }

            reader.close();
            return itemList.toArray();

        } catch (final IOException e) { throw new FileDataInvalidException("Error reading " + ITEM_RETRIEVAL_TXT); }
    }

    @Test
    @Parameters(method = "getItemByIndexAndID")
    public void testValidSearchItemByID(final int index, final int id) { assertSame(CONTROLLER.searchItemByID(id), CONTROLLER.getItemsRecord().getDataList().get(index)); }


    private Integer[] getItemByInvalidID() {
        return new Integer[] {
            0,      //test case 002, test 0, which is smaller than 1, the minimum item ID
            21      //test case 003, test 21, which is larger than 20, the maximum item ID
        };
    }

    @Test
    @Parameters(method = "getItemByInvalidID")
    public void testSearchInvalidID(final int id) {
        assertNull(CONTROLLER.searchItemByID(id));
    }
}