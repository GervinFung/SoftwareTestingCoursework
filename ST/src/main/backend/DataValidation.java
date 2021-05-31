package main.backend;

import main.backend.order.Order;
import main.backend.payment.Payment;

import java.util.Map;
import java.util.regex.Pattern;

public final class DataValidation {

    private final Pattern alphabetAndSpaceOnly;

    public DataValidation() {
        this.alphabetAndSpaceOnly = Pattern.compile("^[A-Za-z. ]+$");
    }


    public boolean isAreaCorrect(final String area, final Map<String, Float> areaDeliveryRateMap) { return areaDeliveryRateMap.containsKey(area.toLowerCase()); }

    public boolean isDistrictCorrect(final String district) {
        return "alor gajah".equalsIgnoreCase(district) ||
                "melaka tengah".equalsIgnoreCase(district) ||
                "central melaka".equalsIgnoreCase(district) ||
                "jasin".equalsIgnoreCase(district); }

    public boolean isContactCorrect(final String contact) {
        if (!contact.isEmpty() || !contact.isBlank()) {
            final boolean isElevenDigit = contact.charAt(0) == '1' && tryParseIntInRange(contact, 100000000, 199999999);
            final boolean isTenDigit;
            if (contact.charAt(0) == '0') {
                isTenDigit = tryParseIntInRange(contact, 1000000, 9999999);
            } else {
                isTenDigit = tryParseIntInRange(contact, 20000000, 99999999);
            }
            return isElevenDigit || isTenDigit;
        }
        return false;
    }

    public Payment.PAYMENT_TYPE getPaymentType(final String paymentType) {
        if ("c".equalsIgnoreCase(paymentType)) {
            return Payment.PAYMENT_TYPE.CREDIT_CARD;
        } else if ("o".equalsIgnoreCase(paymentType)) {
            return Payment.PAYMENT_TYPE.ONLINE_PAYMENT;
        } else {
            return null;
        }
    }

    public Order.ORDER_STATUS getOrderStatus(final String choice) {
        if ("n".equalsIgnoreCase(choice)) {
            return Order.ORDER_STATUS.SUCCESSFUL;
        } else if ("l".equalsIgnoreCase(choice)){
            return Order.ORDER_STATUS.PENDING;
        } else {
            return null;
        }
    }

    public static boolean  tryParseIntInRange(final String data, final int min, final int max) {
        try {
            final int temp = Integer.parseInt(data);
            if (temp >= min && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }
    public static boolean tryParseFloatInRange(final String data, final float min, final float max) {
        try {
            final float temp = Float.parseFloat(data);
            if (temp >= min && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }
    public static boolean tryParseLongInRange(final String data, final long min, final long max) {
        try {
            final long temp = Long.parseLong(data);
            if (temp >= min && temp <= max) {
                return true;
            }
        } catch (final NumberFormatException ignored) {}
        return false;
    }
    public boolean isProperStringFormat(final String input) {
        return !input.isEmpty() && !input.isBlank() && input.charAt(0) != ' ' && this.alphabetAndSpaceOnly.matcher(input).find();
    }
}
