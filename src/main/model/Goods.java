package model;


public class Goods {
    // This class represents the resources, goods and services that are traded in a market
    // (e.g: Iron, Grocery, Services).
    // Every good has a base price, that is the price if supply = demand that people are willing to pay
    // goods also have supply and demand to determine how cheaper or more expensive its price is.
    // A goods is in shortage if demand > price_cap * supply

    public static final double UPPER_PRICE_CAP = 1.75;
    public static final double LOWER_PRICE_CAP = 0.25;
    private final String name;
    private final int basePrice;

    private int supply;
    private int demand;

    private boolean shortage;

    // EFFECTS: construct a resource with given name, base price and 0 supply & demand and no shortage
    public Goods(String name, int basePrice) {
        // stub
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

    public boolean isShortage() {
        return shortage;
    }

    // setters
    public void setSupply(int supply) {
        this.supply = supply;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setShortage() {
        shortage = true;
    }

    public void setNoShortage() {
        shortage = false;
    }



    // MODIFIES: this
    // EFFECTS: return the price of goods from supply & demand and set whether the good is in shortage or not.
    public int determinePrice() {
        if (supply == 0 && demand == 0) {
            shortage = false;
            return basePrice;
        } else if (supply == 0) {
            shortage = true;
            return (int) (basePrice * UPPER_PRICE_CAP);
        } else if (demand == 0) {
            shortage = false;
            return (int) (basePrice * LOWER_PRICE_CAP);
        } else {
            double priceModifier;
            if (demand >= supply) {
                priceModifier = 1.0 + (0.75 * (demand - supply) / supply);
            } else {
                priceModifier = 1.0 + (0.75 * (demand - supply) / demand);
            }

            if (priceModifier >= UPPER_PRICE_CAP) {
                shortage = true;
                return (int) (basePrice * UPPER_PRICE_CAP);
            } else if (priceModifier <= LOWER_PRICE_CAP) {
                shortage = false;
                return (int) (basePrice * LOWER_PRICE_CAP);
            } else {
                shortage = false;
                return (int) (basePrice * priceModifier);
            }
        }
    }
}
