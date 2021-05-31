package app.project.coffeemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;


public class CoffeeMachine {
    private final int numOutlets;
    private final Logger log = LoggerFactory.getLogger(CoffeeMachine.class.getSimpleName());
    private final List<Beverage> beverages;
    private final Inventory inventory;

    public CoffeeMachine(int numOutlets, List<Beverage> beverages, Inventory inventory) {
        this.numOutlets = numOutlets;
        this.beverages = beverages;
        this.inventory = inventory;
    }

    public Set<String> startPreparing() {
        int numBeveragesToPrepare = Math.min(numOutlets, beverages.size());
        Set<String> output = new HashSet<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(numOutlets, Runtime.getRuntime().availableProcessors() - 1));

        List<Callable<String>> callableTasks = new ArrayList<>();
        for (Beverage beverage : beverages) {
            String status = checkRequirements(beverage);
            if (status.isEmpty()) {
                callableTasks.add(new PrepareBeverage(beverage, inventory));
                numBeveragesToPrepare--;
            } else
                output.add(status);
            if (numBeveragesToPrepare == 0)
                break;
        }

        try {
            List<Future<String>> futures = executorService.invokeAll(callableTasks);
            for (Future<String> future : futures) {
                String status = future.get();
                output.add(status);
            }
        } catch (ExecutionException e) {
            log.error("Something went wrong");
        } catch (InterruptedException e) {
            log.error("Thread running the task was interrupted");
        }

        executorService.shutdown();
        return output;
    }

    public String checkRequirements(Beverage beverage) {
        for (Ingredient ingredient : beverage.getIngredients()) {
            if (inventory.itemUnavailable(ingredient.name))
                return beverage.getName() + " cannot be prepared because " + ingredient.name + " is not available";

            if (inventory.itemLowInStock(ingredient.name))
                return beverage.getName() + " cannot be prepared because item " + ingredient.name + " is not sufficient";
        }
        return "";
    }

    public void addNewBeverage(Beverage beverage) {
        for (Beverage b : beverages) {
            if (b.getName().equals(beverage.getName()))
                throw new IllegalArgumentException("A beverage with name " + beverage.getName() + " already exists.");
        }
        beverages.add(beverage);
    }

    public Map<String, Integer> getItemsToRefill() {
        return inventory.getItemsToRefill();
    }

    public void addInventoryItem(String name, int amount) {
        inventory.addItem(name, amount);
    }

    public Set<String> getInventorySummary() {
        return inventory.inventorySummary();
    }

}
