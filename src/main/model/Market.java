package model;

import java.util.ArrayList;
import java.util.List;

public class Market {
    // The Market is where prices of goods are determined, bought and sold this is represented as a list of Goods.
    // GDP represents the total value of the market


    private List<Goods> market;
    private double gdp;

    public Market() {
        //stub
    }

    // getters
    public double getGDP() {
        return 1;
    }

    public List<String> getAvailableGoods() {
        return new ArrayList<String>();
    }

    public List<Integer> getSupplyOfGoods() {
        return new ArrayList<Integer>();
    }

    public List<Integer> getDemandOfGoods() {
        return new ArrayList<Integer>();
    }

    public List<Double> getPriceModifiers() {
        return new ArrayList<Double>();
    }

    // REQUIRES: Goods of same name isn't already in market;
    // MODIFIES: this
    // EFFECTS: add goods to the market
    public void addGoods(Goods g) {
        // void
    }

    // MODIFIES: this
    // EFFECTS: return true if successfully removed goods from the market otherwise return false
    public boolean removeGoods(Goods g) {
        return false;
    }


    public void setGDP(double gdp) {
        // stub
    }

    public List<Integer> getMarketPrices() {
        return new ArrayList<Integer>();
    }

    public void setMarketPrices() {
        // stub
    }

    public int determineGDP() {
        return 0;
    }
}
