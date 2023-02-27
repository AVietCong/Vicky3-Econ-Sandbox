package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Market {
    // The Market is where prices of goods are determined, bought and sold this is represented as a list of Goods.


    private List<Goods> market;

    // EFFECTS: create a market with no goods
    public Market() {
        market = new ArrayList<>();
    }


    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the name of goods in corresponding position
    public List<String> getAvailableGoods() {
        List<String> listOfGoods = new ArrayList<>();
        for (Goods goods : market) {
            listOfGoods.add(goods.getName());
        }
        return listOfGoods;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the supply of goods in corresponding position
    public List<Integer> getSupplyOfGoods() {
        List<Integer> listOfSupply = new ArrayList<>();
        for (Goods goods : market) {
            listOfSupply.add(goods.getSupply());
        }
        return listOfSupply;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the demand of goods in corresponding position
    public List<Integer> getDemandOfGoods() {
        List<Integer> listOfDemand = new ArrayList<>();
        for (Goods goods : market) {
            listOfDemand.add(goods.getDemand());
        }
        return listOfDemand;
    }

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the price modifiers of goods in corresponding position
    public List<Double> getPriceModifiers() {
        List<Double> listOfModifiers = new ArrayList<>();
        for (Goods goods : market) {
            listOfModifiers.add(goods.determinePriceModifier());
        }
        return listOfModifiers;
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

    // EFFECTS: return prices corresponding to goods in market
    // INVARIANT: List<Integer>.size() == market.size();
    public List<Integer> getPrices() {
        List<Integer> listOfPrices = new ArrayList<>();
        for (Goods goods : market) {
            listOfPrices.add(goods.determinePrice());
        }
        return listOfPrices;
    }

    // EFFECTS: return gdp or total value of the market
    public int determineGDP() {
        int marketValue = 0;
        for (Goods goods : market) {
            marketValue += goods.getDemand() * goods.determinePrice();
        }
        return marketValue;
    }

    // EFFECTS: return the number of goods in the market
    public int numberOfGoods() {
        return market.size();
    }

    // MODIFIES: this
    // EFFECTS: remove all goods that are considered "inactive" (no demand and supply)
    public void removeInactiveGoods() {
        for (Iterator<Goods> iterator = market.iterator(); iterator.hasNext();) {
            Goods goods = iterator.next();
            if (goods.getDemand() == 0 && goods.getSupply() == 0) {
                iterator.remove();
            }
        }
    }
}
