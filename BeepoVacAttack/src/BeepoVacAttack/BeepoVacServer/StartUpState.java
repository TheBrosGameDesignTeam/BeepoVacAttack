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

            Object message = MainGame.queue.poll();

            if (message instanceof Packet pack) {

                // if the first player presses space, start the game
                if (pack.getPlayer()==1 && pack.getMessage().compareTo("space")==0) {

                    // create each BeepoVac
                    for (int i=0; i < MainGame.listeners.size(); i++){
                        MainGame.players.add(new BeepoVac(100, 100));
                    }

                    bg.enterState(MainGame.PLAYINGSTATE);
                }

            } else if (message instanceof Listener listener) {

                MainGame.listeners.add(listener);
                System.out.println("Player " + listener.getPlayer() + " has joined!!!");

                // send back which player the client is
                Packet pack = new Packet("player");
                pack.setPlayer(listener.getPlayer());

                // this sends that a player has joined
                MainGame.observer.send(pack);
            }
        }

    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}