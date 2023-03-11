package persistence;

import model.ConstructionSector;
import model.Goods;
import model.Industries;
import model.Market;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{

    Goods steel = new Goods("Steel", 50, Goods.GoodsType.INDUSTRIAL);
    Goods explosives = new Goods("Explosives", 50, Goods.GoodsType.INDUSTRIAL);
    Goods tools = new Goods("Tools", 40, Goods.GoodsType.INDUSTRIAL);

    @Test
    void testWriterInvalidFile() {
        try {
            Market market = new Market();
            Industries industries = new Industries();
            ConstructionSector constructionSector = new ConstructionSector(Arrays.asList(steel, tools, explosives),
                    Arrays.asList(70, 20, 20));
            JsonWriter writer = new JsonWriter("./data/mySandbox.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }
}
