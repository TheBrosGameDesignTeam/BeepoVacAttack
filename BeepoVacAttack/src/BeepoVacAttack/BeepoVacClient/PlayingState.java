package BeepoVacAttack.BeepoVacClient;

//import BeepoVacAttack.BeepoVacServer.MainGame;
import jig.ResourceManager;
import jig.Vector;
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
    private Vector cameraPosition = new Vector(0,0);
    private float cameraScale = 1f;
    private final float playerSpeed = 100;

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        try {
            level = Level.fromXML("ExampleLevel.xml");
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

    private void centerCameraAt(Vector position, Graphics g) {
        // Store some stuff
        float hWidth = MainGame.getWidth() / 2;
        float hHeight = MainGame.getHeight() / 2;

        // Translate the camera so that the center is at the origin
        g.translate(MainGame.getWidth(), MainGame.getHeight());

        // Translate the camera back
        g.translate(-hWidth - position.getX(), -hHeight - position.getY());

        g.pushTransform();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;
        g.drawString("We are playing!", 100, 100);

        centerCameraAt(cameraPosition, g);

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

        Vector up = new Vector(0, -1);
        Vector right = new Vector(1, 0);
        Vector left = right.scale(-1);
        Vector down = up.scale(-1);
        float deltaAdjustedSpeed = playerSpeed * ((float)delta / 1000.0f);

        Packet pack = null;

        // temp - need to refactor
        if (input.isKeyDown(Input.KEY_A)){
            pack = new Packet("a");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
            cameraPosition = cameraPosition.add(left.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_D)){
            pack = new Packet("d");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
            cameraPosition = cameraPosition.add(right.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_S)){
            pack = new Packet("s");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
            cameraPosition = cameraPosition.add(down.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_W)){
            pack = new Packet("w");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
            cameraPosition = cameraPosition.add(up.scale(deltaAdjustedSpeed));
        }

        ClientBeepoVac myBeepoVac = bg.players.get(bg.whichPlayer - 1);

        cameraPosition = new Vector(myBeepoVac.getX(), myBeepoVac.getY());

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
