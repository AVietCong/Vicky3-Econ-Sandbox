package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {

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

    int wagePerLevel;

    @BeforeEach
    void setup() {
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

        wagePerLevel = farm.WAGES_PER_LEVEL;
    }

    @Test
    void testConstructor() {
        assertEquals("Farm", farm.getName());
        assertEquals(50, farm.getConstructionCost());
        assertEquals(1, farm.getSize());
        assertEquals(0, farm.getIncome());
        assertEquals(0, farm.getExpense());
        assertEquals(Arrays.asList(fertilizer), farm.getInputGoods());
        assertEquals(Arrays.asList(grain), farm.getOutputGoods());
        assertEquals(Arrays.asList(15), farm.getInputAmount());
        assertEquals(Arrays.asList(90), farm.getOutputAmount());
    }

    @Test
    void testsetSize() {
        farm.setSize(10);
        assertEquals(10, farm.getSize());
    }

    @Test
    void testconsumeOnce() {
        farm.consume();
        assertEquals(15, fertilizer.getDemand());

        steelMill.consume();
        assertEquals(90, iron.getDemand());
        assertEquals(30, coal.getDemand());
    }

    @Test
    void testconsumeTwice() {
        steelMill.consume();
        steelMill.consume();
        assertEquals(2 * 90, iron.getDemand());
        assertEquals(2 * 30, coal.getDemand());
    }

    @Test
    void testconsumeZero() {
        steelMill.downsize(1);
        steelMill.consume();
        assertEquals(0, iron.getDemand());
        assertEquals(0, coal.getDemand());
    }


    @Test
    void testconsumeTwoBuildingsSameResource() {
        steelMill.consume();
        chemicalPlant.consume();
        assertEquals(120, iron.getDemand());
    }

    @Test
    void testconsumeLevel51() {
        for (int i = 0; i < 50; i++) {
            farm.expand();
        }

        assertEquals(51, farm.getSize());
        int expected = (int) ((1 + (51 - 1) * 0.01) * 51 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    void testconsumeMaxEoS() {
        for (int i = 0; i < 60; i++) {
            farm.expand();
        }

        assertEquals(61, farm.getSize());
        int expected = (int) (1.5 * 61 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    void testconsumeNormalLevel() {
        for (int i = 0; i < 30; i++) {
            farm.expand();
        }

        assertEquals(31, farm.getSize());
        int expected = (int) (1.3 * 31 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    void testproduceOnce() {
        farm.produce();
        assertEquals(90, grain.getSupply());
    }

    @Test
    void testproduceTwice() {
        farm.produce();
        farm.produce();
        assertEquals(2 * 90, grain.getSupply());
    }

    @Test
    void testproduceZero() {
        farm.downsize(1);
        assertEquals(0, grain.getSupply());
    }

    @Test
    void testproduceLevel51() {
        for (int i = 0; i < 50; i++) {
            farm.expand();
        }

        assertEquals(51, farm.getSize());
        int expected = (int) ((1 + (51 - 1) * 0.01) * 51 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    void testproduceMaxEoS() {
        for (int i = 0; i < 60; i++) {
            farm.expand();
        }

        assertEquals(61, farm.getSize());
        int expected = (int) (1.5 * 61 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    void testproduceNormalLevel() {
        for (int i = 0; i < 30; i++) {
            farm.expand();
        }

        assertEquals(31, farm.getSize());
        int expected = (int) (1.3 * 31 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    void testisProfitable() {
        // Cheap Input -> Expensive Output | Expected to be profitable
        fertilizer.setDemand(100);
        fertilizer.setSupply(125);
        grain.setDemand(400);
        grain.setSupply(500);

        int fertilizerPrice = fertilizer.determinePrice();
        int grainPrice = grain.determinePrice();

        boolean expected = (grainPrice * 90) >= (fertilizerPrice * 15 + wagePerLevel);
        assertEquals(expected, farm.isProfitable());

        // Expensive Input -> Cheap Output | Expected to be unprofitable
        iron.setDemand(500);
        iron.setSupply(300);
        coal.setDemand(400);
        coal.setSupply(200);
        steel.setSupply(120);
        steel.setDemand(40);

        int expense = 90 * iron.determinePrice() + 30 * coal.determinePrice() + wagePerLevel;
        int income = 120 * steel.determinePrice();
        steelMill.buyGoods();
        steelMill.sellGoods();
        assertFalse(steelMill.isProfitable());
    }

    @Test
    void testexpandOnce() {
        assertEquals(1, steelMill.getSize());
        steelMill.expand();
        assertEquals(2, steelMill.getSize());
    }

    @Test
    void testexpandTwice() {
        steelMill.expand();
        steelMill.expand();
        assertEquals(3, steelMill.getSize());
    }

    @Test
    void testdownsizeNormal() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(5));
        assertEquals(5, steelMill.getSize());
    }

    @Test
    void testdownsizeOneLevel() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(1));
        assertEquals(9, steelMill.getSize());
    }

    @Test
    void testdownsizeToZero() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(10));
        assertEquals(0, steelMill.getSize());
    }

    @Test
    void testdownsizeInvalid() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertFalse(steelMill.downsize(11));
        assertEquals(10, steelMill.getSize());
    }

    @Test
    void testdetermineEoSLevelOne() {
        assertEquals(1.0, chemicalPlant.determineEoS());
    }

    @Test
    void testdetermineEoSNormal() {
        for (int i = 0; i < 10; i++) {
            chemicalPlant.expand();
        }
        assertEquals(1.1, chemicalPlant.determineEoS());
    }

    @Test
    void testdetermineEoSMaxLevel() {
        for (int i = 0; i < 51; i++) {
            chemicalPlant.expand();
        }
        assertEquals(1.5, chemicalPlant.determineEoS());
    }

    @Test
    void testpayExpenseOnce() {
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
    }

    @Test
    void testpayExpenseZero() {
        farm.downsize(1);
        assertEquals(0, farm.getExpense());
    }

    @Test
    void testpayExpenseTwice() {
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
    }

    @Test
    void testpayExpenseSize11() {
        for (int i = 0; i < 10; i++) {
            farm.expand();
        }

        double economyOfScale = farm.determineEoS();
        int expected = (int) (11 * 15 * 30 * economyOfScale + wagePerLevel * 11);
        farm.payExpense();
        assertEquals(expected, farm.getExpense());
    }

    @Test
    void testgainIncome() {
        farm.gainIncome();
        assertEquals(90 * 20, farm.getIncome());
        farm.gainIncome();
        assertEquals(90 * 20, farm.getIncome());
    }

    @Test
    void tesgainIncomeZero() {
        farm.downsize(1);
        assertEquals(0, farm.getIncome());
    }

    @Test
    void testpayWagesSize1() {
        chemicalPlant.payWages();
        assertEquals(wagePerLevel, chemicalPlant.getExpense());
    }

    @Test
    void testpayWagesSize11() {
        for (int i = 0; i < 10; i++) {
            chemicalPlant.expand();
        }

        chemicalPlant.payWages();
        assertEquals(wagePerLevel * 11, chemicalPlant.getExpense());
    }

    @Test
    void testbuyGoodsSize1() {
        iron.setDemand(400);
        iron.setSupply(300);
        coal.setDemand(200);
        coal.setSupply(250);

        int ironPrice = iron.determinePrice();
        int coalPrice = coal.determinePrice();
        int expected = ironPrice * 90 + coalPrice * 30;
        steelMill.buyGoods();
        assertEquals(expected, steelMill.getExpense());
    }

    @Test
    void testbuyGoodsSize11() {
        for (int i = 0; i < 10; i++) {
            steelMill.expand();
        }
        iron.setDemand(400);
        iron.setSupply(300);
        coal.setDemand(200);
        coal.setSupply(250);

        double economyOfScale = steelMill.determineEoS();
        int ironPrice = iron.determinePrice();
        int coalPrice = coal.determinePrice();
        int expected = (int) (ironPrice * 90 * 11 * economyOfScale + coalPrice * 11 * 30 * economyOfScale);
        steelMill.buyGoods();
        assertEquals(expected, steelMill.getExpense());
    }


    @Test
    void testtoJson() {
        for (int i = 0; i < 10; i++) {
            steelMill.expand();
        }
        iron.setDemand(400);
        iron.setSupply(300);
        coal.setDemand(200);
        coal.setSupply(250);
        steel.setDemand(200);
        steel.setSupply(100);
        steelMill.sellGoods();
        steelMill.payExpense();

        assertEquals("{\"income\":126324,\"wages\":500,\"cost\":450,\"input goods\":" +
                "[{\"upper cap\":1.75,\"lower cap\":0.25,\"shortage\":false,\"name\":\"Iron\"," +
                "\"type\":\"INDUSTRIAL\",\"supply\":300,\"demand\":400,\"base\":40}," +
                "{\"upper cap\":1.75,\"lower cap\":0.25,\"shortage\":false,\"name\":\"Coal\"," +
                "\"type\":\"INDUSTRIAL\",\"supply\":250,\"demand\":200,\"base\":30}]," +
                "\"size\":11,\"output amount\":[120],\"eos\":1.5,\"name\":\"Steel Mill\",\"expense\":68662," +
                "\"output goods\":[{\"upper cap\":1.75,\"lower cap\":0.25,\"shortage\":false,\"name\":\"Steel\"," +
                "\"type\":\"INDUSTRIAL\",\"supply\":100,\"demand\":200,\"base\":50}],\"input amount\":[90,30]}",
        steelMill.toJson().toString());
    }
}
