package BeepoVacClient;

import java.io.PrintWriter;

public class Caller extends Thread {

    private final PrintWriter printWriter;

    public Caller(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public void run() {
        printWriter.println("Hello, Server!");
        printWriter.flush();

//        while (true) {
//            for (int i = 0; i < 100; i++) {
//                System.out.println(i);
//                printWriter.println(i);
//                printWriter.flush();
//                try {
//                    sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void move(int move) {

        System.out.println(move);
        printWriter.println(move);
        printWriter.flush();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
