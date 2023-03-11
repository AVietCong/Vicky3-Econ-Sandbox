package persistence;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
// TODO: Implement check classes in model
public class JsonTest {
    protected void checkMarket(Market expected, Market actual) {
        assertEquals(expected, actual);
    }

    protected void checkIndustries(Industries expected, Industries actual) {
        assertEquals(expected, actual);
    }

    protected void checkConstruction(ConstructionSector expected, ConstructionSector actual) {
        assertEquals(expected, actual);
    }

}
