package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistenFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Economy economy = reader.read();
            fail("IOExceeption expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralEconomy() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEconomy.json");
        try {
            Economy economy = reader.read();
            checkEconomy(createEconomy(), economy);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    Economy createEconomy() {
        Market market = new Market();
        market.addGoods(new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL));
        market.addGoods(new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL));
        market.addGoods(new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL));
        market.addGoods(new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL));
        market.addGoods(new Goods("Grain", 20, Goods.GoodsType.CONSUMER));
        market.addGoods(new Goods("Groceries", 30, Goods.GoodsType.CONSUMER));
        market.addGoods(new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL));
        market.addGoods(new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL));

        Industries industries = new Industries();
        industries.add(new Building("Farm", 50,
                Arrays.asList(new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL)), Arrays.asList(15),
                Arrays.asList(new Goods("Grain", 20, Goods.GoodsType.CONSUMER)), Arrays.asList(90)));
        industries.add(new Building("Iron Mine", 300,
                Arrays.asList(new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL),
                        new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL),
                        new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL)),
                Arrays.asList(15, 15, 10),
                Arrays.asList(new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL)), Arrays.asList(80)));
        industries.add(new Building("Food Industry", 450,
                Arrays.asList(new Goods("Grain", 20, Goods.GoodsType.CONSUMER),
                        new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL)),
                Arrays.asList(20, 10),
                Arrays.asList(new Goods("Groceries", 30, Goods.GoodsType.CONSUMER)), Arrays.asList(65)));
        ConstructionSector constructionSector = new ConstructionSector(
                Arrays.asList(new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL),
                        new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL),
                        new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL)),
                Arrays.asList(70, 20, 20));
        constructionSector.build(new Building("Farm", 50,
                Arrays.asList(new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL)), Arrays.asList(15),
                Arrays.asList(new Goods("Grain", 20, Goods.GoodsType.CONSUMER)), Arrays.asList(90)));
        return new Economy(industries, market, constructionSector);
    }
}
