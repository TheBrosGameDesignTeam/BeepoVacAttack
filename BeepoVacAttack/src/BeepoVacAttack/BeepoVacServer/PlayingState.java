package BeepoVacAttack.BeepoVacServer;

import BeepoVacAttack.BeepoVacClient.ClientBeepoVac;
import BeepoVacAttack.GamePlay.*;
import BeepoVacAttack.GamePlay.Map;
import BeepoVacAttack.GamePlay.MapNode;
import BeepoVacAttack.GamePlay.BeepoVac;
import BeepoVacAttack.GamePlay.DustBunny;
import BeepoVacAttack.Networking.Listener;
import BeepoVacAttack.Networking.Packet;
import com.sun.tools.javac.Main;
import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

import static java.lang.System.exit;

public class PlayingState extends BasicGameState {
    public Level level;
    public Entity environment;
    public Entity underneath;
    public Entity carpet;
    public HashMap<String, LevelFurnitureRecipe> recipes;

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
        environment = new Entity();
        carpet = new Entity();
        underneath = new Entity();

        try {
            level = Level.fromXML("ExampleLevel.xml");
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }

        recipes = level.getFurnitureRecipes();

        for (LevelWall obj: level.getWalls()) {
            Vector offset;
            if (obj.getRadius() != null) {
                offset = new Vector(obj.getPosition().add(new Vector(obj.getRadius(), obj.getRadius())));
                environment.addShape(new ConvexPolygon(obj.getRadius()), offset);
            } else {
                offset = new Vector(obj.getPosition().add(new Vector(obj.getSize().scale(0.5f))));
//                environment.addShape(new ConvexPolygon(obj.getSize().getX(), obj.getSize().getY()), obj.getPosition());
                environment.addShape(new ConvexPolygon(obj.getSize().getX(), obj.getSize().getY()), offset);
            }
        }
        for (LevelFurnitureInstance furn: level.getFurnitureInstances()) {
            LevelFurnitureRecipe furnType = recipes.get(furn.getName());
//            for (LevelObject obj: furnType.getSubobjects()) {
//                Vector offset;
//                if (!(obj instanceof LevelWall)) {
//                    continue;
//                }
//                LevelWall wall = (LevelWall) obj;
//                if (wall.getRadius() != null) {
//                    offset = new Vector(furn.getPosition().add(wall.getPosition()));
//                    environment.addShape(new ConvexPolygon(wall.getRadius()), offset);
//                } else {
//                    offset = new Vector(furn.getPosition().add(wall.getPosition().add(wall.getSize().scale(0.5f))));
//                    environment.addShape(new ConvexPolygon(wall.getSize().getX(), wall.getSize().getY()), offset);
//                }
//            }
            for (int i = 0; i < furnType.getSubobjects().size(); i++) {
                LevelObject obj = furnType.getSubobjects().get(i);
                Vector offset;
                if (!(obj instanceof LevelWall)) {
                    continue;
                }

                LevelWall wall = (LevelWall) obj;
                if (furnType.getSubobjects().size() == 4 && i == 3 ||
                    furnType.getSubobjects().size() == 6 && i == 5) {
                    offset = new Vector(furn.getPosition().add(wall.getPosition().add(wall.getSize().scale(0.5f))));
                    underneath.addShape(new ConvexPolygon(wall.getSize().getX(), wall.getSize().getY()), offset);
                    continue;
                }
                if (wall.getRadius() != null) {
                    offset = new Vector(furn.getPosition().add(wall.getPosition()));
                    environment.addShape(new ConvexPolygon(wall.getRadius()), offset);
                } else {
                    offset = new Vector(furn.getPosition().add(wall.getPosition().add(wall.getSize().scale(0.5f))));
                    environment.addShape(new ConvexPolygon(wall.getSize().getX(), wall.getSize().getY()), offset);
                }
            }
        }
        for (LevelSurface surface: level.getSurfaces()) {
            Vector offset = surface.getPosition().add(surface.getSize().scale(0.5f));
            if (surface.getType() == LevelSurfaceType.CARPET) {
                System.out.println(surface.getSize());
                carpet.addShape(new ConvexPolygon(surface.getSize().getX(),surface.getSize().getY()), offset);
            }
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
                       Graphics g) throws SlickException {
        MainGame bg = (MainGame)game;

        environment.render(g);
        MainGame.players.forEach((player) -> player.render(g));
        MainGame.bunnies.forEach((bunny) -> bunny.render(g));
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

            System.out.println("Packet message is " + pack.getMessage());
            // Check if this is a "restart" command
            if (pack.getMessage().equals("restart"))
            {
                Packet ret = new Packet("restart");
                ret.setRestart();
                MainGame.players.forEach(beepoVac -> beepoVac.resetPosition());
                MainGame.bunnies.forEach(bunny -> bunny.resetPosition());
                MainGame.observer.send(ret);
                return;
            }

            // move which player that pack belongs to
            if (pack.getPlayer() == 1) {
                MainGame.players.get(0).setMove(pack.getMessage(), delta);
            } else if (pack.getPlayer() == 2){
                MainGame.players.get(1).setMove(pack.getMessage(), delta);
            }

            // update node values using dijkstra's
            dijkstras(bg, MainGame.players.get(0));

            // update the dust bunnies
            Vector bun = MainGame.bunnies.getFirst().getPosition();
            Vector pos = Map.getMap()[Math.round(bun.getX()/100)][Math.round(bun.getY()/100)].getPi();
            MainGame.bunnies.forEach((bunny) -> bunny.update(delta, pos));

            // get the pos of each player and each bun and save it in a snapshot
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
                    if (collision != null && !other.isCaught) {
                        System.out.println("Collision (BeepoVac v Bunny");
                        other.isCaught = true;
//                        player.handleCollision();
                    }
                }

                if (player.collides(environment) != null) {
                    System.out.println("Player colliding with environment");
                    player.handleCollision();
                }
                if (player.collides(carpet) != null) {
                    System.out.println("On carpet");
                    player.setOnCarpet(true);
                } else {
                    player.setOnCarpet(false);
                }
                if (player.getVacType() == 2 && player.collides(underneath) != null) {
                    player.handleCollision();
                }
            }

            for (DustBunny bunny: MainGame.bunnies) {
//                for (BeepoVac other: MainGame.players) {
//                    collision = bunny.collides(other);
//                    if (collision != null) {
//                        System.out.println("Collision (Bunny v BeepoVac)");
////                        bunny.handleCollision();
////                        other.handleCollision();
//                    }
//                }
                for (DustBunny other: MainGame.bunnies) {
                    collision = bunny.collides(other);
                    if (collision != null && other != bunny) {
                        System.out.println("Collision (Bunny v Bunny)");
                        bunny.handleCollision();
                        other.handleCollision();
                    }
                }
                if (bunny.collides(environment) != null) {
                    bunny.handleCollision();
                    System.out.println("Bunny colliding with environment");
                }
            }

            // get the pos of each player and save it in a snapshot
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
