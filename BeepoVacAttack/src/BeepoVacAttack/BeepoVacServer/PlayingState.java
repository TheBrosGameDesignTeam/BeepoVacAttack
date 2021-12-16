package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.BeepoVacClient.ClientBeepoVac;
import BeepoVacAttack.GamePlay.*;
import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.Map;
import BeepoVacAttack.Networking.Listener;
import BeepoVacAttack.Networking.Packet;
import com.sun.tools.javac.Main;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

public class PlayingState extends BasicGameState {

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        container.setSoundOn(false);

        // add bunnies for level 1
//        MainGame.bunnies.add(new DustBunny(800, 900)); // testing
//        MainGame.bunnies.add(new DustBunny(900, 900)); // testing
        MainGame.bunnies.add(new DustBunny(1000, 900)); // front room
        MainGame.bunnies.add(new DustBunny(2085, 1419)); // bathroom
        MainGame.bunnies.add(new DustBunny(318, 1422)); // balcony

        MainGame.enemy = new Enemy(500, 500);

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

            // update node values using dijkstra's
            dijkstras(bg, MainGame.players.get(0));

            // update the dust bunnies

            for (DustBunny bun: MainGame.bunnies) {
                Vector bunPos = MainGame.bunnies.get(MainGame.bunnies.indexOf(bun)).getPosition();
                Vector finalPos = Map.getMap()[Math.round(bun.getX()/100)][Math.round(bun.getY()/100)].getPi();
                bun.update(delta, finalPos);
            }
//            Vector bun = MainGame.bunnies.getFirst().getPosition();
//            Vector pos = Map.getMap()[Math.round(bun.getX()/100)][Math.round(bun.getY()/100)].getPi();
//            MainGame.bunnies.forEach((bunny) -> bunny.update(delta, pos));

            // get the pos of each player and each bun and save it in a snapshot
            Packet retPack = new Packet("snapshot");

            // check if any of the bunnies intersect with any of the vacs
            for (DustBunny bunny: MainGame.bunnies) {
                for (BeepoVac vac: MainGame.players) {
                    float distance = bunny.getPosition().distance(vac.getPosition());
                    if (distance < vac.getRadius()){
                        System.out.println("Take this bun off the list: " + MainGame.bunnies.indexOf(bunny));
                        retPack.setRemoveThisBun(MainGame.bunnies.indexOf(bunny));
                    }
                }
            }

            retPack.setSnapshot(MainGame.players, MainGame.bunnies);
            MainGame.observer.send(retPack);

        }

    }

    public void dijkstras(MainGame game, BeepoVac target) {
        MapNode[][] map = Map.getMap();

        Vector up = new Vector(0, -1);
        Vector right = new Vector(1, 0);
        Vector left = right.scale(-1);
        Vector down = up.scale(-1);
        int ratio = 100;

        Set<MapNode> seen = new HashSet<>();
        PriorityQueue<MapNode> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.getDi()));

        for (MapNode[] nodes : map) {
            for (MapNode node : nodes) {
                node.setDi(1000);
                node.setPi(null);
                pq.add(node);
            }
        }

        MapNode current = map[(int)target.getX()/ratio][(int)target.getY()/ratio];
        pq.remove(current);
        current.setDi(0);
        target.setDi(0);
        pq.add(current);

        while (!pq.isEmpty()) {
            MapNode u = pq.remove();
            seen.add(u);

            int weight = u.getDi() + 1;
            int ux = (int) u.getX()/ratio;
            int uy = (int) u.getY()/ratio;
            ArrayList<MapNode> adjacents = getAdjacents(game, u);

            for (MapNode n : adjacents) {
                if (n.getDi() > weight) {
                    pq.remove(n);
                    n.setDi(weight);
                    Vector point;
                    if (n.getX()/ratio == ux + 1) { point = left; }
                    else if (n.getX()/ratio == ux - 1) { point = right; }
                    else if (n.getY()/ratio == uy + 1) { point = up; }
                    else { point = down; }
                    n.setPi(point);
                    pq.add(n);
                }
            }

        }

    }

    public ArrayList<MapNode> getAdjacents(MainGame game, MapNode u) {
        MapNode[][] map = Map.getMap();
        int ratio = 100;
        int ux = (int) u.getX()/ratio;
        int uy = (int) u.getY()/ratio;

        ArrayList<MapNode> adjacents = new ArrayList<>(8);
        if (ux < Map.getRows()-1) adjacents.add(map[(ux)+1][uy]);
        if (ux > 0)             adjacents.add(map[(ux)-1][uy]);
        if (uy < Map.getCols()-1) adjacents.add(map[ux][(uy)+1]);
        if (uy > 0)             adjacents.add(map[ux][(uy)-1]);

        return adjacents;
    }

    @Override
    public int getID() {
        return MainGame.PLAYINGSTATE;
    }

}
