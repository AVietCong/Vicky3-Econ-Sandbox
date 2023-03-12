package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;
    private Market market;
    private Industries industry;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: read economy from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Economy read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseEconomy(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses economy from JSON object and returns it
    private Economy parseEconomy(JSONObject jsonObject) {
        market = parseMarket(jsonObject.getJSONObject("market"));
        industry = parseIndustry(jsonObject.getJSONObject("industry"));
        ConstructionSector sector = parseConstruction(jsonObject.getJSONObject("construction"));

        return new Economy(industry, market, sector);
    }

    // EFFECTS: parses market from JSON object and returns it
    private Market parseMarket(JSONObject jsonObject) {
        Market market = new Market();
        JSONArray jsonArray = jsonObject.getJSONArray("market");
        for (Object json : jsonArray) {
            JSONObject nextGoods = (JSONObject) json;
            addGoods(market, nextGoods);
        }
        return market;
    }

    // MODIFIES: market
    // EFFECTS: parses goods from JSON object and add them to market
    private void addGoods(Market market, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int basePrice = jsonObject.getInt("base");
        int demand = jsonObject.getInt("demand");
        int supply = jsonObject.getInt("supply");
        Goods.GoodsType type = Goods.GoodsType.valueOf(jsonObject.getString("type"));
        Goods goods = new Goods(name, basePrice, type);
        goods.setSupply(supply);
        goods.setDemand(demand);
        market.addGoods(goods);
    }

    // MODIFIES: listOfGoods
    // EFFECTS: parses name of goods from JSON array, find the good in market that has already been parsed
    // and add it to list
    private void addGoods(List<Goods> listOfGoods, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextGoods = (JSONObject) json;
            String name = nextGoods.getString("name");
            int index = market.getAvailableGoods().indexOf(name);
            listOfGoods.add(market.getAllGoods().get(index));
        }
    }

    // EFFECTS: parses industry from JSON object and returns it
    private Industries parseIndustry(JSONObject jsonObject) {
        Industries industry = new Industries();
        JSONArray jsonArray = jsonObject.getJSONArray("industries");
        for (Object json : jsonArray) {
            JSONObject nextBuilding = (JSONObject) json;
            addBuilding(industry, nextBuilding);
        }
        return industry;
    }

    // MODIFIES: listOfBuildings
    // EFFECTS: parses name of buildings from JSON array, find the building in industry that has already been parsed
    // and add it to list
    private void addBuilding(List<Building> listOfBuildings, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextBuilding = (JSONObject) json;
            String name = nextBuilding.getString("name");
            int index = industry.getAllBuildingNames().indexOf(name);
            listOfBuildings.add(industry.getAllBuildings().get(index));
        }
    }

    // MODIFIES: industry
    // EFFECTS: parses building from JSON object and add them to industry
    private void addBuilding(Industries industry, JSONObject jsonObject) {
        List<Goods> inputGoods = new ArrayList<>();
        List<Goods> outputGoods = new ArrayList<>();
        List<Integer> inputAmount = new ArrayList<>();
        List<Integer> outputAmount = new ArrayList<>();
        String name = jsonObject.getString("name");
        int size = jsonObject.getInt("size");
        int income = jsonObject.getInt("income");
        int expense = jsonObject.getInt("expense");
        int constructionCost = jsonObject.getInt("cost");
        addGoods(inputGoods, jsonObject.getJSONArray("input goods"));
        addGoods(outputGoods, jsonObject.getJSONArray("output goods"));
        addAmount(inputAmount, jsonObject.getJSONArray("input amount"));
        addAmount(outputAmount, jsonObject.getJSONArray("output amount"));
        Building building = new Building(name, constructionCost, inputGoods, inputAmount, outputGoods, outputAmount);
        building.setSize(size);
        building.setExpense(expense);
        building.setIncome(income);
        industry.add(building);
    }

    // MODIFIES: listOfAmount
    // EFFECTS: parses integer amounts from JSON array then add to list
    private void addAmount(List<Integer> listOfAmount, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            int amount = jsonArray.getInt(i);
            listOfAmount.add(amount);
        }
    }

    // EFFECTS: parses constructionSector from JSON object and returns it
    private ConstructionSector parseConstruction(JSONObject jsonObject) {
        List<Integer> constructionValue = new ArrayList<>();
        List<Integer> inputAmount = new ArrayList<>();
        List<Goods> inputGoods = new ArrayList<>();
        List<Building> queue = new ArrayList<>();
        int expense = jsonObject.getInt("expense");
        addAmount(constructionValue, jsonObject.getJSONArray("value"));
        addAmount(inputAmount, jsonObject.getJSONArray("input amount"));
        addGoods(inputGoods, jsonObject.getJSONArray("input"));
        addBuilding(queue, jsonObject.getJSONArray("queue"));

        ConstructionSector sector = new ConstructionSector(inputGoods, inputAmount);
        sector.setExpense(expense);
        sector.setConstructionQueue(queue);
        sector.setConstructionValue(constructionValue);
        return sector;
    }
}
