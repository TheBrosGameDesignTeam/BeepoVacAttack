package BeepoVacClient;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Caller {

    private final PrintWriter printWriter;

    public Caller(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public void push(String move) {

        printWriter.println(move);
        printWriter.flush();

    }
}
