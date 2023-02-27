package test;

import model.Building;
import model.Goods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
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
    public void setup() {
        fertilizer = new Goods("Fertilizer", 30);
        grain = new Goods("Grain", 20);
        farm = new Building("Farm", 50, Arrays.asList(fertilizer), Arrays.asList(15),
                Arrays.asList(grain), Arrays.asList(90));

        iron = new Goods("Iron", 40);
        coal = new Goods("Coal", 30);
        steel = new Goods("Steel", 50);
        steelMill = new Building("Steel Mill", 450, Arrays.asList(iron,coal), Arrays.asList(90, 30),
                Arrays.asList(steel), Arrays.asList(120));

        sulphur = new Goods("Sulphur", 50);
        explosives = new Goods("Explosives", 50);
        chemicalPlant = new Building("Chemical Plant", 450,
                Arrays.asList(sulphur, iron, coal), Arrays.asList(60, 30, 15),
                Arrays.asList(fertilizer, explosives), Arrays.asList(110, 70));

        wagePerLevel = farm.WAGES_PER_LEVEL;
    }

    @Test
    public void testConstructor() {
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
    public void testsetSize() {
        farm.setSize(10);
        assertEquals(10, farm.getSize());
    }

    @Test
    public void testconsumeOnce() {
        farm.consume();
        assertEquals(15, fertilizer.getDemand());

        steelMill.consume();
        assertEquals(90, iron.getDemand());
        assertEquals(30, coal.getDemand());
    }

    @Test
    public void testconsumeTwice() {
        steelMill.consume();
        steelMill.consume();
        assertEquals(2 * 90, iron.getDemand());
        assertEquals(2 * 30, coal.getDemand());
    }



    @Test
    public void testconsumeTwoBuildingsSameResource() {
        steelMill.consume();
        chemicalPlant.consume();
        assertEquals(120, iron.getDemand());
    }

    @Test
    public void testconsumeLevel51() {
        for (int i = 0; i < 50; i++) {
            farm.expand();
        }

        assertEquals(51, farm.getSize());
        int expected = (int) ((1 + (51 - 1) * 0.01) * 51 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    public void testconsumeMaxEoS() {
        for (int i = 0; i < 60; i++) {
            farm.expand();
        }

        assertEquals(61, farm.getSize());
        int expected = (int) (1.5 * 61 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    public void testconsumeNormalLevel() {
        for (int i = 0; i < 30; i++) {
            farm.expand();
        }

        assertEquals(31, farm.getSize());
        int expected = (int) (1.3 * 31 * 15);
        farm.consume();
        assertEquals(expected, fertilizer.getDemand());
    }

    @Test
    public void testproduceOnce() {
        farm.produce();
        assertEquals(90, grain.getSupply());
    }

    @Test
    public void testproduceTwice() {
        farm.produce();
        farm.produce();
        assertEquals(2 * 90, grain.getSupply());
    }

    @Test
    public void testproduceLevel51() {
        for (int i = 0; i < 50; i++) {
            farm.expand();
        }

        assertEquals(51, farm.getSize());
        int expected = (int) ((1 + (51 - 1) * 0.01) * 51 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    public void testproduceMaxEoS() {
        for (int i = 0; i < 60; i++) {
            farm.expand();
        }

        assertEquals(61, farm.getSize());
        int expected = (int) (1.5 * 61 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    public void testproduceNormalLevel() {
        for (int i = 0; i < 30; i++) {
            farm.expand();
        }

        assertEquals(31, farm.getSize());
        int expected = (int) (1.3 * 31 * 90);
        farm.produce();
        assertEquals(expected, grain.getSupply());
    }

    @Test
    public void testisProfitable() {
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
    public void testexpandOnce() {
        assertEquals(1, steelMill.getSize());
        steelMill.expand();
        assertEquals(2, steelMill.getSize());
    }

    @Test
    public void testexpandTwice() {
        steelMill.expand();
        steelMill.expand();
        assertEquals(3, steelMill.getSize());
    }

    @Test
    public void testdownsizeNormal() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(5));
        assertEquals(5, steelMill.getSize());
    }

    @Test
    public void testdownsizeOneLevel() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(1));
        assertEquals(9, steelMill.getSize());
    }

    @Test
    public void testdownsizeToZero() {
        for (int i = 0; i < 9; i++) {
            steelMill.expand();
        }
        assertTrue(steelMill.downsize(10));
        assertEquals(0, steelMill.getSize());
    }

    @Test
    public void testdetermineEoSLevelOne() {
        assertEquals(1.0, chemicalPlant.determineEoS());
    }

    @Test
    public void testdetermineEoSNormal() {
        for (int i = 0; i < 10; i++) {
            chemicalPlant.expand();
        }
        assertEquals(1.1, chemicalPlant.determineEoS());
    }

    @Test
    public void testdetermineEoSMaxLevel() {
        for (int i = 0; i < 51; i++) {
            chemicalPlant.expand();
        }
        assertEquals(1.5, chemicalPlant.determineEoS());
    }

    @Test
    public void testpayExpenseOnce() {
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
    }

    @Test
    public void testpayExpenseTwice() {
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
        farm.payExpense();
        assertEquals(15 * 30 + wagePerLevel, farm.getExpense());
    }

    @Test
    public void testpayExpenseSize11() {
        for (int i = 0; i < 10; i++) {
            farm.expand();
        }

        double economyOfScale = farm.determineEoS();
        int expected = (int) (11 * 15 * 30 * economyOfScale + wagePerLevel * 11);
        farm.payExpense();
        assertEquals(expected, farm.getExpense());
    }

    @Test
    public void testgainIncome() {
        farm.gainIncome();
        assertEquals(90 * 20, farm.getIncome());
        farm.gainIncome();
        assertEquals(90 * 20, farm.getIncome());
    }

    @Test
    public void testpayWagesSize1() {
        chemicalPlant.payWages();
        assertEquals(wagePerLevel, chemicalPlant.getExpense());
    }

    @Test
    public void testpayWagesSize11() {
        for (int i = 0; i < 10; i++) {
            chemicalPlant.expand();
        }

        chemicalPlant.payWages();
        assertEquals(wagePerLevel * 11, chemicalPlant.getExpense());
    }

    @Test
    public void testbuyGoodsSize1() {
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
    public void testbuyGoodsSize11() {
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
}
