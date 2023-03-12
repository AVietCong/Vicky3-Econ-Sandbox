package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

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

    Market market;
    Industries industry;
    ConstructionSector constructionSector;

    @BeforeEach
    void setup() {
        fertilizer = new Goods("Fertilizer", 30, Goods.GoodsType.INDUSTRIAL);
        grain = new Goods("Grain", 20, Goods.GoodsType.CONSUMER);
        groceries = new Goods("Groceries", 30, Goods.GoodsType.CONSUMER);
        steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
        iron = new Goods("Iron", 40, Goods.GoodsType.INDUSTRIAL);
        coal = new Goods("Coal", 30, Goods.GoodsType.INDUSTRIAL);
        explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
        tools = new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL);
        farm = new Building("Farm", 50, Arrays.asList(fertilizer), Arrays.asList(15),
                Arrays.asList(grain), Arrays.asList(90));
        ironMine = new Building("Iron Mine", 300,
                Arrays.asList(tools, coal, explosives), Arrays.asList(15, 15, 10),
                Arrays.asList(iron), Arrays.asList(80));
        foodIndustry = new Building("Food Industry", 450,
                Arrays.asList(grain, iron), Arrays.asList(20, 10),
                Arrays.asList(groceries), Arrays.asList(65));

        market = new Market();
        market.addGoods(fertilizer);
        market.addGoods(grain);
        market.addGoods(groceries);
        market.addGoods(steel);
        market.addGoods(iron);
        market.addGoods(coal);
        market.addGoods(explosives);
        market.addGoods(tools);

        industry = new Industries();
        industry.add(farm);
        industry.add(ironMine);
        industry.add(foodIndustry);
        constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                Arrays.asList(70, 20, 20));
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Economy economy = new Economy(industry, market, constructionSector);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    // don't need to test economy with empty market and industry as those are not empty when they are passed to
    // economy before the user get a chance to save
    @Test
    void testWriterGeneralEconomy() {
        try {
            Economy economy = new Economy(industry, market, constructionSector);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEconomy.json");
            writer.open();
            writer.write(economy);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralEconomy.json");
            Economy readEconomy = reader.read();
            checkEconomy(economy, readEconomy);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
