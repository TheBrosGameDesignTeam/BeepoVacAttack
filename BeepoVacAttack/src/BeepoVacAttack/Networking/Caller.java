package BeepoVacAttack.Networking;

import BeepoVacAttack.Networking.Packet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class Caller {

    private final ObjectOutputStream outputStream;

    public Caller(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void push(Packet message){

        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
