package persistence;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
// TODO: Implement check classes in model
public class JsonTest {
    protected void checkMarket(List<Goods> goods, Market market) {
        assertEquals(goods, market.getAllGoods());
    }

    protected void checkIndustries(List<String> buildings, List<Integer> size, List<Integer> income,
                                   List<Integer> expense, List<List<Integer>> inputAmount, List<List<Integer>> outputAmount,
                                   List<List<Goods>> inputGoods, List<List<Goods>> outputGoods, Industries industries) {
        assertEquals(buildings, industries.getAllBuildingNames());
        assertEquals(size, industries.getAllSize());
        assertEquals(income, industries.getAllIncome());
        assertEquals(expense, industries.getAllExpenses());
        assertEquals(inputGoods, industries.getAllInputGoods());
        assertEquals(inputAmount, industries.getAllInputGoodsAmount());
        assertEquals(outputGoods, industries.getAllOutputGoods());
        assertEquals(outputAmount, industries.getAllOutputGoodsAmount());
    }

    protected void checkConstruction(List<String> queue, List<Integer> value, List<Goods> inputGoods, List<Integer> inputAmount,
                                     ConstructionSector construction) {
        List<String> constructionQueue = new ArrayList<>();
        for (Building b : construction.getConstructionQueue()) {
            constructionQueue.add(b.getName());
        }
        assertEquals(queue, constructionQueue);
    }
}
