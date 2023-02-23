package model;


public class Goods {
    // This class represents the resources, goods and services that are traded in a market
    // (e.g: Iron, Grocery, Services).
    // Every good has a base price, that is the price if supply = demand that people are willing to pay
    // goods also have supply and demand to determine how cheaper or more expensive its price is.
    // A good is in shortage if demand > price_cap * supply

    public static final double UPPER_PRICE_CAP = 1.75;
    public static final double LOWER_PRICE_CAP = 0.25;
    private final String name;
    private final int basePrice;

    private int supply;
    private int demand;
    private double priceModifier;

    private boolean shortage;

    // EFFECTS: construct a resource with given name, base price and 0 supply & demand and no shortage
    public Goods(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        supply = 0;
        demand = 0;
        shortage = false;
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

    public double getPriceModifier() {return priceModifier;}

    // setters
    public void setSupply(int supply) {
        this.supply = supply;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setShortageStatus(boolean s) {
        shortage = s;
    }

    public void setPriceModifier(double modifier) {priceModifier = modifier;}


    // MODIFIES: this
    // EFFECTS: determine and return price modifier based on f(supply,demand)
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
                modifier = 1.0 + (0.75 * (demand - supply) / supply);
            } else {
                modifier = 1.0 + (0.75 * (demand - supply) / demand);
            }
        }
        return modifier;
    }


    // MODIFIES: this
    // EFFECTS: return the price of goods from supply & demand
    public int determinePrice() {
        return (int) (basePrice * priceModifier);
    }


    // MODIFIES: this
    // EFFECTS: determine and return whether a good is in shortage
    public boolean isShortage() {
        if (priceModifier > UPPER_PRICE_CAP) {
            return true;
        }
        return false;
    }

}
