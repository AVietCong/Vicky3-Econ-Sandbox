package persistence;

import model.Goods;
import model.Industries;
import model.Market;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// This class is a writer that writes JSON representation of the economy to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter printWriter;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of market to file
    public void write(Market market, Industries industries) {
        JSONObject json = market.toJson();
        saveToFile(json.toString(TAB));
    }

    public void close() {
        printWriter.close();
    }

    private void saveToFile(String json) {
        printWriter.print(json);
    }
}
