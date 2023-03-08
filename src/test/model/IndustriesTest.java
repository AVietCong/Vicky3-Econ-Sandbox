package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndustriesTest {
    Industries industries;

    Building farm;
    Goods fertilizer;
    Goods grain;

    Building steelMill;
    Goods iron;
    Goods coal;
    Goods steel;

    Building chemicalPlant;
    Goods sulphur;
    Goods explosives;

    @BeforeEach
    public void setup() {
        industries = new Industries();

        fertilizer = new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL);
        grain = new Goods("Grain", 20, Goods.GoodsType.CONSUMER);
        farm = new Building("Farm", 50, Arrays.asList(fertilizer), Arrays.asList(15),
                Arrays.asList(grain), Arrays.asList(90));

        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        coal = new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL);
        steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
        steelMill = new Building("Steel Mill", 450, Arrays.asList(iron,coal), Arrays.asList(90, 30),
                Arrays.asList(steel), Arrays.asList(120));

        sulphur = new Goods("Sulphur", 50, Goods.GoodsType.INDUSTRIAL);
        explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
        chemicalPlant = new Building("Chemical Plant", 450,
                Arrays.asList(sulphur, iron, coal), Arrays.asList(60, 30, 15),
                Arrays.asList(fertilizer, explosives), Arrays.asList(110, 70));
    }

    @Test
    public void testConstructor() {
        assertTrue(industries.getAllBuildingNames().isEmpty());
    }

    @Test
    public void testaddNewBuiding() {
        assertTrue(industries.add(farm));
        assertTrue(industries.getAllBuildingNames().size() == 1);
        assertTrue(industries.getAllBuildingNames().contains("Farm"));
    }

    @Test
    public void testaddExistingBuiding() {
        industries.add(farm);

        assertFalse(industries.add(new Building("Farm", 100,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>())));
        assertTrue(industries.getAllBuildingNames().size() == 1);
        assertTrue(industries.getAllBuildingNames().contains("Farm"));
    }

    @Test
    public void testgetAllBuildingsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(farm), industries.getAllBuildings());
    }

    @Test
    public void testgetAllBuildingsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(steelMill, farm, chemicalPlant), industries.getAllBuildings());
    }

    @Test
    public void testgetAllBuildingNamesOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList("Farm"), industries.getAllBuildingNames());
    }

    @Test
    public void testgetAllBuildingNamesMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList("Steel Mill", "Farm", "Chemical Plant"), industries.getAllBuildingNames());
    }

    @Test
    public void testgetAllSizeOneBuilding() {
        farm.setSize(10);
        industries.add(farm);
        assertEquals(Arrays.asList(10), industries.getAllSize());
    }

    @Test
    public void testgetAllSizeMultipleBuilding() {
        steelMill.setSize(5);
        farm.setSize(6);
        chemicalPlant.setSize(2);
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(5,6,2), industries.getAllSize());
    }

    @Test
    public void testgetAllInputGoodsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(fertilizer)), industries.getAllInputGoods());
    }

    @Test
    public void testgetAllInputGoodsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(iron, coal),
                Arrays.asList(fertilizer),
                Arrays.asList(sulphur, iron, coal)),
                industries.getAllInputGoods());
    }

    @Test
    public void testgetAllInputGoodsAmountOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(15)), industries.getAllInputGoodsAmount());
    }

    @Test
    public void testgetAllInputGoodsAmountMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(90, 30),
                        Arrays.asList(15),
                        Arrays.asList(60, 30, 15)),
                industries.getAllInputGoodsAmount());
    }

    @Test
    public void testgetAllOutputGoodsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(grain)), industries.getAllOutputGoods());
    }

    @Test
    public void testgetAllOutputGoodsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(steel),
                        Arrays.asList(grain),
                        Arrays.asList(fertilizer, explosives)),
                industries.getAllOutputGoods());
    }

    @Test
    public void testgetAllOutputGoodsAmountOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(90)), industries.getAllOutputGoodsAmount());
    }

    @Test
    public void testgetAllOutputGoodsAmountMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(120),
                        Arrays.asList(90),
                        Arrays.asList(110, 70)),
                industries.getAllOutputGoodsAmount());
    }

    @Test
    public void testgetAllExpensesOneBuilding() {
        industries.add(farm);
        farm.payExpense();
        int expected = farm.getExpense();
        assertEquals(Arrays.asList(expected), industries.getAllExpenses());
    }

    @Test
    public void testgetAllExpensesMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        steelMill.payExpense();
        farm.payExpense();
        chemicalPlant.payExpense();
        List<Integer> expected = Arrays.asList(steelMill.getExpense(), farm.getExpense(), chemicalPlant.getExpense());
        assertEquals(expected, industries.getAllExpenses());
    }

    @Test
    public void testgetAllIncomeOneBuilding() {
        industries.add(farm);
        farm.sellGoods();
        int expected = farm.getIncome();
        assertEquals(Arrays.asList(expected), industries.getAllIncome());
    }

    @Test
    public void testgetAllIncomeMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        steelMill.sellGoods();
        farm.sellGoods();
        chemicalPlant.sellGoods();
        List<Integer> expected = Arrays.asList(steelMill.getIncome(), farm.getIncome(), chemicalPlant.getIncome());
        assertEquals(expected, industries.getAllIncome());
    }

    @Test
    public void testremoveEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);

        industries.removeEmptyBuildings();
        assertEquals(Arrays.asList("Steel Mill", "Chemical Plant"), industries.getAllBuildingNames());
    }

    @Test
    public void testremoveMultipleEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);
        steelMill.downsize(1);

        industries.removeEmptyBuildings();
        assertEquals(Arrays.asList("Chemical Plant"), industries.getAllBuildingNames());
    }
}
