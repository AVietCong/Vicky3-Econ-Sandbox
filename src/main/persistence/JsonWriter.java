package persistence;

import model.*;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// This class is a writer that writes JSON representation of the economy to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of the state of sandbox to file
    public void write(Economy economy) {
        JSONObject jsonSandbox = economy.toJson();
        saveToFile(jsonSandbox.toString(TAB));
    }

    public void close() {
        writer.close();
    }

    private void saveToFile(String json) {
        writer.print(json);
    }
}
