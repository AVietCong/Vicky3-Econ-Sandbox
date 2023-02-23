package test;

import model.Goods;
import model.Market;
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
        iron = new Goods("Iron", 40);
        wood = new Goods("Wood", 20);
        tools = new Goods("Tools",30);
        furniture = new Goods("Furniture", 30);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, market.size());
        assertEquals(0, market.getGDP());
    }

    @Test
    public void testaddGoodsOnce() {
        market.addGoods(iron);
        assertEquals(1, market.size());
        assertTrue(market.getAvailableGoods().contains("Iron"));
    }

    @Test
    public void testaddGoodsTwice() {
        market.addGoods(iron);
        market.addGoods(wood);
        assertEquals(2, market.size());
        assertTrue(market.getAvailableGoods().contains("Wood"));
    }

    @Test
    public void testremoveGoodsInMarket() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        assertTrue(market.removeGoods(wood));
        assertFalse(market.getAvailableGoods().contains("Wood"));
        assertEquals(2, market.size());
    }

    @Test
    public void testremoveGoodsNotInMarket() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);

        assertFalse(market.removeGoods(furniture));
        assertEquals(3, market.size());
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
        tools.setSupply(250);
        wood.setSupply(400);
        iron.setSupply(100);


        List expected = Arrays.asList(100, 400, 250);
        List actual = market.getSupplyOfGoods();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetDemandOfGoods() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.setDemand(125);
        tools.setDemand(500);
        wood.setDemand(300);


        List expected = Arrays.asList(125, 300, 500);
        List actual = market.getDemandOfGoods();
        assertEquals(expected, actual);
    }

    @Test
    public void testgetPriceModifiers() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.setDemand(125);
        iron.setSupply(100);
        tools.setDemand(500);
        tools.setSupply(250);
        wood.setDemand(50);
        wood.setSupply(400);

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
        iron.setDemand(125);
        iron.setSupply(100);
        tools.setDemand(500);
        tools.setSupply(250);
        wood.setDemand(50);
        wood.setSupply(400);

        List expected = Arrays.asList(iron.determinePrice(),
                wood.determinePrice(),
                tools.determinePrice());

        List actual = market.getPrices();
        assertEquals(expected, actual);
    }

    @Test
    public void testdetermineGDP() {
        market.addGoods(iron);
        market.addGoods(wood);
        market.addGoods(tools);
        iron.setDemand(125);
        iron.setSupply(100);
        tools.setDemand(500);
        tools.setSupply(250);
        wood.setDemand(50);
        wood.setSupply(400);

        int expected = iron.getDemand() * iron.determinePrice() +
                wood.getDemand() * wood.determinePrice() +
                tools.getDemand() * tools.determinePrice();
        int actual = market.determineGDP();
        assertEquals(expected, actual);
    }
}