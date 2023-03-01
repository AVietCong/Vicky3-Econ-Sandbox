package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// This class is a writer that writes JSON representation of the economy to a file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter printWriter;
    private String destination;

    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destination));
    }

    //public void write()
}
