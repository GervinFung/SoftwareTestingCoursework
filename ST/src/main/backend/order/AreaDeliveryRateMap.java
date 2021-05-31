package main.backend.order;

import main.backend.FileDataInvalidException;
import main.backend.DataValidation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AreaDeliveryRateMap {

    private static final String DELIVERY_RATE_TXT = "textFiles/deliveryRate.txt";

    private final Map<String, Float> areaDeliveryRateMap;

    public AreaDeliveryRateMap() { this.areaDeliveryRateMap = this.readAreaDeliveryRateFromFile(); }

    public Map<String, Float> getAreaDeliveryRateMap() { return this.areaDeliveryRateMap; }

    private Map<String, Float> readAreaDeliveryRateFromFile() {

        final HashMap<String, Float> areaDeliveryRateMap = new HashMap<>();
        try (final BufferedReader reader = new BufferedReader(new FileReader(DELIVERY_RATE_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
                final String area = data[0];
                final float deliveryRate = this.deliveryRate(data[1]);
                areaDeliveryRateMap.put(area.toLowerCase(), deliveryRate);
            }
            reader.close();
            //create unmodifiable list to improve immutability, reduce bug occurrence
            return Collections.unmodifiableMap(areaDeliveryRateMap);

        } catch (final IOException e) {
            e.printStackTrace();
            throw new FileDataInvalidException("Error reading " + DELIVERY_RATE_TXT);
        }
    }

    private float deliveryRate(final String deliveryRate) {
        if (DataValidation.tryParseFloatInRange(deliveryRate, 0.01f, Float.MAX_VALUE)) {
            return Float.parseFloat(deliveryRate);
        } throw new FileDataInvalidException(
                "\n\nFile format should be the following\n" +
                        "First column is the 20 state of Melaka, second column is the delivery rate, which MUST be larger than 0\n" +
                        "Alor Gajah/2.5\n" +
                        "Asahan/4\n\n");
    }
}