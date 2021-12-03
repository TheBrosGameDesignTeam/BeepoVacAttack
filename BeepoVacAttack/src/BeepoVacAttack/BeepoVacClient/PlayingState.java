package BeepoVacAttack.BeepoVacClient;

//import BeepoVacAttack.BeepoVacServer.MainGame;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import BeepoVacAttack.Networking.Packet;

import BeepoVacAttack.GamePlay.Level;


public class PlayingState extends BasicGameState {

//    public float p1X = 0, p1Y = 0;

    private Level level = null;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        try {
            level = Level.fromXML("BeepoVacAttack/BeepoVacAttack/src/BeepoVacAttack/resources/level/ExampleLevel.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (level == null) {
            System.out.println("level couldn't be loaded :(");
            return;
        }

        level.initializeDustMap();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;
        g.drawString("We are playing!", 100, 100);

        level.renderBackground(g);

        bg.players.forEach(
            (player) -> player.render(g)
        );

        level.renderOverlay(g);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        MainGame bg = (MainGame)game;

        Packet pack = null;

        // temp - need to refactor
        if (input.isKeyDown(Input.KEY_A)){
            pack = new Packet("a");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
        }
        if (input.isKeyDown(Input.KEY_D)){
            pack = new Packet("d");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
        }
        if (input.isKeyDown(Input.KEY_S)){
            pack = new Packet("s");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
        }
        if (input.isKeyDown(Input.KEY_W)){
            pack = new Packet("w");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
        }

        // take in the game state and apply it.
        while (!MainGame.queue.isEmpty()) {
            Object message = MainGame.queue.poll();
            Packet test = (Packet) message;

            // make sure this is a snapshot
            if (test.getMessage().compareTo("snapshot")==0) {

                // load all positions into beepoVacs
                for (ClientBeepoVac beepoVac : bg.players) {
                    float x = test.positions.poll();
                    float y = test.positions.poll();
                    beepoVac.setBeepoVacPos(x,y, level);
                }
            }
        }
    }

    @Override
    public int getID() {
        return MainGame.PLAYINGSTATE;
    }

}
