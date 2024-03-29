package BeepoVacAttack.BeepoVacClient;

//import BeepoVacAttack.BeepoVacServer.MainGame;
import BeepoVacAttack.GamePlay.*;
import BeepoVacAttack.GamePlay.Map;
import Tweeninator.TweenManager;
import jig.ConvexPolygon;
import jig.Entity;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import BeepoVacAttack.Networking.Packet;

import jig.ResourceManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayingState extends BasicGameState {

    private Level level = null;
    private Vector cameraPosition = new Vector(0,0);
    private float cameraScale = 1f;
    private final float playerSpeed = 100;

    private final int initialTime = 1000 * 50;
    private int timeRemaining = initialTime;

    private boolean canChange = false;
    private boolean debug = true;

	private GameOverScreen gameOverScreen;
	private boolean canQuitOrPlayAgain = false;
    private boolean doneWithGame = false;

    public Entity underneath;

    private Vector up = new Vector(0, -1);
    private Vector right = new Vector(1, 0);
    private Vector left = right.scale(-1);
    private Vector down = up.scale(-1);

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
        gameOverScreen = new GameOverScreen(() -> { canQuitOrPlayAgain = true; System.out.println("Player can now quit or play again"); });
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(true);

        MainGame bg = (MainGame)game;

        cameraPosition = new Vector(0,0);
        timeRemaining = initialTime;
        gameOverScreen.reset();
        canQuitOrPlayAgain = false;
        bg.players.get(bg.whichPlayer - 1).setIsMoving(false);

        // init dock stations for this level
        bg.docks.clear();
        bg.docks.add(new Dock(1745,782));
        bg.docks.add(new Dock(531,1057));
        bg.docks.add(new Dock(2080,135));

        // init bunnies
        bg.bunnies.clear();
        for (int i=0; i<3; i++) bg.bunnies.add(new ClientDustBunny());

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

    private String msToTimeString(int ms)
    {
        long tu = TimeUnit.MILLISECONDS.toSeconds(ms);
        return "Time: " + tu;
    }

    private void centerCameraAt(Vector position, Graphics g) {
        // Store some stuff
        float hWidth = MainGame.getWidth() / 2;
        float hHeight = MainGame.getHeight() / 2;

        float camX = hWidth - position.getX();
        float camY = hHeight - position.getY();

        // Translate the camera
        g.translate(camX, camY);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;

        boolean c = container.getInput().getControllerCount() > 0;

        centerCameraAt(cameraPosition, g);

        level.renderBackground(g);

        bg.docks.forEach(
            (dock) -> dock.render(g)
        );

        bg.players.forEach(
            (player) -> player.render(g)
        );

        for (ClientDustBunny bunny : bg.bunnies)  {
            if (!bunny.caught) bunny.render(g);
        }

        // render the switch icon when you are near a dock
        if (this.canChange) {
            g.drawImage(ResourceManager.getImage(c ? MainGame.SWITCH_JOYCON_IMG : MainGame.SWITCH_IMG),bg.players.get(bg.whichPlayer-1).getX()-50,
                    bg.players.get(bg.whichPlayer-1).getY()-88);
        }

        g.setColor(Color.white);
        g.setFont(MainGame.getNormalFont());

        for (Bang b : bg.explosions) {
            b.render(g);
            if (b.isActive()) g.drawString("+30 sec", b.getX(), b.getY());
        }

        level.renderOverlay(g, bg.players.get(bg.whichPlayer-1).getUnderSomething());

        g.resetTransform();
        g.setColor(Color.white);
        g.setFont(MainGame.getNormalFont());

        g.drawString(msToTimeString(timeRemaining), 10, 40);
        g.drawString(String.format("%.0f", 100 - level.getDustMap().getPercentRemaining()) + "%", 10, 40 + 5 + 25);

        // Draw game over stuff
        gameOverScreen.render(g, c);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        MainGame.deltaDup = delta;
        TweenManager.update(delta);
        Input input = container.getInput();
        MainGame bg = (MainGame)game;
        ClientBeepoVac myBeepoVac = bg.players.get(bg.whichPlayer - 1);

        boolean c = input.getControllerCount() > 0;

        // If the game is over, don't send any more output
        if (timeRemaining < 0)
        {
            if (!doneWithGame && myBeepoVac.getIsMoving()) {
                myBeepoVac.stopVacSound();
                doneWithGame = true;
            }

            // If the player can quit or play again, handle those options!
            if (canQuitOrPlayAgain && bg.whichPlayer == 1)
            {

                // Quit game
                if (input.isKeyDown(Input.KEY_Q) || (c && input.isControlPressed(MainGame.JOYCON_DOWN)))
                {
                    System.exit(0);
                }

                // Restart the level
                else if (input.isKeyPressed(Input.KEY_P) || (c && input.isControlPressed(MainGame.JOYCON_RIGHT)))
                {
                    Packet pack = new Packet("restart");
                    pack.setPlayer(bg.whichPlayer);
                    bg.caller.push(pack);
                    System.out.println("restart message sent");
                }
            }

            // Wait for a restart command
            while (!MainGame.queue.isEmpty()) {
                Object message = MainGame.queue.poll();
                Packet test = (Packet) message;

                // Check if this packet is telling us to restart
                if (test.getRestart()) {
                    System.out.println("Time to restart!");
                    bg.enterState(MainGame.PLAYINGSTATE);
                }
            }
            return;
        }


        // Restart the level
//        if (input.isKeyPressed(Input.KEY_P) || (c && input.isControlPressed(MainGame.JOYCON_RIGHT)))
//        {
//            Packet pack = new Packet("restart");
//            pack.setPlayer(bg.whichPlayer);
//            bg.caller.push(pack);
//            System.out.println("restart message sent");
//            return;
//        }

        float deltaAdjustedSpeed = playerSpeed * ((float)delta / 1000.0f);

        // Deal with player input. Get it ready to send
        Packet pack = null;
        String sendMove = "";

        if (input.isKeyDown(Input.KEY_A) || (c && input.isControllerLeft(0))) {
            sendMove += "a";
            cameraPosition = cameraPosition.add(left.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_D) || (c && input.isControllerRight(0))) {
            sendMove += "d";
            cameraPosition = cameraPosition.add(right.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_S) || (c && input.isControllerDown(0))) {
            sendMove += "s";
            cameraPosition = cameraPosition.add(down.scale(deltaAdjustedSpeed));
        }
        if (input.isKeyDown(Input.KEY_W) || (c && input.isControllerUp(0))) {
            sendMove += "w";
            cameraPosition = cameraPosition.add(up.scale(deltaAdjustedSpeed));
        }

        // SOUND: check if moved
        if (sendMove.compareTo("") == 0) {
            if (myBeepoVac.getIsMoving()) myBeepoVac.setIsMoving(false);
        } else if (!myBeepoVac.getIsMoving()) myBeepoVac.setIsMoving(true);

        // SOUND: check if still moving to loop non-start sound
        if (myBeepoVac.getIsMoving() && !ResourceManager.getSound(MainGame.VAC_ON_SOUND).playing()) {
            myBeepoVac.contVacSound();
        }

        // check proximity between docker and this client
        for (Dock dock : bg.docks) {
            float dockDistance = dock.getPosition().distance(myBeepoVac.getPos());
            if (dockDistance < 50) {
                this.canChange = true;
                break;
            } else {
                this.canChange = false;
            }
        }

        // check if you want to change vac
        if ((input.isKeyPressed(Input.KEY_E) || (c && input.isControlPressed(MainGame.JOYCON_RIGHT, 0))) && this.canChange) {
            sendMove += "e";
            ResourceManager.getSound(MainGame.CLICK_SOUND).play();
        }

        // send concatenated string
        pack = new Packet(sendMove);
        pack.setPlayer(bg.whichPlayer);
        bg.caller.push(pack);

        // take in the game state and apply it.
        while (!MainGame.queue.isEmpty()) {
            Object message = MainGame.queue.poll();
            Packet test = (Packet) message;

            // Check if this packet is telling us to restart
            if (test.getRestart())
            {
                System.out.println("Time to restart!");
                bg.enterState(MainGame.PLAYINGSTATE);
                return;
            }
            // make sure this is a snapshot
            if (test.getMessage().compareTo("snapshot")==0) {

                // load all positions into beepoVacs
                for (ClientBeepoVac beepoVac : bg.players) {
                    float x = test.vacPositions.poll();
                    float y = test.vacPositions.poll();
                    // set vac type too
                    beepoVac.setBeepoVacType(test.vacTypes.poll());
                    // andddddd the direction IDK
                    beepoVac.setBeepoVacDir(test.vacDirections.poll());
                    beepoVac.setBeepoVacPos(x, y, level);
                    beepoVac.setUnderSomething(test.underSomething.poll());
                }

                // load all positions into dustBunnies
                for (ClientDustBunny dustBunny : bg.bunnies) {
                    if (test.enemyPositions.size() > 0) {
                        float x = test.enemyPositions.poll();
                        float y = test.enemyPositions.poll();
                        if (!dustBunny.caught) dustBunny.setDustBunnyPos(x, y);
                    }
                }

                // set the caught bunny to caught and add seconds to timer
                if (test.getRemoveThisBun() != 100 && !bg.bunnies.get(test.getRemoveThisBun()).caught) {
                    // add bang animation to array
                    bg.explosions.add(new Bang(bg.bunnies.get(test.getRemoveThisBun()).getX()+10,
                            bg.bunnies.get(test.getRemoveThisBun()).getY()+10));
                    bg.bunnies.get(test.getRemoveThisBun()).setCaught();
                    // add 30 secs to the timer
                    timeRemaining+=30000;
                }

            }
        }


        cameraPosition = new Vector(myBeepoVac.getX(), myBeepoVac.getY());

        timeRemaining -= delta;
        if (timeRemaining < 0)
        {
            System.out.println("TIME UP");
            gameOverScreen.animateIn(level.getPercentClear());
        }

    }

    @Override
    public int getID() {
        return MainGame.PLAYINGSTATE;
    }

}
