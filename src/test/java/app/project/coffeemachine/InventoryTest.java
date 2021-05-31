package app.project.coffeemachine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InventoryTest {

    @Test
    public void testItemUnavailable() {
        Map<String, InventoryItem> items = new HashMap<>();
        items.put("a", new InventoryItem("a", 0));
        items.put("b", new InventoryItem("b", 10));
        Inventory inventory = new Inventory(items);

        Assertions.assertTrue(inventory.itemUnavailable("x"));
        Assertions.assertTrue(inventory.itemUnavailable("x"));
        Assertions.assertTrue(inventory.itemUnavailable("a"));
        Assertions.assertFalse(inventory.itemUnavailable("b"));

        items.get("b").lowInStock = true;
        Assertions.assertFalse(inventory.itemUnavailable("b"));
    }

    @Test
    public void testItemLowInStock() {
        Map<String, InventoryItem> items = new HashMap<>();
        items.put("a", new InventoryItem("a", 0));
        items.put("b", new InventoryItem("b", 10));
        Inventory inventory = new Inventory(items);

        Assertions.assertTrue(inventory.itemLowInStock("x"));
        Assertions.assertTrue(inventory.itemLowInStock("x"));
        Assertions.assertTrue(inventory.itemLowInStock("a"));
        Assertions.assertFalse(inventory.itemLowInStock("b"));

        items.get("b").lowInStock = true;
        Assertions.assertTrue(inventory.itemLowInStock("b"));
    }

    @Test
    public void testItemsToRefill() {
        Map<String, InventoryItem> items = new HashMap<>();
        items.put("a", new InventoryItem("a", 0));
        items.put("b", new InventoryItem("b", 10));
        items.put("c", new InventoryItem("c", 100));
        Inventory inventory = new Inventory(items);

        Map<String, Integer> itemsToRefill = new HashMap<>();
        itemsToRefill.put("a", 0);

        assertEquals(inventory.getItemsToRefill(), itemsToRefill);

        itemsToRefill = new HashMap<>();
        items.get("b").lowInStock = true;
        itemsToRefill.put("b", 10);
        itemsToRefill.put("a", 0);

        assertEquals(inventory.getItemsToRefill(), itemsToRefill);

        items.put("d", new InventoryItem("d", 0));
        assertNotEquals(inventory.getItemsToRefill(), itemsToRefill);

        itemsToRefill.put("d", 0);
        itemsToRefill.put("e", 10);
        assertNotEquals(inventory.getItemsToRefill(), itemsToRefill);

    }

    @Test
    public void testAddItem() {
        Map<String, InventoryItem> items = new HashMap<>();
        items.put("a", new InventoryItem("a", 0));
        items.put("b", new InventoryItem("b", 10));
        Inventory inventory = new Inventory(items);

        inventory.addItem("b", 100);
        assertEquals(inventory.getItemQuantity("b"), 110);

        inventory.addItem("c", 10);
        assertEquals(inventory.getItemQuantity("c"), 10);
    }


}
