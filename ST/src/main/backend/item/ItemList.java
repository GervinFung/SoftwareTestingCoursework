package main.backend.item;

import main.backend.FileDataInvalidException;
import main.backend.fileinterface.MyFileAccessor;
import main.backend.DataValidation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ItemList implements MyFileAccessor<Item> {

    private static final String ITEM_TXT = "textFiles/item.txt";
    private static final String ERROR_MESSAGE =
            "\n\nFile structure should be the following\n" +
            "Number/Name of Cake/Pastry/P(pastry OR C(cake)/member price/non-member price/is it promotional(false - no, true - yes)\n" +
            "1/Butter cake/C/46.00/46.80/false\n" +
            "2/Marble cake/C/76.50/77.20/false\n" + "and so on, where number is POSITIVE integer and prices is has decimal point\n";

    // create immutable list to reduce bug occurrence
    private final List<Item> itemList;

    public ItemList() { this.itemList = this.readFromFile(); }

    private int ID(final String ID) {
        if (DataValidation.tryParseIntInRange(ID, 0, Integer.MAX_VALUE)) {
            return Integer.parseInt(ID);
        } throw new FileDataInvalidException(ERROR_MESSAGE);
    }

    private float price(final String price) {
        if (DataValidation.tryParseFloatInRange(price, 0.01f, Float.MAX_VALUE)) {
            return Float.parseFloat(price);
        } throw new FileDataInvalidException(ERROR_MESSAGE);
    }

    private char type(final char c) {
        if (c == 'C' || c == 'P') {
            return c;
        } throw new FileDataInvalidException(ERROR_MESSAGE);
    }

    private boolean isPromotional(final String promotional) {
        if ("true".equalsIgnoreCase(promotional)) {
            return true;
        } else if ("false".equalsIgnoreCase(promotional)) {
            return false;
        }
        throw new FileDataInvalidException(ERROR_MESSAGE);
    }


    @Override
    public List<Item> getDataList() { return this.itemList; }

    @Override
    public List<Item> readFromFile() {

        final List<Item> itemList = new ArrayList<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(ITEM_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
                final int ID = this.ID(data[0]);
                final String name = data[1];
                final char type = this.type(data[2].charAt(0));
                final float memberPrice = this.price(data[3]);
                final float nonMemberPrice = this.price(data[4]);
                final boolean promotional = this.isPromotional(data[5]);
                itemList.add(new Item(ID, name, type, memberPrice, nonMemberPrice, promotional));
            }
            reader.close();
            //create unmodifiable list to improve immutability, reduce bug occurrence
            return Collections.unmodifiableList(itemList);

        } catch (final FileNotFoundException e) {
            throw new RuntimeException("File is missing: " + ITEM_TXT);
        } catch (final IOException e) {
            throw new FileDataInvalidException("Error reading " + ITEM_TXT);
        }
    }
}