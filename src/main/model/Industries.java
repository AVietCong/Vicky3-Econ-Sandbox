package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// This class represents all the buildings that have been constructed

public class Industries implements Writable {


    private List<Building> industries;

    public Industries() {
        industries = new ArrayList<>();
    }

    public List<Building> getAllBuildings() {
        return industries;
    }

    // EFFECTS: return the name of all buildings in the order they were added
    public List<String> getAllBuildingNames() {
        List<String> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getName());
        }
        return result;
    }

    // EFFECTS: return the size of all buildings in the order they were added
    public List<Integer> getAllSize() {
        List<Integer> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getSize());
        }
        return result;
    }

    // EFFECTS: return lists of input goods of all buildings in the order they were added
    public List<List<Goods>> getAllInputGoods() {
        List<List<Goods>> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getInputGoods());
        }
        return result;
    }

    // EFFECTS: return lists of amount of input goods of all buildings in the order they were added
    public List<List<Integer>> getAllInputGoodsAmount() {
        List<List<Integer>> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getInputAmount());
        }
        return result;
    }

    // EFFECTS: return lists of output goods of all buildings in the order they were added
    public List<List<Goods>> getAllOutputGoods() {
        List<List<Goods>> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getOutputGoods());
        }
        return result;
    }

    // EFFECTS: return lists of amount of output goods of all buildings in the order they were added
    public List<List<Integer>> getAllOutputGoodsAmount() {
        List<List<Integer>> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getOutputAmount());
        }
        return result;
    }

    // EFFECTS: return expenses of all buildings in the order they were added
    public List<Integer> getAllExpenses() {
        List<Integer> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getExpense());
        }
        return result;
    }

    // EFFECTS: return income of all buildings in the order they were added
    public List<Integer> getAllIncome() {
        List<Integer> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.getIncome());
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: return industry with only active building (size != 0)
    public Industries removeEmptyBuildings() {
        Industries activeBuildings = new Industries();
        for (Building building : industries) {
            if (building.getSize() != 0) {
                activeBuildings.add(building);
            }
        }
        return activeBuildings;
    }

    // REQUIRES: building of same name is not already in industries
    // MODIFIES: this
    // EFFECTS: add building to industries and return true if industries.size() has changes; otherwise return false
    public boolean add(Building b) {
        if (getAllBuildingNames().contains(b.getName())) {
            return false;
        }
        industries.add(b);
        return true;
    }

    // EFFECT: return industries as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Building building : industries) {
            jsonArray.put(building.toJson());
        }
        json.put("industries", jsonArray);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Industries that = (Industries) o;
        return Objects.equals(industries, that.industries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(industries);
    }
}
