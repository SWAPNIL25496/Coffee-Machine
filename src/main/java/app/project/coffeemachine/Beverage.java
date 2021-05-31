package app.project.coffeemachine;

import java.util.List;

/**
 * Class for storing types of Beverages
 */
public class Beverage {
    private final String name;
    private final List<Ingredient> ingredients;

    public Beverage(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
