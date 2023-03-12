package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructionSectorTest {

    ConstructionSector constructionSector;
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
    int wages;

    @BeforeEach
    void setup() {
        wages = constructionSector.WAGES;
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

        constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives), Arrays.asList(70, 20, 20));
    }

    @Test
    void testConstructor() {
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
        assertEquals(0, constructionSector.getExpense());
        assertEquals(Arrays.asList(steel, tools, explosives), constructionSector.getInputGoods());
        assertEquals(Arrays.asList(70, 20, 20), constructionSector.getInputAmount());
    }

    @Test
    void testsetExpense() {
        constructionSector.setExpense(5000);
        assertEquals(5000, constructionSector.getExpense());
    }

    @Test
    void testsetConstructionQueue() {
        constructionSector.setConstructionQueue(Arrays.asList(farm, foodIndustry, ironMine));
        assertEquals(Arrays.asList(farm, foodIndustry, ironMine), constructionSector.getConstructionQueue());
    }

    @Test
    void testsetConstructionValue() {
        constructionSector.setConstructionValue(Arrays.asList(150, 300, 200));
        assertEquals(Arrays.asList(150, 300, 200), constructionSector.getConstructionValue());
    }

    @Test
    void testaddOnce() {
        constructionSector.build(farm);
        assertTrue(constructionSector.getConstructionQueue().contains(farm));
        assertEquals(1, constructionSector.getConstructionValue().size());
        assertTrue(constructionSector.getConstructionQueue().size()
                == constructionSector.getConstructionValue().size());
    }

    @Test
    void testaddMultiple() {
        constructionSector.build(farm);
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        assertTrue(constructionSector.getConstructionQueue().contains(farm));
        assertTrue(constructionSector.getConstructionQueue().contains(ironMine));
        assertEquals(Arrays.asList(farm, ironMine, farm), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(50, 300, 50), constructionSector.getConstructionValue());
        assertTrue(constructionSector.getConstructionQueue().size()
                == constructionSector.getConstructionValue().size());
    }

    @Test
    void testremoveEmptyQueue() {
        assertFalse(constructionSector.remove(farm));
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    void testremoveOnceInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);

        assertTrue(constructionSector.remove(farm));
        assertEquals(Arrays.asList(ironMine, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300, 450), constructionSector.getConstructionValue());
    }

    @Test
    void testremoveNotInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);

        assertFalse(constructionSector.remove(new Building("Tools Workshop", 450,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));
        assertEquals(Arrays.asList(ironMine, farm, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300, 50, 450), constructionSector.getConstructionValue());
    }

    @Test
    void testremoveOnceFirstInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.build(ironMine);

        assertTrue(constructionSector.remove(ironMine));
        assertEquals(Arrays.asList(farm, foodIndustry, ironMine), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(50, 450, 300), constructionSector.getConstructionValue());
    }

    @Test
    void testremoveMultipleOfOneBuilding() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.build(foodIndustry);

        assertTrue(constructionSector.remove(foodIndustry));
        assertTrue(constructionSector.remove(foodIndustry));
        assertEquals(Arrays.asList(ironMine, farm), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300, 50), constructionSector.getConstructionValue());
    }

    @Test
    void testremoveMultiple() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.build(foodIndustry);

        assertTrue(constructionSector.remove(ironMine));
        assertTrue(constructionSector.remove(foodIndustry));
        assertEquals(Arrays.asList(farm, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(50, 450), constructionSector.getConstructionValue());
    }

    @Test
    void testconsumeOnce() {
        constructionSector.consume();

        assertEquals(70, steel.getDemand());
        assertEquals(20, tools.getDemand());
        assertEquals(20, explosives.getDemand());
    }

    @Test
    void testconsumeTwice() {
        constructionSector.consume();
        constructionSector.consume();
        assertEquals(2 * 70, steel.getDemand());
        assertEquals(2 * 20, tools.getDemand());
        assertEquals(2 * 20, explosives.getDemand());
    }

    @Test
    void testpayExpenseOnce() {
        int expected = steel.determinePrice() * 70
                + tools.determinePrice() * 20
                + explosives.determinePrice() * 20
                + wages;
        constructionSector.payExpense();
        assertEquals(expected, constructionSector.getExpense());
    }

    @Test
    void testpayExpenseTwice() {
        int expected = steel.determinePrice() * 70
                + tools.determinePrice() * 20
                + explosives.determinePrice() * 20
                + wages;
        constructionSector.payExpense();
        assertEquals(expected, constructionSector.getExpense());
        constructionSector.payExpense();
        assertEquals(expected, constructionSector.getExpense());
    }

    @Test
    void testpayWages() {
        constructionSector.payWages();
        assertEquals(wages, constructionSector.getExpense());
    }


    @Test
    void testbuyGoods() {
        steel.setDemand(400);
        steel.setSupply(300);
        explosives.setDemand(200);
        explosives.setSupply(250);

        int expected = steel.determinePrice() * 70 + explosives.determinePrice() * 20 + tools.determinePrice() * 20;
        constructionSector.buyGoods();
        assertEquals(expected, constructionSector.getExpense());
    }

    @Test
    void testconstructEmptyQueue() {
        assertTrue(constructionSector.construct().isEmpty());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    void testconstructOneInQueueFinished()  {
        constructionSector.build(farm);
        assertEquals(Arrays.asList(farm), constructionSector.construct());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    void testconstructThreeInQueueFinished()  {
        constructionSector.build(farm);
        constructionSector.build(farm);
        constructionSector.build(farm);
        assertEquals(Arrays.asList(farm, farm, farm), constructionSector.construct());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    void testconstructOneUnfinished() {
        constructionSector.build(ironMine);
        constructionSector.build(foodIndustry);
        assertTrue(constructionSector.construct().isEmpty());
        assertEquals(Arrays.asList(ironMine, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300 - 150, 450), constructionSector.getConstructionValue());
    }

    @Test
    void testconstructFinishedAndUnfinished() {
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        assertEquals(Arrays.asList(farm), constructionSector.construct());
        assertEquals(Arrays.asList(foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(350), constructionSector.getConstructionValue());
    }

    @Test
    void testtoJson() {
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.payExpense();
        assertEquals("{\"input\":[{\"name\":\"Steel\",\"type\":\"INDUSTRIAL\",\"supply\":0," +
                        "\"demand\":0,\"base\":50},{\"name\":\"Tools\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":40}," +
                        "{\"name\":\"Explosives\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":50}]," +
                        "\"expense\":10300,\"value\":[50,450],\"input amount\":[70,20,20]," +
                        "\"queue\":[{\"income\":0,\"cost\":50," +
                        "\"input goods\":[{\"name\":\"Fertilizer\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":30}]," +
                        "\"size\":1,\"output amount\":[90],\"name\":\"Farm\",\"expense\":0," +
                        "\"output goods\":[{\"name\":\"Grain\",\"type\":\"CONSUMER\",\"supply\":0,\"demand\":0,\"base\":20}]," +
                        "\"input amount\":[15]},{\"income\":0,\"cost\":450," +
                        "\"input goods\":[{\"name\":\"Grain\",\"type\":\"CONSUMER\",\"supply\":0,\"demand\":0,\"base\":20}," +
                        "{\"name\":\"Iron\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":40}]," +
                        "\"size\":1,\"output amount\":[65],\"name\":\"Food Industry\",\"expense\":0," +
                        "\"output goods\":[{\"name\":\"Groceries\",\"type\":\"CONSUMER\",\"supply\":0,\"demand\":0,\"base\":30}]," +
                        "\"input amount\":[20,10]}]}", constructionSector.toJson().toString());
    }

    @Test
    void testEquals() {
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.payExpense();

        assertTrue(constructionSector.equals(constructionSector));
        assertFalse(constructionSector.equals(null));
        assertFalse(constructionSector.equals("Construction Sector"));

        ConstructionSector expectedEqual = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(farm);
        expectedEqual.build(foodIndustry);
        expectedEqual.payExpense();
        assertTrue(constructionSector.equals(expectedEqual));

        ConstructionSector differentInputGoods = new ConstructionSector(Arrays.asList(grain, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(farm);
        expectedEqual.build(foodIndustry);
        expectedEqual.payExpense();
        assertFalse(constructionSector.equals(differentInputGoods));

        ConstructionSector differentInputAmount = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 10, 20));
        expectedEqual.build(farm);
        expectedEqual.build(foodIndustry);
        expectedEqual.payExpense();
        assertFalse(constructionSector.equals(differentInputAmount));

        ConstructionSector differentExpense = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(farm);
        expectedEqual.build(foodIndustry);
        assertFalse(constructionSector.equals(differentExpense));

        ConstructionSector differentQueue = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(ironMine);
        expectedEqual.build(foodIndustry);
        expectedEqual.payExpense();
        assertFalse(constructionSector.equals(differentQueue));

        constructionSector.remove(farm);
        constructionSector.build(ironMine);
        ConstructionSector differentValue = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(foodIndustry);
        expectedEqual.build(ironMine);
        expectedEqual.payExpense();
        expectedEqual.construct();
        assertFalse(constructionSector.equals(differentValue));
    }

    @Test
    void testHashCode() {
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.payExpense();
        ConstructionSector expectedEqual = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
        expectedEqual.build(farm);
        expectedEqual.build(foodIndustry);
        expectedEqual.payExpense();

        assertEquals(constructionSector.hashCode(), expectedEqual.hashCode());
    }
}
