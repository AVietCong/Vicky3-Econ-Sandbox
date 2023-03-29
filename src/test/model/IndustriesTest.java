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
    void testConstructor() {
        assertTrue(industries.getAllBuildingNames().isEmpty());
    }

    @Test
    void testaddNewBuiding() {
        assertTrue(industries.add(farm));
        assertTrue(industries.getAllBuildingNames().size() == 1);
        assertTrue(industries.getAllBuildingNames().contains("Farm"));
    }

    @Test
    void testaddExistingBuiding() {
        industries.add(farm);

        assertFalse(industries.add(new Building("Farm", 100,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>())));
        assertTrue(industries.getAllBuildingNames().size() == 1);
        assertTrue(industries.getAllBuildingNames().contains("Farm"));
    }

    @Test
    void testgetAllBuildingsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(farm), industries.getAllBuildings());
    }

    @Test
    void testgetAllBuildingsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(steelMill, farm, chemicalPlant), industries.getAllBuildings());
    }

    @Test
    void testgetAllBuildingNamesOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList("Farm"), industries.getAllBuildingNames());
    }

    @Test
    void testgetAllBuildingNamesMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList("Steel Mill", "Farm", "Chemical Plant"), industries.getAllBuildingNames());
    }

    @Test
    void testgetAllSizeOneBuilding() {
        farm.setSize(10);
        industries.add(farm);
        assertEquals(Arrays.asList(10), industries.getAllSize());
    }

    @Test
    void testgetAllSizeMultipleBuilding() {
        steelMill.setSize(5);
        farm.setSize(6);
        chemicalPlant.setSize(2);
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(5,6,2), industries.getAllSize());
    }

    @Test
    void testgetAllInputGoodsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(fertilizer)), industries.getAllInputGoods());
    }

    @Test
    void testgetAllInputGoodsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(iron, coal),
                Arrays.asList(fertilizer),
                Arrays.asList(sulphur, iron, coal)),
                industries.getAllInputGoods());
    }

    @Test
    void testgetAllInputGoodsAmountOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(15)), industries.getAllInputGoodsAmount());
    }

    @Test
    void testgetAllInputGoodsAmountMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(90, 30),
                        Arrays.asList(15),
                        Arrays.asList(60, 30, 15)),
                industries.getAllInputGoodsAmount());
    }

    @Test
    void testgetAllOutputGoodsOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(grain)), industries.getAllOutputGoods());
    }

    @Test
    void testgetAllOutputGoodsMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(steel),
                        Arrays.asList(grain),
                        Arrays.asList(fertilizer, explosives)),
                industries.getAllOutputGoods());
    }

    @Test
    void testgetAllOutputGoodsAmountOneBuilding() {
        industries.add(farm);
        assertEquals(Arrays.asList(Arrays.asList(90)), industries.getAllOutputGoodsAmount());
    }

    @Test
    void testgetAllOutputGoodsAmountMultipleBuilding() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);
        assertEquals(Arrays.asList(Arrays.asList(120),
                        Arrays.asList(90),
                        Arrays.asList(110, 70)),
                industries.getAllOutputGoodsAmount());
    }

    @Test
    void testgetAllExpensesOneBuilding() {
        industries.add(farm);
        farm.payExpense();
        int expected = farm.getExpense();
        assertEquals(Arrays.asList(expected), industries.getAllExpenses());
    }

    @Test
    void testgetAllExpensesMultipleBuilding() {
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
    void testgetAllIncomeOneBuilding() {
        industries.add(farm);
        farm.sellGoods();
        int expected = farm.getIncome();
        assertEquals(Arrays.asList(expected), industries.getAllIncome());
    }

    @Test
    void testgetAllIncomeMultipleBuilding() {
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
    void testremoveEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);

        assertEquals(Arrays.asList("Steel Mill", "Chemical Plant"), industries.removeEmptyBuildings().getAllBuildingNames());
    }

    @Test
    void testremoveMultipleEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);
        steelMill.downsize(1);

        assertEquals(Arrays.asList("Chemical Plant"), industries.removeEmptyBuildings().getAllBuildingNames());
    }

    @Test
    void testreturnEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);

        assertEquals(Arrays.asList("Farm"), industries.returnEmptyBuildings().getAllBuildingNames());
    }

    @Test
    void testreturnMultipleEmptyBuildings() {
        industries.add(steelMill);
        industries.add(farm);
        industries.add(chemicalPlant);

        farm.downsize(1);
        steelMill.downsize(1);

        assertEquals(Arrays.asList("Steel Mill", "Farm"), industries.returnEmptyBuildings().getAllBuildingNames());
    }

    @Test
    void testtoJson() {
        industries.add(steelMill);
        industries.add(farm);
        assertEquals("{\"industries\":[{\"income\":0,\"cost\":450," +
                "\"input goods\":[{\"name\":\"Iron\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":40}," +
                "{\"name\":\"Coal\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":30}]," +
                "\"size\":1,\"output amount\":[120],\"name\":\"Steel Mill\",\"expense\":0," +
                "\"output goods\":[{\"name\":\"Steel\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0,\"base\":50}]," +
                "\"input amount\":[90,30]},{\"income\":0,\"cost\":50," +
                "\"input goods\":[{\"name\":\"Fertilizer\",\"type\":\"INDUSTRIAL\",\"supply\":0,\"demand\":0," +
                "\"base\":30}],\"size\":1,\"output amount\":[90],\"name\":\"Farm\",\"expense\":0," +
                "\"output goods\":[{\"name\":\"Grain\",\"type\":\"CONSUMER\",\"supply\":0,\"demand\":0,\"base\":20}]," +
                "\"input amount\":[15]}]}",
                industries.toJson().toString());
    }

    @Test
    void testEquals() {
        industries.add(steelMill);
        industries.add(farm);
        assertTrue(industries.equals(industries));
        assertFalse(industries.equals(null));
        assertFalse(industries.equals("Test"));

        Industries expectedEquals = new Industries();
        expectedEquals.add(steelMill);
        expectedEquals.add(farm);
        assertTrue(industries.equals(industries));

        Industries differentBuildings = new Industries();
        differentBuildings.add(steelMill);
        differentBuildings.add(chemicalPlant);
        assertFalse(industries.equals(differentBuildings));
    }

    @Test
    void testHashCode() {
        industries.add(steelMill);
        industries.add(farm);
        Industries expectedEquals = new Industries();
        expectedEquals.add(steelMill);
        expectedEquals.add(farm);

        assertEquals(industries.hashCode(), expectedEquals.hashCode());
    }
}
