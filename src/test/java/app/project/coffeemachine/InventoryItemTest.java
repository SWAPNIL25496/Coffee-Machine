package app.project.coffeemachine;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryItemTest {


    @Test
    public void testAdd() {
        InventoryItem inventoryItem = new InventoryItem("item_name", 12);
        inventoryItem.add(10);
        Assertions.assertEquals(inventoryItem.quantity, 22);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> inventoryItem.add(-10));
        String expectedMessage = "Quantity can not be negative";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testConsume() {
        InventoryItem inventoryItem = new InventoryItem("item_name", 100);
        assertFalse(inventoryItem.consume(200));
        assertTrue(inventoryItem.consume(10));
        assertTrue(inventoryItem.consume(90));
        assertFalse(inventoryItem.consume(1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> inventoryItem.consume(-1));
        String expectedMessage = "Quantity can not be negative";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }
}
