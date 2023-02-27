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
    Building ironMine;
    Building foodIndustry;
    int wages;

    @BeforeEach
    public void setup() {
        wages = constructionSector.WAGES;
        fertilizer = new Goods("Fertilizer", 30);
        grain = new Goods("Grain", 20);
        groceries = new Goods("Groceries", 30);
        steel = new Goods("Steel", 50);
        iron = new Goods("Iron", 40);
        coal = new Goods("Coal", 30);
        explosives = new Goods("Explosives", 50);
        tools = new Goods("Tools", 40);
        farm = new Building("Farm", 50, Arrays.asList(fertilizer), Arrays.asList(15),
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
    public void testConstructor() {
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
        assertEquals(0, constructionSector.getExpense());
        assertEquals(Arrays.asList(steel, tools, explosives), constructionSector.getInputGoods());
        assertEquals(Arrays.asList(70, 20, 20), constructionSector.getInputAmount());
    }

    @Test
    public void testaddOnce() {
        constructionSector.build(farm);
        assertTrue(constructionSector.getConstructionQueue().contains(farm));
        assertEquals(1, constructionSector.getConstructionValue().size());
        assertTrue(constructionSector.getConstructionQueue().size()
                == constructionSector.getConstructionValue().size());
    }

    @Test
    public void testaddMultiple() {
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
    public void testremoveEmptyQueue() {
        assertFalse(constructionSector.remove(farm));
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    public void testremoveOnceInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);

        assertTrue(constructionSector.remove(farm));
        assertEquals(Arrays.asList(ironMine, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300, 450), constructionSector.getConstructionValue());
    }

    @Test
    public void testremoveNotInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);

        assertFalse(constructionSector.remove(new Building("Tools Workshop", 450,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())));
        assertEquals(Arrays.asList(ironMine, farm, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300, 50, 450), constructionSector.getConstructionValue());
    }

    @Test
    public void testremoveOnceFirstInQueue() {
        constructionSector.build(ironMine);
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        constructionSector.build(ironMine);

        assertTrue(constructionSector.remove(ironMine));
        assertEquals(Arrays.asList(farm, foodIndustry, ironMine), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(50, 450, 300), constructionSector.getConstructionValue());
    }

    @Test
    public void testremoveMultipleOfOneBuilding() {
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
    public void testremoveMultiple() {
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
    public void testconsumeOnce() {
        constructionSector.consume();

        assertEquals(70, steel.getDemand());
        assertEquals(20, tools.getDemand());
        assertEquals(20, explosives.getDemand());
    }

    @Test
    public void testconsumeTwice() {
        constructionSector.consume();
        constructionSector.consume();
        assertEquals(2 * 70, steel.getDemand());
        assertEquals(2 * 20, tools.getDemand());
        assertEquals(2 * 20, explosives.getDemand());
    }

    @Test
    public void testpayExpenseOnce() {
        int expected = steel.determinePrice() * 70
                + tools.determinePrice() * 20
                + explosives.determinePrice() * 20
                + wages;
        constructionSector.payExpense();
        assertEquals(expected, constructionSector.getExpense());
    }

    @Test
    public void testpayExpenseTwice() {
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
    public void testpayWages() {
        constructionSector.payWages();
        assertEquals(wages, constructionSector.getExpense());
    }


    @Test
    public void testbuyGoods() {
        steel.setDemand(400);
        steel.setSupply(300);
        explosives.setDemand(200);
        explosives.setSupply(250);

        int expected = steel.determinePrice() * 70 + explosives.determinePrice() * 20 + tools.determinePrice() * 20;
        constructionSector.buyGoods();
        assertEquals(expected, constructionSector.getExpense());
    }

    @Test
    public void testconstructEmptyQueue() {
        assertTrue(constructionSector.construct().isEmpty());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    public void testconstructOneInQueueFinished()  {
        constructionSector.build(farm);
        assertEquals(Arrays.asList(farm), constructionSector.construct());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    public void testconstructThreeInQueueFinished()  {
        constructionSector.build(farm);
        constructionSector.build(farm);
        constructionSector.build(farm);
        assertEquals(Arrays.asList(farm, farm, farm), constructionSector.construct());
        assertTrue(constructionSector.getConstructionQueue().isEmpty());
        assertTrue(constructionSector.getConstructionValue().isEmpty());
    }

    @Test
    public void testconstructOneUnfinished() {
        constructionSector.build(ironMine);
        constructionSector.build(foodIndustry);
        assertTrue(constructionSector.construct().isEmpty());
        assertEquals(Arrays.asList(ironMine, foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(300 - 150, 450), constructionSector.getConstructionValue());
    }

    @Test
    public void testconstructFinishedAndUnfinished() {
        constructionSector.build(farm);
        constructionSector.build(foodIndustry);
        assertEquals(Arrays.asList(farm), constructionSector.construct());
        assertEquals(Arrays.asList(foodIndustry), constructionSector.getConstructionQueue());
        assertEquals(Arrays.asList(350), constructionSector.getConstructionValue());
    }
}
