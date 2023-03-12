package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.Goods.GoodsType.CONSUMER;
import static model.Goods.GoodsType.INDUSTRIAL;
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
        iron = new Goods("Iron", 40, INDUSTRIAL);
        wood = new Goods("Wood", 20, INDUSTRIAL);
        tools = new Goods("Tools", 40, INDUSTRIAL);
        furniture = new Goods("Furniture", 30, CONSUMER);
        upperPriceCap = Goods.UPPER_PRICE_CAP;
        lowerPriceCap = Goods.LOWER_PRICE_CAP;
    }

    @Test
    public void testConstructor() {
        assertEquals("Iron", iron.getName());
        assertEquals(40, iron.getBasePrice());
        assertEquals(0, iron.getDemand());
        assertEquals(0, iron.getSupply());
        //assertFalse(iron.isShortage());
        assertEquals(INDUSTRIAL, iron.getGoodsType());
        assertEquals(CONSUMER, furniture.getGoodsType());
    }

    @Test
    public void testsetDemand() {
        iron.setDemand(100);
        assertEquals(100, iron.getDemand());
        iron.setDemand(80);
        assertEquals(80, iron.getDemand());
    }

    @Test
    public void testsetSupply() {
        iron.setSupply(200);
        assertEquals(200, iron.getSupply());
        iron.setSupply(90);
        assertEquals(90, iron.getSupply());
    }

    @Test
    public void testaddSupply() {
        wood.addSupply(2000);
        assertEquals(2000, wood.getSupply());

        wood.addSupply(1800);
        assertEquals(2000 + 1800, wood.getSupply());
    }

    @Test
    public void testaddDemand() {
        tools.addDemand(1700);
        assertEquals(1700, tools.getDemand());

        tools.addDemand(1800);
        assertEquals(1700 + 1800, tools.getDemand());
    }

    @Test
    public void testsetShortage() {
        //iron.setShortage(true);
        //assertTrue(iron.isShortage());

        //iron.setShortage(false);
        //assertFalse(iron.isShortage());
    }

    @Test
    public void testdeterminePriceNoSupplyDemand() {
        assertEquals(0, furniture.getDemand());
        assertEquals(0, furniture.getSupply());

        assertEquals(30, furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceNoSupply() {
        furniture.addDemand(50);
        assertEquals(0, furniture.getSupply());

        assertEquals((int) (upperPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceNoDemand() {
        furniture.addSupply(70);
        assertEquals(0, furniture.getDemand());

        assertEquals((int) (lowerPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceDemandGreaterNoShortage() {
        furniture.addSupply(100);
        furniture.addDemand(174);
        int actualFurniturePrice = (int) (furniture.getBasePrice() * (1.0 + (0.75 * (174 - 100) / 100)));
        assertEquals(actualFurniturePrice, furniture.determinePrice());
        //assertFalse(furniture.isShortage());

        iron.addSupply(200);
        iron.addDemand(300);
        int actualIronPrice = (int) (iron.getBasePrice() * (1.0 + (0.75 * (300 - 200) / 200)));
        assertEquals(actualIronPrice, iron.determinePrice());
        //assertFalse(iron.isShortage());
    }


    @Test
    public void testdeterminePriceDemandGreaterYesShortage() {
        furniture.addSupply(100);
        furniture.addDemand(350);
        int expectedFurniturePrice = (int) (furniture.getBasePrice() * upperPriceCap);
        int actualFurniturePrice = furniture.determinePrice();
        assertEquals(expectedFurniturePrice, actualFurniturePrice);

        tools.addSupply(200);
        tools.addDemand(400);
        int expectedToolsPrice = (int) (tools.getBasePrice() * upperPriceCap);
        int actualToolsPrice = tools.determinePrice();
        assertEquals(expectedToolsPrice, actualToolsPrice);
    }

    @Test
    public void testdeterminePriceSupplyGreater() {
        furniture.addSupply(500);
        furniture.addDemand(300);
        int expectedFurniturePrice = (int) (furniture.getBasePrice() * (1.0 + (0.75 * (300 - 500) / 300)));
        int actualFurniturePrice = furniture.determinePrice();
        assertEquals(expectedFurniturePrice, actualFurniturePrice);

        iron.addSupply(800);
        iron.addDemand(150);
        assertEquals((int) (lowerPriceCap * iron.getBasePrice()), iron.determinePrice());

        wood.addSupply(800);
        wood.addDemand(201);
        int expectedWoodPrice = (int) (wood.getBasePrice() * lowerPriceCap);
        assertEquals(expectedWoodPrice, wood.determinePrice());
    }

    @Test
    public void testdeterminePriceDemandNoSupply() {
        furniture.addDemand(60);
        assertEquals(0, furniture.getSupply());
        assertEquals(60, furniture.getDemand());

        assertEquals((int) (upperPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    public void testdeterminePriceSupplyNoDemand() {
        furniture.addSupply(30);
        assertEquals(0, furniture.getDemand());
        assertEquals(30, furniture.getSupply());

        assertEquals((int) (lowerPriceCap * furniture.getBasePrice()), furniture.determinePrice());
    }

    @Test
    void testtoJson() {
        furniture.addSupply(500);
        furniture.addDemand(300);
        assertEquals("{\"name\":\"Furniture\",\"type\":\"CONSUMER\",\"supply\":500,\"demand\":300,\"base\":30}",
                furniture.toJson().toString());
    }

    @Test
    void testEquals() {
        iron.setDemand(200);
        iron.setSupply(300);
        assertTrue(iron.equals(iron));
        assertFalse(iron.equals(null));
        assertFalse(iron.equals("Iron"));

        Goods expectedEqual = new Goods("Iron", 40, INDUSTRIAL);
        expectedEqual.setDemand(200);
        expectedEqual.setSupply(300);
        assertTrue(iron.equals(expectedEqual));

        Goods differentName = new Goods("Wood", 40, INDUSTRIAL);
        differentName.setDemand(200);
        differentName.setSupply(300);
        assertFalse(iron.equals(differentName));

        Goods differentBasePrice = new Goods("Iron", 30, INDUSTRIAL);
        differentBasePrice.setDemand(200);
        differentBasePrice.setSupply(300);
        assertFalse(iron.equals(differentBasePrice));

        Goods differentType = new Goods("Iron", 40, CONSUMER);
        differentBasePrice.setDemand(200);
        differentBasePrice.setSupply(300);
        assertFalse(iron.equals(differentType));

        Goods differentDemand = new Goods("Iron", 40, INDUSTRIAL);
        differentDemand.setDemand(250);
        differentDemand.setSupply(300);
        assertFalse(iron.equals(differentDemand));

        Goods differentSupply = new Goods("Iron", 40, INDUSTRIAL);
        differentSupply.setDemand(200);
        differentSupply.setSupply(400);
        assertFalse(iron.equals(differentSupply));
    }

    @Test
    void testHashCode() {
        Goods oneGood = new Goods("Iron", 40, INDUSTRIAL);
        oneGood.setDemand(100);
        oneGood.setSupply(150);

        iron.setDemand(100);
        iron.setSupply(150);
        assertEquals(iron.hashCode(), oneGood.hashCode());
    }
}
