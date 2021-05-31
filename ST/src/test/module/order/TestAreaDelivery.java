package test.module.order;

import main.backend.order.AreaDeliveryRateMap;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.assertSame;

public final class TestAreaDelivery {

    private final static AreaDeliveryRateMap AREA_DELIVERY_RATE_MAP = new AreaDeliveryRateMap();

    @Test
    public void testTwentyDeliveryAreaExists() {
        assertSame(20, new AreaDeliveryRateMap().getAreaDeliveryRateMap().size());
    }

    @Test
    public void testAllDeliveryRateGreaterThanZero() {
        final int actual = new AreaDeliveryRateMap()
                .getAreaDeliveryRateMap()
                .values()
                .stream()
                .filter(rate -> rate > 0)
                .collect(Collectors.toUnmodifiableList())
                .size();
        assertSame(20, actual);
    }
}