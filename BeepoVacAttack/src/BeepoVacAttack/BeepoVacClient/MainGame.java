package BeepoVacAttack.BeepoVacClient;
import BeepoVacAttack.Networking.*;
import jig.Entity;

import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainGame extends StateBasedGame {
    private static MainGame instance;

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;

    public static final String VAC_TEST_1 = "BeepoVacAttack/resources/Vac1.png";

    // networking
    public static ConcurrentLinkedQueue<Object> queue;
    Caller caller;
    Listener listener;

    // gameplay
    public int whichPlayer = 0;
    public LinkedList<ClientBeepoVac> players;

    // store screen width and height
    public final int ScreenWidth;
    public final int ScreenHeight;

    public MainGame(String title, int width, int height) {
        super(title);
        ScreenWidth = width;
        ScreenHeight = height;

        queue = new ConcurrentLinkedQueue<Object>();
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        players = new LinkedList<ClientBeepoVac>();

        instance = this;
    }

    public static int getWidth() { return instance.ScreenWidth; }
    public static int getHeight() { return instance.ScreenHeight; }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());
        ResourceManager.loadImage(VAC_TEST_1);
    }

    public static void main(String[] args) {

        AppGameContainer app;
        try {
            Thread.sleep(1000);
            System.out.println("ok sleep is done");
            app = new AppGameContainer(new MainGame("Client", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


