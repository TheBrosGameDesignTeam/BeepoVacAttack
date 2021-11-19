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

        // press space if you are ready to start the game!
        if (input.isKeyPressed(Input.KEY_SPACE)){
            bg.caller.push("space");
        }

        while (!MainGame.queue.isEmpty()) {

            Object message = MainGame.queue.poll();

            if (message instanceof Packet) {
                System.out.println("Returned from server " + ((Packet) message).message);
                // here we would check for confirmation return from the server to switch to the first level
            }
        }
    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}
