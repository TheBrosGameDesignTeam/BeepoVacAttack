package BeepoVacAttack.GamePlay;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.lwjgl.Sys;

public class DustBunny extends Entity {

    private final float speed = 1f;
    private final Vector start = new Vector(speed,0);
    Vector move;
    Vector lastPosition;
    public boolean isCaught = false;
    public boolean isRoaming = true;

    private int timer;
    private int time = 500;

    private Vector defaultPosition;

    public DustBunny(final float x, final float y) {
        super(x,y);
        this.defaultPosition = new Vector(x, y);
        this.move = start;
        timer = time;
        addShape(new ConvexPolygon(30f), new Vector(30f,30f));
        lastPosition = getPosition();
    }

    public void resetPosition()
    {
        this.setPosition(defaultPosition);
        resetTimer();
    }

    public void resetTimer() {
        timer = time;
    }

    public void setIsRoaming(boolean value) { isRoaming = value; }

    public void setRandomMove() {
        float length = start.length();
        move = Vector.getRandom(length);
        // System.out.println("Setting to random vector");
    }

    public void update(final int delta, Vector pos) {
        timer -= delta;
//        System.out.println("Setting bun direction to: " + pos);
        lastPosition = getPosition();

        if (timer <= 0) {
            if (pos != null && !isRoaming) move = pos.negate();    // set bunny direction to opposite of vac
            else setRandomMove();
            resetTimer();
        }

        move = move.unit().scale(speed);

        this.translate(move);
    }

    public void handleCollision() {
        setRandomMove();
        setPosition(lastPosition);
    }
}
