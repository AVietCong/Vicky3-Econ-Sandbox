package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// This class represents where buildings are queued to be built
// and also consume goods and pay expenses like buildings

public class ConstructionSector implements Writable {

    public static final int WAGES = 5000;

    private List<Building> constructionQueue;
    private List<Integer> constructionValue;
    private final List<Goods> inputGoods;
    private final List<Integer> inputAmount;
    private int expense;

    // EFFECTS: create a new construction sector with empty construction queue
    public ConstructionSector(List<Goods> inputGoods, List<Integer> inputAmount) {
        constructionQueue = new ArrayList<>();
        constructionValue = new ArrayList<>();
        expense = 0;
        this.inputGoods = inputGoods;
        this.inputAmount = inputAmount;
    }

    //getters
    public int getExpense() {
        return expense;
    }

    public List<Goods> getInputGoods() {
        return inputGoods;
    }

    public List<Integer> getInputAmount() {
        return inputAmount;
    }

    public List<Building> getConstructionQueue() {
        return constructionQueue;
    }

    public List<Integer> getConstructionValue() {
        return constructionValue;
    }

    //setters
    public void setExpense(int expense) {
        this.expense = expense;
    }

    public void setConstructionQueue(List<Building> queue) {
        constructionQueue = queue;
    }

    public void setConstructionValue(List<Integer> value) {
        constructionValue = value;
    }

    // MODIFIES: this
    // EFFECTS: set expenses to cover costs of operation
    public void payExpense() {
        expense = 0;
        buyGoods();
        payWages();
    }

    // MODIFIES: inputGoods
    // EFFECTS: increase the demand for input goods
    public void consume() {
        for (int i = 0; i < inputGoods.size(); i++) {
            inputGoods.get(i).addDemand((inputAmount.get(i)));
        }
    }

    // MODIFIES: this
    // EFFECTS: increases expenses to buy goods
    public void buyGoods() {
        for (int i = 0; i < inputGoods.size(); i++) {
            expense += inputGoods.get(i).determinePrice() * (inputAmount.get(i));
        }
    }

    // MODIFIES: this
    // EFFECTS: increase expenses to pay wages
    public void payWages() {
        expense += WAGES;
    }

    // EFFECTS: return lists of buildings that are fully constructed and reduce remaining the construction value. Remove
    // the constructed building. the return list could be empty but never null
    public List<Building> construct() {
        int pointsAvailable = 150;
        List<Building> constructed = new ArrayList<>();
        while (pointsAvailable > 0 && constructionQueue.size() > 0) {
            Building currentBuilding = constructionQueue.get(0);
            if (pointsAvailable >= constructionValue.get(0)) {
                pointsAvailable -= constructionValue.get(0);
                constructed.add(constructionQueue.get(0));
                constructionQueue.remove(0);
                constructionValue.remove(0);
            } else {
                constructionValue.set(0, constructionValue.get(0) - pointsAvailable);
                pointsAvailable = 0;
            }
        }
        return constructed;
    }

    // MODIFIES: this
    // EFFECTS: add building to the end of construction queue and add corresponding building's construction value to
    // constructionValue list
    public void build(Building b) {
        constructionQueue.add(b);
        constructionValue.add(b.getConstructionCost());
    }

    // MODIFIES: this
    // EFFECTS: remove first instance of building in construction queue and return true; otherwise return false;
    public boolean remove(Building b) {
        int index = constructionQueue.indexOf(b);
        if (index == -1) {
            return false;
        } else {
            constructionQueue.remove(index);
            constructionValue.remove(index);
            return true;
        }
    }

    // EFFECTS: return this a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expense", expense);
        json.put("queue", queueToJson());
        json.put("value", constructionValue);
        json.put("input", inputGoodsToJson());
        json.put("input amount", inputAmount);

        return json;
    }

    // EFFECTS: return building queue as a JSON object
    private JSONArray queueToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Building building : constructionQueue) {
            jsonArray.put(building.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: return list of goods as a JSON object
    private JSONArray inputGoodsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Goods goods : inputGoods) {
            jsonArray.put(goods.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: return true if two construction queue is equal in every field.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstructionSector that = (ConstructionSector) o;
        return getExpense() == that.getExpense() && Objects.equals(getConstructionQueue(),
                that.getConstructionQueue()) && Objects.equals(getConstructionValue(),
                that.getConstructionValue()) && getInputGoods().equals(that.getInputGoods())
                && getInputAmount().equals(that.getInputAmount());
    }

    // EFFECTS: return hashCode
    @Override
    public int hashCode() {
        return Objects.hash(getConstructionQueue(), getConstructionValue(), getInputGoods(), getInputAmount(),
                getExpense());
    }
}
