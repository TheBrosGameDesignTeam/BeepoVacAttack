package BeepoVacAttack.BeepoVacClient;
import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.Networking.Packet;
import BeepoVacAttack.Networking.*;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.*;
import java.net.Socket;

class StartUpState extends BasicGameState {

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        BeepoVacAttack.BeepoVacClient.MainGame bg = (BeepoVacAttack.BeepoVacClient.MainGame)game;

        Socket socket = null;
        try {
            socket = new Socket("localhost", 4999);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        bg.listener = new Listener(inputStream, MainGame.queue);
        bg.caller = new Caller(outputStream);

        bg.listener.start();

    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {

        MainGame bg = (MainGame)game;

        // Draw background colors
        g.setColor(new Color(170, 160, 255));
        g.fillRect(0, 0, MainGame.getWidth(), MainGame.getHeight());

        g.setColor(Color.white);
        g.setFont(MainGame.getNormalFont());

        String line1 = "You Are Player " + bg.whichPlayer;
        g.drawString(line1, MainGame.xPosForStringCenteredAt(MainGame.getWidth() / 2, line1, MainGame.getNormalFont()), MainGame.getHeight() * 0.4f);

        String line2 = "---";
        if (bg.whichPlayer == 1) line2 = "Press Space to Start!";
        else  line2 = "Waiting for Player 1 to Start...";

        g.drawString(line2, MainGame.xPosForStringCenteredAt(MainGame.getWidth() / 2, line2, MainGame.getNormalFont()), MainGame.getHeight() * 0.5f);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        MainGame bg = (MainGame)game;

        // press space if you are ready to start the game!
        if (input.isKeyPressed(Input.KEY_SPACE)){
            // send both the player and the message.
            Packet pack = new Packet("space");
            pack.setPlayer(bg.whichPlayer);
            bg.caller.push(pack);
        }

        // this is where the client gets info back from the observer
        while (!MainGame.queue.isEmpty()) {

            Object message = MainGame.queue.poll();

            if (message instanceof Packet pack) {

                // here we check to see which player this client is
                if (pack.getMessage().compareTo("player") == 0) {

                    // assign which player if not assigned
                    if (bg.whichPlayer == 0) bg.whichPlayer = pack.getPlayer();
                    System.out.println("I am player " + bg.whichPlayer);

                } else if (pack.getMessage().compareTo("goToPlayingState") == 0) {
                    // create the beepovacs & bunnies
                    for (int i=0; i<pack.getHowManyPlayers(); i++) {
                        bg.players.add(new ClientBeepoVac(100, 100));
                    }
                    bg.enterState(MainGame.PLAYINGSTATE);
                }
            }
        }
    }

    @Override
    public int getID() {
        return MainGame.STARTUPSTATE;
    }

}
