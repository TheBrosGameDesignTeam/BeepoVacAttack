package BeepoVacClient;

import jig.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;

    public static ConcurrentLinkedQueue<Object> queue;
    Caller caller;
    Listener listener;

    public MainGame(String title, int width, int height) {
        super(title);
        queue = new ConcurrentLinkedQueue<Object>();
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new MainGame("Client", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

}


