package model;

// This class represents the resources, goods and services that are traded in a market
// (e.g: Iron, Grocery, Services).
// Every good has a base price, that is the price if supply = demand that people are willing to pay
// goods also have supply and demand to determine how cheaper or more expensive its price is.
// A good is in shortage if price >= UPPER_PRICE_CAP * basePrice

import org.json.JSONObject;
import persistence.Writable;

public class Goods implements Writable {

    public static final double UPPER_PRICE_CAP = 1.75;
    public static final double LOWER_PRICE_CAP = 0.25;
    private final String name;
    private final int basePrice;

    private int supply;
    private int demand;
    private boolean shortage;
    private GoodsType type;

    // EFFECT: return good as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("upper cap", UPPER_PRICE_CAP);
        json.put("lower cap", LOWER_PRICE_CAP);
        json.put("name", name);
        json.put("base", basePrice);
        json.put("supply", supply);
        json.put("demand", demand);
        json.put("type", type);
        json.put("shortage", shortage);
        return json;
    }


    public enum GoodsType { CONSUMER, INDUSTRIAL }

    // EFFECTS: construct a resource with given name, base price and 0 supply & demand and no shortage
    public Goods(String name, int basePrice, GoodsType goodsType) {
        this.name = name;
        this.basePrice = basePrice;
        supply = 0;
        demand = 0;
        shortage = false;
        type = goodsType;
    }

    // getters
    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getSupply() {
        return supply;
    }

    public int getDemand() {
        return demand;
    }

    public GoodsType getGoodsType() {
        return type;
    }

    // setters
    public void setSupply(int amount) {
        supply = amount;
    }

    public void setShortage(boolean s) {
        shortage = s;
    }

    public void setDemand(int amount) {
        demand = amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: increase supply by amount
    public void addSupply(int amount) {
        supply += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: increase demand by amount
    public void addDemand(int amount) {
        demand += amount;
    }


    // MODIFIES: this
    // EFFECTS: determine and return price modifier based on 1 + {0.75 * [(demand - supply) / MIN(demand,supply)]}
    public double determinePriceModifier() {
        double modifier;
        if (supply == 0 && demand == 0) {
            return 1.0;
        } else if (supply == 0) {
            return UPPER_PRICE_CAP;
        } else if (demand == 0) {
            return LOWER_PRICE_CAP;
        } else {
            if (demand >= supply) {
                modifier = 1.0 + ((0.75 * (demand - supply)) / supply);
                if (modifier >= UPPER_PRICE_CAP) {
                    return UPPER_PRICE_CAP;
                }
            } else {
                modifier = 1.0 + (0.75 * (demand - supply) / demand);
                if (modifier <= LOWER_PRICE_CAP) {
                    return LOWER_PRICE_CAP;
                }
            }
        }
        return modifier;
    }


    // MODIFIES: this
    // EFFECTS: return the price of goods from supply & demand
    public int determinePrice() {
        return (int) (basePrice * determinePriceModifier());
    }


    // MODIFIES: this
    // EFFECTS: determine and return whether a good is in shortage
    public boolean isShortage() {
        return shortage;
    }
}
