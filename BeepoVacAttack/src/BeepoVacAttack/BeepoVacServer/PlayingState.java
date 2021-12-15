package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.BeepoVacClient.ClientBeepoVac;
import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.DustBunny;
import BeepoVacAttack.GamePlay.Map;
import BeepoVacAttack.GamePlay.MapNode;
import BeepoVacAttack.Networking.Listener;
import BeepoVacAttack.Networking.Packet;
import com.sun.tools.javac.Main;
import jig.Collision;
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
        Collision collision;

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
            Vector bun = MainGame.bunnies.getFirst().getPosition();
            Vector pos = Map.getMap()[Math.round(bun.getX()/100)][Math.round(bun.getY()/100)].getPi();
            MainGame.bunnies.forEach((bunny) -> bunny.update(delta, pos));

            for (BeepoVac player: MainGame.players) {
                for (BeepoVac other: MainGame.players) {
                    collision = player.collides(other);
                    if (collision != null && other != player) {
                        System.out.println("Collision (BeepoVac v BeepoVac");
                        player.handleCollision();
                        other.handleCollision();
                    }
                }
                for (DustBunny other: MainGame.bunnies) {
                    collision = player.collides(other);
                    if (collision != null) {
                        System.out.println("Collision (BeepoVac v Bunny");
                        player.handleCollision();
                    }
                }
            }

            for (DustBunny bunny: MainGame.bunnies) {
                for (BeepoVac other: MainGame.players) {
                    collision = bunny.collides(other);
                    if (collision != null) {
                        System.out.println("Collision (Bunny v BeepoVac)");
                        bunny.handleCollision();
                        other.handleCollision();
                    }
                }
                for (DustBunny other: MainGame.bunnies) {
                    collision = bunny.collides(other);
                    if (collision != null && other != bunny) {
                        System.out.println("Collision (Bunny v Bunny)");
                        bunny.handleCollision();
                        other.handleCollision();
                    }
                }
            }

            // get the pos of each player and save it in a snapshot
            Packet retPack = new Packet("snapshot");
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
