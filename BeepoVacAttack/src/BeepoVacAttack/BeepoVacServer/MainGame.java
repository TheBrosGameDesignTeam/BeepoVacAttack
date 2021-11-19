package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.Networking.Listener;

import jig.Entity;

import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;

    public static final String VAC_TEST_1 = "BeepoVacAttack/resources/Vac1.png";

    // movement testing
    BeepoVac[] players = {null, null};

    public static ConcurrentLinkedQueue<Object> queue;
    public static LinkedList<Listener> listeners;

    // observer of the main game
    public static Observer observer;

    public MainGame(String title, int width, int height) {
        super(title);
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

        queue = new ConcurrentLinkedQueue<Object>();
        listeners = new LinkedList<Listener>();

    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {

        addState(new StartUpState());
        ResourceManager.loadImage(VAC_TEST_1);

    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
            app = new AppGameContainer(new MainGame("Server", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }


}

