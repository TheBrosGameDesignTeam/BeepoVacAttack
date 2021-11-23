package BeepoVacAttack.BeepoVacServer;
import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.Networking.Listener;
import BeepoVacAttack.Networking.Packet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class StartUpState extends BasicGameState {

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        MainGame.observer = new Observer();

        // start up server thread
        Server server = new Server(2);
        server.start();

    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;
        g.drawString("Accepting Connections", 100, 100);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        MainGame bg = (MainGame)game;

        // read from ConcurrentLinkedQueue
        while (!MainGame.queue.isEmpty()) {

            System.out.println("Msg received");

            Object message = MainGame.queue.poll();

            if (message instanceof Packet) {

                Packet pack = (Packet) message;

                // if the first player presses space, start the game
                if (pack.getPlayer() == 1 && pack.getMessage().compareTo("space") == 0) {
                    System.out.println("Go to playing state!");
                    // send the object to the observer - confirm that we change states
                    MainGame.observer.send(message);
                    // go to next state
                    bg.enterState(MainGame.PLAYINGSTATE);
                }

            } else if (message instanceof Listener) {
                Listener listener = (Listener) message;
                MainGame.listeners.add(listener);
                MainGame.players.add(new BeepoVac(100, 100));
                System.out.println("Player " + listener.getPlayer() + " has joined!!!");
            }
        }

    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}