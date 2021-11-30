package BeepoVacAttack.BeepoVacClient;
//import BeepoVacAttack.BeepoVacServer.PlayingState;
import BeepoVacAttack.GamePlay.BeepoVac;
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

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;

    public static final String VAC_TEST_1 = "BeepoVacAttack/resources/Vac1.png";

    public static ConcurrentLinkedQueue<Object> queue;
    Caller caller;
    Listener listener;

    // gameplay
    public int whichPlayer = 0;
    public LinkedList<ClientBeepoVac> players;

    public MainGame(String title, int width, int height) {
        super(title);

        queue = new ConcurrentLinkedQueue<Object>();
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        players = new LinkedList<ClientBeepoVac>();

    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());
        ResourceManager.loadImage(VAC_TEST_1);
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


