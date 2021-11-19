package BeepoVacClient;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class StartUpState extends BasicGameState {

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        MainGame bg = (MainGame)game;

        Socket socket = null;
        try {
            socket = new Socket("localhost", 4999);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        PrintWriter printWriter = null;

        //testing 2 way communication
        BufferedReader bufferedReader = null;

        try {
            printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        bg.caller = new Caller(printWriter);
        bg.listener = new Listener(bufferedReader);

        bg.listener.start();

    }


    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;

        g.drawString("Connection to Server Made", 100, 100);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        MainGame bg = (MainGame)game;

        if (input.isKeyDown(Input.KEY_W)){
            bg.caller.push("w");
        }
        if (input.isKeyDown(Input.KEY_S)){
            bg.caller.push("s");
        }
        if (input.isKeyDown(Input.KEY_D)){
            bg.caller.push("d");
        }
        if (input.isKeyDown(Input.KEY_A)){
            bg.caller.push("a");
        }

        while (!MainGame.queue.isEmpty()) {

            System.out.println("We are here");

            Object message = MainGame.queue.poll();

            if (message instanceof Packet) {
                System.out.println("Returned from server " + ((Packet) message).message);
            }
        }
    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}
