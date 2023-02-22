package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Market {
    // The Market is where prices of goods are determined, bought and sold this is represented as a list of Goods.
    // GDP represents the total value of the market


    private List<Goods> market;
    private int gdp;

    // EFFECTS: create a market with no goods
    public Market() {
        market = new ArrayList<>();
        gdp = 0;
    }

    // getters
    public double getGDP() {
        return gdp;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the name of goods in corresponding position
    public List<String> getAvailableGoods() {
        List<String> ListOfGoods = new ArrayList<>();
        for (Goods goods : market) {
            ListOfGoods.add(goods.getName());
        }
        return ListOfGoods;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the supply of goods in corresponding position
    public List<Integer> getSupplyOfGoods() {
        List<Integer> ListOfSupply = new ArrayList<>();
        for (Goods goods : market) {
            ListOfSupply.add(goods.getSupply());
        }
        return ListOfSupply;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the demand of goods in corresponding position
    public List<Integer> getDemandOfGoods() {
        List<Integer> ListOfDemand = new ArrayList<>();
        for (Goods goods : market) {
            ListOfDemand.add(goods.getDemand());
        }
        return ListOfDemand;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the price modifiers of goods in corresponding position
    public List<Double> getPriceModifiers() {
        List<Double> ListOfModifiers = new ArrayList<>();
        for (Goods goods : market) {
            ListOfModifiers.add(goods.determinePriceModifier());
        }
        return ListOfModifiers;
    }

    // REQUIRES: Goods of same name isn't already in market;
    // MODIFIES: this
    // EFFECTS: add goods to the market
    public void addGoods(Goods g) {
        market.add(g);
    }

    // MODIFIES: this
    // EFFECTS: return true if successfully removed goods from the market otherwise return false
    public boolean removeGoods(Goods g) {
        if (market.contains(g)) {
            market.remove(g);
            return true;
        }
        return false;
    }

    // EFFECTS: set the GDP of the market
    public void setGDP(int gdp) {
        this.gdp = gdp;
    }

    // EFFECTS: return prices corresponding to goods in market
    // INVARIANT: List<Integer>.size() == market.size();
    public List<Integer> getPrices() {
        List<Integer> ListOfPrices = new ArrayList<>();
        for (Goods goods : market) {
            ListOfPrices.add(goods.determinePrice());
        }
        return ListOfPrices;
    }


    public int determineGDP() {
        int marketValue = 0;
        for (Goods goods : market) {
            marketValue += goods.getDemand() * goods.determinePrice();
        }
        return marketValue;
    }

    // EFFECTS: return the number of goods in the market
    public int size() {return market.size();}
}
