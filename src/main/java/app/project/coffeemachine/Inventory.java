package app.project.coffeemachine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Inventory {
    private final Map<String, InventoryItem> items;


    public Inventory(Map<String, InventoryItem> items) {
        this.items = items;
        markItemsUnavailable();
    }

    public void markItemsUnavailable() {
        for (Map.Entry<String, InventoryItem> e : items.entrySet()) {
            if (e.getValue().quantity == 0)
                e.getValue().lowInStock = true;
        }
    }

    public int getItemQuantity(String name) {
        if (!items.containsKey(name))
            throw new IllegalArgumentException("This Item does not exist");
        return items.get(name).quantity;
    }

    public boolean itemUnavailable(String name) { // if quantity is zero or has no entry
        if (!items.containsKey(name))
            addItem(name, 0); // adding an item required to make the beverage but not mentioned in the inventory
        return items.get(name).quantity == 0;
    }

    public boolean itemLowInStock(String name) {
        if (!items.containsKey(name))
            addItem(name, 0); // adding an item required to make the beverage but not mentioned in the inventory
        return items.get(name).lowInStock;
    }

    public Map<String, Integer> getItemsToRefill() { // returns unavailable and low in stock items
        Map<String, Integer> itemsToRefill = new HashMap<>();
        items.keySet().forEach(itemName -> {
            if (itemUnavailable(itemName) || itemLowInStock(itemName))
                itemsToRefill.put(itemName, getItemQuantity(itemName));
        });
        return itemsToRefill;
    }

    public boolean consumeItem(String name, int amount) {
        return items.get(name).consume(amount);
    }

    public void addItem(String name, int amount) {
        if (items.containsKey(name)) // checking if the item is already present or not
            items.get(name).add(amount); // refill item in this case
        else
            items.put(name, new InventoryItem(name, amount)); // add new item in this case
    }

    public Set<String> inventorySummary() {
        Set<String> itemsSet = new HashSet<>();
        for (Map.Entry<String, InventoryItem> e : items.entrySet()) {
            itemsSet.add(e.getKey() + " : " + e.getValue().quantity);
        }
        return itemsSet;
    }
}
