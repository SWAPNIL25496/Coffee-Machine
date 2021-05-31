package app.project.coffeemachine;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.*;


public class Main {
    public static int testNumber = 0;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/TestAll.json"));

            JSONArray tests = (JSONArray) obj;

            tests.forEach(element -> parseMachineObject((JSONObject) element));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseMachineObject(JSONObject element) {

        JSONObject machineObject = (JSONObject) element.get("machine");

        JSONObject outletsObject = (JSONObject) machineObject.get("outlets");

        int numOutlets = (int) (long) outletsObject.get("count_n");
        List<Beverage> beverages = new ArrayList<>();
        Map<String, InventoryItem> items = new HashMap<>();

        JSONObject beveragesObject = (JSONObject) machineObject.get("beverages");
        beveragesObject.forEach((K, V) -> {
            String beverageName = (String) K;
            List<Ingredient> ingredients = new ArrayList<>();
            ((JSONArray) V).forEach(ingredient -> {
                JSONObject ingredientObject = (JSONObject) ingredient;
                ingredientObject.forEach((k, v) -> ingredients.add(new Ingredient((String) k, (int) (long) v)));
            });

            beverages.add(new Beverage(beverageName, ingredients));
        });

        JSONObject itemsQuantityObject = (JSONObject) machineObject.get("total_items_quantity");
        itemsQuantityObject.forEach((K, V) -> items.put((String) K, new InventoryItem((String) K, (int) (long) V)));

        CoffeeMachine coffeeMachine = new CoffeeMachine(numOutlets, beverages, new Inventory(items));
        System.out.println("\n<--- Test #" + ++testNumber + " --->");
        Set<String> output = coffeeMachine.startPreparing();
        for (String s : output)
            System.out.println(s);
    }

}
