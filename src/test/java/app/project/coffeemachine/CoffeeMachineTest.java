package app.project.coffeemachine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyInt;

public class CoffeeMachineTest {

    @Test
    public void testCheckRequirements() {
        Inventory inventory = Mockito.mock(Inventory.class);
        CoffeeMachine coffeeMachine = new CoffeeMachine(anyInt(), new ArrayList<>(), inventory);

        // case when ingredient is not available
        Mockito.when(inventory.itemUnavailable("item_name")).thenReturn(true);
        Mockito.when(inventory.itemLowInStock("item_name")).thenReturn(true);

        String status = coffeeMachine.checkRequirements(new Beverage("beverage_name",
                Arrays.asList(new Ingredient("item_name", anyInt()))));
        assertEquals(status, "beverage_name cannot be prepared because item_name is not available");

        // case when ingredient is available but insufficient (quantity is non-zero)
        Mockito.when(inventory.itemUnavailable("item_name")).thenReturn(false);
        Mockito.when(inventory.itemLowInStock("item_name")).thenReturn(true);

        status = coffeeMachine.checkRequirements(new Beverage("beverage_name",
                Arrays.asList(new Ingredient("item_name", anyInt()))));
        assertEquals(status, "beverage_name cannot be prepared because item item_name is not sufficient");

        // case when ingredient is available and sufficient
        Mockito.when(inventory.itemUnavailable("item_name")).thenReturn(false);
        Mockito.when(inventory.itemLowInStock("item_name")).thenReturn(false);

        status = coffeeMachine.checkRequirements(new Beverage("beverage_name",
                Arrays.asList(new Ingredient("item_name", anyInt()))));
        assertEquals(status, "");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStartPreparing() {
        CoffeeMachine coffeeMachine;
        JSONParser parser = new JSONParser();
        List<Set<String>> correctOutputs = new ArrayList<>();

        //test0
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/answer0.json"));
            JSONObject possibleStatesObject = (JSONObject) obj;
            correctOutputs = parsePossibleStatesObject(possibleStatesObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/test0.json"));
            JSONObject machineObject = (JSONObject) obj;
            coffeeMachine = parseMachineObject(machineObject);
            Set<String> output = coffeeMachine.startPreparing();
            assertTrue(correctOutputs.contains(output));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //test1
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/answer1.json"));
            JSONObject possibleStatesObject = (JSONObject) obj;
            correctOutputs = parsePossibleStatesObject(possibleStatesObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/test1.json"));
            JSONObject machineObject = (JSONObject) obj;
            coffeeMachine = parseMachineObject(machineObject);
            Set<String> output = coffeeMachine.startPreparing();
            assertTrue(correctOutputs.contains(output));
            Set<String> expectedQuantities = new HashSet<>(Arrays.asList("green_mixture : 150", "sugar_syrup : 200", "hot_water : 1500", "hot_milk : 2500", "tea_leaves_syrup : 550", "ginger_syrup : 500"));
            assertEquals(coffeeMachine.getInventorySummary(), expectedQuantities);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //test2
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/answer2.json"));
            JSONObject possibleStatesObject = (JSONObject) obj;
            correctOutputs = parsePossibleStatesObject(possibleStatesObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/test2.json"));
            JSONObject machineObject = (JSONObject) obj;
            coffeeMachine = parseMachineObject(machineObject);
            Set<String> output = coffeeMachine.startPreparing();
            assertTrue(correctOutputs.contains(output));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //test3
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/answer3.json"));
            JSONObject possibleStatesObject = (JSONObject) obj;
            correctOutputs = parsePossibleStatesObject(possibleStatesObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try {
            Object obj = parser.parse(new FileReader("src/test/resources/test3.json"));
            JSONObject machineObject = (JSONObject) obj;
            coffeeMachine = parseMachineObject(machineObject);
            Set<String> output = coffeeMachine.startPreparing();
            assertTrue(correctOutputs.contains(output));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("unchecked")
    private List<Set<String>> parsePossibleStatesObject(JSONObject obj) {
        List<Set<String>> allPossibleStates = new ArrayList<>();
        JSONArray possibleStatesArray = (JSONArray) obj.get("possible_states");

        possibleStatesArray.forEach(element -> {
            Set<String> possibleStatesSet = new HashSet<>();
            JSONObject stateObject = (JSONObject) element;

            JSONArray statesArray = (JSONArray) stateObject.get("states");

            statesArray.forEach(state -> {
                ((JSONObject) state).forEach((k, v) -> {
                    possibleStatesSet.add((String) v);
                });
            });
            allPossibleStates.add(possibleStatesSet);
        });
        return allPossibleStates;
    }

    @SuppressWarnings("unchecked")
    private CoffeeMachine parseMachineObject(JSONObject element) {

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

        return new CoffeeMachine(numOutlets, beverages, new Inventory(items));

    }

}
