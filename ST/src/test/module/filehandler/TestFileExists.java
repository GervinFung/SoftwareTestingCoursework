package test.module.filehandler;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public final class TestFileExists {

    //Test case 001, test all file path exists, to ensure that if application code read file has problem
    //it's due to file content problem, not whether the file exists or not

    private static final String ITEM_TXT = "textFiles/item.txt";
    private final static String MEMBER_FILE_NAME = "textFiles/member.txt";
    private static final String DELIVERY_RATE_TXT = "textFiles/deliveryRate.txt";

    @Test
    public void testFileExists() {
        assertTrue(new File(ITEM_TXT).exists());
        assertTrue(new File(MEMBER_FILE_NAME).exists());
        assertTrue(new File(DELIVERY_RATE_TXT).exists());
    }
}