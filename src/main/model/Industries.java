package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Industries {
    // This class represents all the buildings that have been constructed

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

    public List<Integer> getAllProfit() {
        List<Integer> result = new ArrayList<>();
        for (Building b : industries) {
            result.add(b.determineProfit());
        }
        return result;
    }

    // MODIFIES: this
    // EFFECTS: remove buildings with size 0 from industries
    public void removeEmptyBuildings() {
        for (Iterator<Building> iterator = industries.iterator(); iterator.hasNext();) {
            Building building = iterator.next();
            if (building.getSize() == 0) {
                iterator.remove();
            }
        }
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
}
