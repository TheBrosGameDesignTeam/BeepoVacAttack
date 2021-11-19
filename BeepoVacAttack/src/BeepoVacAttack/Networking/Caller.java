package BeepoVacAttack.Networking;

import BeepoVacAttack.Networking.Packet;
import java.io.PrintWriter;

public class Caller {

    private final PrintWriter printWriter;

    public Caller(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public void push(String message) {

        printWriter.println(message);
        printWriter.flush();

    }
}
