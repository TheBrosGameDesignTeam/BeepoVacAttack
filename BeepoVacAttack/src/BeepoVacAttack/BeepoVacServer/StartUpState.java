package BeepoVacAttack.BeepoVacServer;
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

//                System.out.println("Message Received from player " + pack.getPlayer());
                System.out.println(pack.getMessage());

                // move the player
                if (pack.getPlayer() == 1) {
                    System.out.println("Player " + pack.getPlayer() + " is ready!");
//                    bg.players[0].setMove(pack.getMessage());
                    bg.players[0] = new BeepoVac(200, 200);
                } else if (pack.getPlayer() == 2) {
                    System.out.println("Player " + pack.getPlayer() + " is ready!");
//                    bg.players[1].setMove(pack.getMessage());
                    bg.players[1] = new BeepoVac(400, 200);
                }

                if (bg.players[0] != null && bg.players[1] != null) {
                    // go to next state
                    System.out.println("We are ready to start the game!!!");

                }

                // send the object to the observer - testing
                MainGame.observer.send(message);

            } else if (message instanceof Listener) {
                MainGame.listeners.add((Listener) message);
//                System.out.println(MainGame.listeners.peek());
//                bg.player[] = new BeepoVac(200, 200);
            }
        }

//        // update player - put this in next state
//        for (BeepoVac beep : bg.players) {
//            if (beep != null) beep.update(delta);
//        }

        // send confirmation to each client to say we are changing states.

    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}