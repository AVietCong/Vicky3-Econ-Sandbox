package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EconomyTest {

    Economy economy;
    Goods steel;
    Goods explosives;
    Goods tools;
    Goods fertilizer;
    Goods grain;
    Goods groceries;
    Goods iron;
    Goods coal;
    Building farm;
    Building otherFarm;
    Building ironMine;
    Building foodIndustry;

    @BeforeEach
    void setup() {
        fertilizer = new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL);
        grain = new Goods("Grain", 20, Goods.GoodsType.CONSUMER);
        groceries = new Goods("Groceries", 30, Goods.GoodsType.CONSUMER);
        steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        coal = new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL);
        explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
        tools = new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL);
        farm = new Building("Farm", 50, Arrays.asList(fertilizer), Arrays.asList(15),
                Arrays.asList(grain), Arrays.asList(90));
        otherFarm = new Building("Farm", 450, Arrays.asList(fertilizer), Arrays.asList(15),
                Arrays.asList(grain), Arrays.asList(90));
        ironMine = new Building("Iron Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(iron), Arrays.asList(80));
        foodIndustry = new Building("Food Industry", 450,
                Arrays.asList(grain, iron), Arrays.asList(20, 10),
                Arrays.asList(groceries), Arrays.asList(65));

        Market market = new Market();
        market.addGoods(steel);
        market.addGoods(explosives);
        market.addGoods(tools);

        Industries industries = new Industries();
        industries.add(ironMine);
        industries.add(foodIndustry);
        ConstructionSector constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        constructionSector.build(farm);
        economy = new Economy(industries, market, constructionSector);
    }

    @Test
    void testConstructor() {
        Market expectedMarket = new Market();
        expectedMarket.addGoods(steel);
        expectedMarket.addGoods(explosives);
        expectedMarket.addGoods(tools);

        Industries expectedIndustries = new Industries();
        expectedIndustries.add(ironMine);
        expectedIndustries.add(foodIndustry);

        ConstructionSector expectedCS = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedCS.build(farm);

        assertEquals(expectedMarket,economy.getMarket());
        assertEquals(expectedIndustries, economy.getIndustries());
        assertEquals(expectedCS, economy.getConstructionSector());
    }
}
