package app.project.coffeemachine;

import java.util.concurrent.Callable;

public class PrepareBeverage implements Callable<String> {
    private final Beverage beverage;
    private final Inventory inventory;

    public PrepareBeverage(Beverage beverage, Inventory inventory) {
        this.beverage = beverage;
        this.inventory = inventory;
    }

    @Override
    public String call() {

        // delay added to simulate randomness
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Ingredient ingredient : beverage.getIngredients()) {
            if (!inventory.consumeItem(ingredient.name, ingredient.quantity)) {
                if (inventory.itemUnavailable(ingredient.name))
                    return beverage.getName() + " cannot be prepared because " + ingredient.name + " is not available";

                return beverage.getName() + " cannot be prepared because item " + ingredient.name + " is not sufficient";
            }
        }

        return beverage.getName() + " is prepared";
    }

}
