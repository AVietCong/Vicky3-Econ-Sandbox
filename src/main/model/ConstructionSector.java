package model;

import java.util.ArrayList;
import java.util.List;

// This class represents where buildings are queued to be built
// and also consume goods and pay expenses like buildings

public class ConstructionSector {

    public static final int WAGES = 5000;

    private List<Building> constructionQueue;
    private List<Integer> constructionValue;
    private List<Goods> inputGoods;
    private List<Integer> inputAmount;
    private int expense;

    // EFFECTS: create a new construction sector with size 1, empty construction queue
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
}
