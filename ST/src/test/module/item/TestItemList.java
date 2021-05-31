package test.module.item;

import org.junit.Test;
import main.backend.item.Item;
import main.backend.item.ItemList;

import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public final class TestItemList {

    private final static HashSet<Item> ITEM_HASH_SET = new HashSet<>(new ItemList().getDataList());

    //Test case 001, Get the size of HashSet of list of item
    @Test
    public void testItemHashCodeUniqueness() {
        assertEquals("Item list should only have 20 unique data only",20, ITEM_HASH_SET.size());
    }

    //Test case 002, Get the HashSet of list of item and find number of promotional item
    @Test
    public void testItemPromotional() {
        //check only 5 items is promotional
        assertEquals("Item list should only have 5 promotional item only",5,
                ITEM_HASH_SET
                .stream()
                .filter(item -> item.getPromotional().isPromotional() && item.getPromotional().getDiscountRatio() == 0.95f)
                .collect(Collectors.toUnmodifiableList()).size());
    }

    //Test case 003, Get the HashSet of list of item and find number of item type with cake
    @Test
    public void testItemTypeCake() {
        //only 13 items are cake
        assertEquals("Item list should only have 13 cakes only",13,
                ITEM_HASH_SET
                .stream()
                .filter(item -> item.getItemType().isCake())
                .collect(Collectors.toUnmodifiableList()).size());
    }

    //Test case 004, Get the size of HashSet of list of item that contains both member and non member price
    @Test
    public void testMemberAndNonMemberItem() {
        assertEquals(20,
                ITEM_HASH_SET
                        .stream()
                        .filter(item -> item.getMemberPrice() < item.getNonMemberPrice())
                        .collect(Collectors.toUnmodifiableList()).size());
    }
}