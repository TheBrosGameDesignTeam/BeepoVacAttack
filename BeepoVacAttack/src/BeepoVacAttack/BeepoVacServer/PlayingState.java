package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.DustBunny;
import BeepoVacAttack.Networking.Listener;
import BeepoVacAttack.Networking.Packet;
import com.sun.tools.javac.Main;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayingState extends BasicGameState {

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        // add bunnies for level 1
        MainGame.bunnies.add(new DustBunny(900, 900)); // front room
        MainGame.bunnies.add(new DustBunny(2085, 1419)); // bathroom
        MainGame.bunnies.add(new DustBunny(318, 1422)); // balcony

    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;
        g.drawString("We are playing!", 100, 100);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game,
                       int delta) throws SlickException {

        Input input = container.getInput();
        MainGame bg = (MainGame)game;

        // read from ConcurrentLinkedQueue
        while (!MainGame.queue.isEmpty()) {

            Object message = MainGame.queue.poll();
            Packet pack = (Packet) message;

            // move which player that pack belongs to
            if (pack.getPlayer() == 1) {
                MainGame.players.get(0).setMove(pack.getMessage());
            } else if (pack.getPlayer() == 2){
                MainGame.players.get(1).setMove(pack.getMessage());
            }

            // update the dust bunnies
            MainGame.bunnies.forEach((bunny) -> bunny.update(delta));

            // get the pos of each player and each bun and save it in a snapshot
            Packet retPack = new Packet("snapshot");

            // check if any of the bunnies intersect with any of the vacs
            for (DustBunny bun: MainGame.bunnies) {
                for (BeepoVac vac: MainGame.players) {
                    float distance = bun.getPosition().distance(vac.getPosition());
                    if (distance < vac.getRadius()){
                        System.out.println("Take this bun off the list: " + MainGame.bunnies.indexOf(bun));
                        retPack.setRemoveThisBun(MainGame.bunnies.indexOf(bun));
                    }
                }
            }

            retPack.setSnapshot(MainGame.players, MainGame.bunnies);
            MainGame.observer.send(retPack);

        }

    }

    @Override
    public int getID() {
        return MainGame.PLAYINGSTATE;
    }

}
