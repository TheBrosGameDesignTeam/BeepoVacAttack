package BeepoVacClient;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
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
        try {
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        bg.caller = new Caller(printWriter);

        bg.caller.start();
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

        if (input.isKeyPressed(Input.KEY_1)){
            bg.caller.move(1);
        }
        if (input.isKeyPressed(Input.KEY_2)){
            bg.caller.move(2);
        }

    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}