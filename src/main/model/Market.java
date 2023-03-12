package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// The Market is where prices of goods are determined, bought and sold this is represented as a list of Goods.

public class Market implements Writable {

    private List<Goods> market;

    // EFFECTS: create a market with no goods
    public Market() {
        market = new ArrayList<>();
    }

    public List<Goods> getAllGoods() {
        return market;
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

    // INVARIANT: returnList.size() == market.size()
    // EFFECTS: return the price modifiers of goods in corresponding position
    public List<Integer> getBasePrices() {
        List<Integer> listOfBasePrice = new ArrayList<>();
        for (Goods goods : market) {
            listOfBasePrice.add(goods.getBasePrice());
        }
        return listOfBasePrice;
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
    // EFFECTS: return a market with only active goods (supply & demand != 0)
    public Market removeInactiveGoods() {
        Market activeGoodsMarket = new Market();
        for (Goods goods : market) {
            if (goods.getDemand() != 0 || goods.getSupply() != 0) {
                activeGoodsMarket.addGoods(goods);
            }
        }
        return activeGoodsMarket;
    }

    // MODIFIES: this
    // EFFECTS: return all consumer goods in the market
    public List<Goods> returnConsumerGoods() {
        List<Goods> consumerGoods = new ArrayList<>();
        for (Goods goods : market) {
            if (goods.getGoodsType() == Goods.GoodsType.CONSUMER) {
                consumerGoods.add(goods);
            }
        }
        return consumerGoods;
    }

    // MODIFIES: this
    // EFFECTS: return all industrial goods in the market
    public List<Goods> returnIndustrialGoods() {
        List<Goods> industrialGoods = new ArrayList<>();
        for (Goods goods : market) {
            if (goods.getGoodsType() == Goods.GoodsType.INDUSTRIAL) {
                industrialGoods.add(goods);
            }
        }
        return industrialGoods;
    }

    // EFFECTS: return Market as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Goods goods : market) {
            jsonArray.put(goods.toJson());
        }
        json.put("market", jsonArray);
        return json;
    }


    // EFFECTS: return true if list of goods are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Market market1 = (Market) o;
        return Objects.equals(market, market1.market);
    }

    // EFFECTS: return hash code
    @Override
    public int hashCode() {
        return Objects.hash(market);
    }
}
