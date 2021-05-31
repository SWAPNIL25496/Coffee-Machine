package app.project.coffeemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryItem extends Ingredient {
    private final Logger log = LoggerFactory.getLogger(InventoryItem.class.getSimpleName());
    private final Lock itemLock;
    protected boolean lowInStock;

    public InventoryItem(String name, int quantity) {
        super(name, quantity);
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity can not be negative");
        lowInStock = quantity == 0;
        this.itemLock = new ReentrantLock();
    }

    public void add(int additionalQuantity) {
        if (additionalQuantity < 0)
            throw new IllegalArgumentException("Quantity can not be negative");

        itemLock.lock();
        try {
            quantity += additionalQuantity;
            lowInStock = false;
        } finally {
            itemLock.unlock();
        }
    }

    public boolean consume(int consumeQuantity) {
        if (consumeQuantity < 0)
            throw new IllegalArgumentException("Quantity can not be negative");

        itemLock.lock();
        try {
            if (consumeQuantity > quantity) {
                lowInStock = true;
                return false;
            }
            quantity -= consumeQuantity;
            if (quantity == 0)
                lowInStock = true;

        } finally {
            itemLock.unlock();
        }
        return true;
    }
}
