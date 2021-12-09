package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.DustBunny;
import BeepoVacAttack.Networking.Listener;
import jig.Entity;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MainGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;

    // networking
    public static ConcurrentLinkedQueue<Object> queue;
    public static LinkedList<Listener> listeners;
    public static Observer observer;

    // gameplay
    public static LinkedList<BeepoVac> players;
    public static LinkedList<DustBunny> bunnies;

    public MainGame(String title, int width, int height) {
        super(title);
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

        queue = new ConcurrentLinkedQueue<Object>();
        listeners = new LinkedList<Listener>();
        players = new LinkedList<BeepoVac>();
        bunnies = new LinkedList<DustBunny>();

    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());
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

