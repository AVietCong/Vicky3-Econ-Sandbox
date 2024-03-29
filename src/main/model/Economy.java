package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

public class Economy implements Writable {

    private final Industries industries;
    private final Market market;
    private final ConstructionSector constructionSector;

    // EFFECTS: construct an economy with given industries, market and construction sector
    public Economy(Industries industries, Market market, ConstructionSector constructionSector) {
        this.industries = industries;
        this.market = market;
        this.constructionSector = constructionSector;
    }

    // getter:
    public Industries getIndustries() {
        return industries;
    }

    public Market getMarket() {
        return market;
    }

    public ConstructionSector getConstructionSector() {
        return constructionSector;
    }

    // EFFECTS: return Json representation of Economy
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("market", market.toJson());
        json.put("industry", industries.toJson());
        json.put("construction", constructionSector.toJson());
        return json;
    }

    // EFFECTS: return true if all fields are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Economy economy = (Economy) o;
        return getIndustries().equals(economy.getIndustries()) && getMarket().equals(economy.getMarket())
                && getConstructionSector().equals(economy.getConstructionSector());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndustries(), getMarket(), getConstructionSector());
    }
}
