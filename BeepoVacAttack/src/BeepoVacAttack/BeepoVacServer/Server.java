package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.Networking.Caller;
import BeepoVacAttack.Networking.Listener;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server extends Thread {

    private final int maxConnections;
    public static ConcurrentLinkedQueue<Object> queue;

    public Server(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void run() {

        ServerSocket serverSocket;
        int connections = 0;

        try {
            serverSocket = new ServerSocket(4999);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Socket socket;

        while (connections < maxConnections) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            InputStreamReader inputStreamReader;

            try {
                inputStreamReader = new InputStreamReader(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(inputStreamReader);

            // testing 2 way communication
            PrintWriter printWriter;
            try {
                printWriter = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                continue;
            }

            Listener listener = new Listener(bufferedReader, MainGame.queue);
            Caller caller = new Caller(printWriter);

            MainGame.observer.add(caller);

            listener.start();

        }

        // once we are done accepting connections, set number of current connections?

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
