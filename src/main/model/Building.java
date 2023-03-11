package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;
import java.util.Objects;

//This class represents the industries of a nation. Buildings take in resources and convert them into more valuable
//resources that can be consumed by the population or other buildings. Buildings have income and expenses that
//is determined by prices of input and output goods and building size.

public class Building implements Writable {

    // INVARIANT: inputGoods.size() == inputAmount.size(), outputGoods.size() == outputAmount.size()
    // a building consume a good in inputGoods with corresponding amount in inputAmount
    public static final int WAGES_PER_LEVEL = 500;
    public static final double MAXIMUM_EOS_BONUS = 1.5;
    protected String name;
    protected int size;
    protected int income;
    protected int expense;
    protected int constructionCost;
    protected final List<Goods> inputGoods;
    protected final List<Integer> inputAmount;
    protected final List<Goods> outputGoods;
    protected final List<Integer> outputAmount;

    // REQUIRES: constructionCost > 0, all lists != null and size() > 0
    // EFFECTS: create building with given name, input and output process and construction cost
    public Building(String name, int constructionCost,
                    List<Goods> inputGoods, List<Integer> inputAmount,
                    List<Goods> outputGoods, List<Integer> outputAmount) {
        this.name = name;
        this.constructionCost = constructionCost;
        size = 1;
        income = 0;
        expense = 0;
        this.inputGoods = inputGoods;
        this.inputAmount = inputAmount;
        this.outputGoods = outputGoods;
        this.outputAmount = outputAmount;
    }

    // getters
    public int getExpense() {
        return expense;
    }

    public int getIncome() {
        return income;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getConstructionCost() {
        return constructionCost;
    }

    public List<Goods> getInputGoods() {
        return inputGoods;
    }

    public List<Goods> getOutputGoods() {
        return outputGoods;
    }

    public List<Integer> getInputAmount() {
        return inputAmount;
    }

    public List<Integer> getOutputAmount() {
        return outputAmount;
    }

    // setters
    // REQUIRES: size >= 0
    public void setSize(int size) {
        this.size = size;
    }

    // MODIFIES: inputGoods
    // EFFECTS: increase the demand of input goods in market
    public void consume() {
        for (int i = 0; i < inputGoods.size(); i++) {
            inputGoods.get(i).addDemand((int) (inputAmount.get(i) * size * determineEoS()));
        }
    }

    // MODIFIES: outputGoods
    // EFFECTS: increase the supply of output goods in market
    public void produce() {
        for (int i = 0; i < outputGoods.size(); i++) {
            outputGoods.get(i).addSupply((int) (outputAmount.get(i) * size * determineEoS()));
        }
    }

    // MODIFIES: this
    // EFFECTS: set expenses to cover costs of operations
    public void payExpense() {
        expense = 0;
        buyGoods();
        payWages();
    }

    // MODIFIES: this
    // EFFECTS: determine income from selling goods or subsidies
    public void gainIncome() {
        income = 0;
        sellGoods();
    }

    // MODIFIES: this
    // EFFECTS: increase expenses to buy goods
    public void buyGoods() {
        for (int i = 0; i < inputGoods.size(); i++) {
            expense += (int) (inputGoods.get(i).determinePrice() * (inputAmount.get(i) * size * determineEoS()));
        }
    }

    // MODIFIES: this
    // EFFECTS: increase expenses to pay wages
    public void payWages() {
        expense += WAGES_PER_LEVEL * size;
    }


    // MODIFIES: this
    // EFFECTS: increase income by selling goods
    public void sellGoods() {
        for (int i = 0; i < outputGoods.size(); i++) {
            income += (int) (outputGoods.get(i).determinePrice() * (outputAmount.get(i) * size * determineEoS()));
        }
    }

    // REQUIRES: size > 0
    // EFFECTS: return the economy of scale bonus for building object
    public double determineEoS() {
        double bonus = (1 + (size - 1) * 0.01);
        if (bonus > MAXIMUM_EOS_BONUS) {
            return MAXIMUM_EOS_BONUS;
        }
        return bonus;
    }

    // EFFECTS: determine if building is profitable; return true if income >= expense; otherwise return false
    public boolean isProfitable() {
        if (income >= expense) {
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: increase the size of building by 1
    public void expand() {
        size += 1;
    }

    // REQUIRES: level <= size
    // MODIFIES: this
    // EFFECTS: decrease the size of building by amount and return true if successful
    public boolean downsize(int level) {
        if (level <= size) {
            size -= level;
            return true;
        }
        return false;
    }

    // EFFECTS: return a JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("wages", WAGES_PER_LEVEL);
        json.put("eos", MAXIMUM_EOS_BONUS);
        json.put("name", name);
        json.put("size", size);
        json.put("income", income);
        json.put("expense", expense);
        json.put("cost", constructionCost);
        json.put("input goods", goodsToJson(inputGoods));
        json.put("input amount", inputAmount);
        json.put("output goods", goodsToJson(outputGoods));
        json.put("output amount", outputAmount);

        return json;
    }

    // EFFECTS: return list of goods as a JSON object
    private JSONArray goodsToJson(List<Goods> allGoods) {
        JSONArray jsonArray = new JSONArray();
        for (Goods goods : allGoods) {
            jsonArray.put(goods.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: return true if every field of two buildings are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Building building = (Building) o;
        return getSize() == building.getSize() && getIncome() == building.getIncome()
                && getExpense() == building.getExpense()
                && getConstructionCost() == building.getConstructionCost()
                && getName().equals(building.getName()) && getInputGoods().equals(building.getInputGoods())
                && getInputAmount().equals(building.getInputAmount())
                && getOutputGoods().equals(building.getOutputGoods())
                && getOutputAmount().equals(building.getOutputAmount());
    }

    // EFFECTS: return hash code
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSize(), getIncome(), getExpense(), getConstructionCost(), getInputGoods(),
                getInputAmount(), getOutputGoods(), getOutputAmount());
    }
}
