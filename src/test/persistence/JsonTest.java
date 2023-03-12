package persistence;

import model.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {
    protected void checkEconomy(Economy expected, Economy actual) {
        assertTrue(expected.equals(actual));
    }
}
