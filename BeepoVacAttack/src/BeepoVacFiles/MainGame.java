package BeepoVacClient;

import jig.Entity;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainGame extends StateBasedGame {

    public static final int STARTUPSTATE = 0;

    public MainGame(String title, int width, int height) {
        super(title);
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
    }

    public static void main(String[] args) {
        AppGameContainer app;
        try {
//            app = new AppGameContainer(new MainGame("BeepoVacAttack!", 800, 600));
            app = new AppGameContainer(new MainGame("Client", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }


}


