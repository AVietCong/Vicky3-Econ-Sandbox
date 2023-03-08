package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MarketTest {

    Market market;
    Goods iron;
    Goods wood;
    Goods tools;
    Goods furniture;

    @BeforeEach
    public void setup() {
        market = new Market();
        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        wood = new Goods("Wood", 20, Goods.GoodsType.INDUSTRIAL);
        tools = new Goods("Tools",30, Goods.GoodsType.INDUSTRIAL);
        furniture = new Goods("Furniture", 30, Goods.GoodsType.CONSUMER);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, market.numberOfGoods());
    }

    @Test
    public void testaddGoodsOnce() {
        market.addGoods(iron);
        assertEquals(1, market.numberOfGoods());
        assertTrue(market.getAvailableGoods().contains("Iron"));
    }

    @Test
    public void testgetAllGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        market.addGoods(furniture);

        assertEquals(Arrays.asList(iron, wood, tools, furniture), market.getAllGoods());
    }

    @Test
    public void testaddGoodsTwice() {
        market.addGoods(iron);
        market.addGoods(wood);
        assertEquals(2, market.numberOfGoods());
        assertTrue(market.getAvailableGoods().contains("Wood"));
    }

    @Test
    public void testremoveGoodsInMarket() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        assertTrue(market.removeGoods(wood));
        assertFalse(market.getAvailableGoods().contains("Wood"));
        assertEquals(2, market.numberOfGoods());
    }

    @Test
    public void testremoveGoodsNotInMarket() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        assertFalse(market.removeGoods(furniture));
        assertEquals(3, market.numberOfGoods());
    }

    @Test
    public void testgetAvailableGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        List expected = Arrays.asList("Iron", "Wood", "Tools");
        List actual = market.getAvailableGoods();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetSupplyOfGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        tools.addSupply(250);
        wood.addSupply(400);
        iron.addSupply(100);


        List expected = Arrays.asList(100, 400, 250);
        List actual = market.getSupplyOfGoods();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetDemandOfGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.addDemand(125);
        tools.addDemand(500);
        wood.addDemand(300);


        List expected = Arrays.asList(125, 300, 500);
        List actual = market.getDemandOfGoods();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetPriceModifiers() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.addDemand(125);
        iron.addSupply(100);
        tools.addDemand(500);
        tools.addSupply(250);
        wood.addDemand(50);
        wood.addSupply(400);

        List expected = Arrays.asList(iron.determinePriceModifier(),
                wood.determinePriceModifier(),
                tools.determinePriceModifier());
        List actual = market.getPriceModifiers();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetMarketPrices() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.addDemand(125);
        iron.addSupply(100);
        tools.addDemand(500);
        tools.addSupply(250);
        wood.addDemand(50);
        wood.addSupply(400);

        List expected = Arrays.asList(iron.determinePrice(),
                wood.determinePrice(),
                tools.determinePrice());

        List actual = market.getPrices();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetBasePrices() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.addDemand(125);
        iron.addSupply(100);
        tools.addDemand(500);
        tools.addSupply(250);
        wood.addDemand(50);
        wood.addSupply(400);

        List expected = Arrays.asList(40, 20, 30);

        List actual = market.getBasePrices();
        assertEquals(expected, actual);
    }

    @Test
    public void testdetermineGDP() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.addDemand(125);
        iron.addSupply(100);
        tools.addDemand(500);
        tools.addSupply(250);
        wood.addDemand(50);
        wood.addSupply(400);

        int expected = iron.getDemand() * iron.determinePrice()
                + wood.getDemand() * wood.determinePrice()
                + tools.getDemand() * tools.determinePrice();
        int actual = market.determineGDP();
        assertEquals(expected, actual);
    }

    @Test
    public void testremoveInactiveGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        market.addGoods(furniture);
        iron.addSupply(100);
        tools.addDemand(500);
        furniture.addSupply(200);
        furniture.addDemand(250);

        Market resultMarket = market.removeInactiveGoods();
        assertEquals(Arrays.asList("Iron", "Tools", "Furniture"), resultMarket.getAvailableGoods());
    }

    @Test
    void testreturnConsumerGoods() {
        Goods services = new Goods("Services", 30, Goods.GoodsType.CONSUMER);
        market.addGoods(iron);
        market.addGoods(services);
        market.addGoods(wood);
        market.addGoods(tools);
        market.addGoods(furniture);

        assertEquals(Arrays.asList(services, furniture), market.returnConsumerGoods());
    }

    @Test
    void testreturnConsumerGoodsEmpty() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        assertTrue( market.returnConsumerGoods().isEmpty());
    }

    @Test
    void testreturnIndustrialGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        market.addGoods(furniture);

        assertEquals(Arrays.asList(iron, wood, tools), market.returnIndustrialGoods());
    }

    @Test
    void testreturnIndustrialGoodsEmpty() {
        market.addGoods(furniture);
        Goods services = new Goods("Services", 30, Goods.GoodsType.CONSUMER);
        market.addGoods(services);

        assertTrue( market.returnIndustrialGoods().isEmpty());
    }
}
