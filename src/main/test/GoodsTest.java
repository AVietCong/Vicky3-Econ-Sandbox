package test;

import model.Goods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GoodsTest {

    Goods iron;
    Goods wood;
    Goods tools;
    Goods furniture;
    double upperPriceCap;
    double lowerPriceCap;


    @BeforeEach
    public void setup() {
        iron = new Goods("Iron", 40);
        wood = new Goods("Wood", 20);
        tools = new Goods("Tools", 40);
        furniture = new Goods("Furniture", 30);
        upperPriceCap = Goods.UPPER_PRICE_CAP;
        lowerPriceCap = Goods.LOWER_PRICE_CAP;
    }

    @Test
    public void testConstructor() {
        assertEquals("Iron", iron.getName());
        assertEquals(40, iron.getBasePrice());
        assertEquals(0, iron.getDemand());
        assertEquals(0, iron.getSupply());
        assertFalse(iron.isShortage());
    }

    @Test
    public void testsetSupply() {
        wood.setSupply(2000);
        assertEquals(2000, wood.getSupply());

        wood.setSupply(1800);
        assertEquals(1800, wood.getSupply());
    }

    @Test
    public void testsetDemand() {
        tools.setDemand(1700);
        assertEquals(1700, tools.getDemand());

        tools.setDemand(1800);
        assertEquals(1800, tools.getDemand());
    }

    @Test
    public void testsetShortage() {
        iron.setShortage(true);
        assertTrue(iron.isShortage());

        iron.setShortage(false);
        assertFalse(iron.isShortage());
    }

    @Test
    public void testdeterminePriceNoSupplyDemand() {
        assertEquals(0, furniture.getDemand());
        assertEquals(0, furniture.getSupply());

        assertEquals(30, furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceNoSupply() {
        furniture.setDemand(50);
        assertEquals(0, furniture.getSupply());

        assertEquals((int) (upperPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceNoDemand() {
        furniture.setSupply(70);
        assertEquals(0, furniture.getDemand());

        assertEquals((int) (lowerPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceDemandGreaterNoShortage() {
        furniture.setSupply(100);
        furniture.setDemand(174);
        int actualFurniturePrice = (int) (furniture.getBasePrice() * (1.0 + (0.75 * (174 - 100) / 100)));
        assertEquals(actualFurniturePrice, furniture.determinePrice());
        assertFalse(furniture.isShortage());

        iron.setSupply(200);
        iron.setDemand(300);
        int actualIronPrice = (int) (iron.getBasePrice() * (1.0 + (0.75 * (300 - 200) / 200)));
        assertEquals(actualIronPrice, iron.determinePrice());
        assertFalse(iron.isShortage());
    }


    @Test
    public void testdeterminePriceDemandGreaterYesShortage() {
        furniture.setSupply(100);
        furniture.setDemand(350);
        int expectedFurniturePrice = (int) (furniture.getBasePrice() * upperPriceCap);
        int actualFurniturePrice = furniture.determinePrice();
        assertEquals(expectedFurniturePrice, actualFurniturePrice);

        tools.setSupply(200);
        tools.setDemand(400);
        int expectedToolsPrice = (int) (tools.getBasePrice() * upperPriceCap);
        int actualToolsPrice = tools.determinePrice();
        assertEquals(expectedToolsPrice, actualToolsPrice);
    }

    @Test
    public void testdeterminePriceSupplyGreater() {
        furniture.setSupply(500);
        furniture.setDemand(300);
        int expectedFurniturePrice = (int) (furniture.getBasePrice() * (1.0 + (0.75 * (300 - 500) / 300)));
        int actualFurniturePrice = furniture.determinePrice();
        assertEquals(expectedFurniturePrice, actualFurniturePrice);

        iron.setSupply(800);
        iron.setDemand(150);
        assertEquals((int) (lowerPriceCap * iron.getBasePrice()), iron.determinePrice());

        wood.setSupply(800);
        wood.setDemand(201);
        int expectedWoodPrice = (int) (wood.getBasePrice() * lowerPriceCap);
        assertEquals(expectedWoodPrice, wood.determinePrice());
    }

    @Test
    public void testdeterminePriceDemandNoSupply() {
        furniture.setDemand(60);
        assertEquals(0, furniture.getSupply());
        assertEquals(60, furniture.getDemand());

        assertEquals((int) (upperPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceSupplyNoDemand() {
        furniture.setSupply(30);
        assertEquals(0, furniture.getDemand());
        assertEquals(30, furniture.getSupply());

        assertEquals((int) (lowerPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }
}
