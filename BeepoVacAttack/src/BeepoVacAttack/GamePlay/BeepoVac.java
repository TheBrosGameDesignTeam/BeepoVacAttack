package BeepoVacAttack.GamePlay;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class BeepoVac extends MapNode {

    private final float speed = 0.2f;
    private final Vector up = new Vector(0,-speed);
    private final Vector left = new Vector(-speed,0);
    private final Vector down = new Vector(0,speed);
    private final Vector right = new Vector(speed,0);
    Vector move;
    Vector lastPosition;

    private int vacType = 1;
    private int direction = 0;
    private int radius = 50;

    private Vector defaultPosition;

    public BeepoVac(final float x, final float y) {
        super(x, y);
        this.move = new Vector(0, 0);
        lastPosition = getPosition();
        addShape(new ConvexPolygon(30f), new Vector(15f,15f));
    }

    public void resetPosition()
    {
        this.setPosition(defaultPosition);
        direction = 0;
        vacType = 1;
    }

    public void setMove(String move, int delta) {
        lastPosition = getPosition();

        Vector newMove = new Vector(0,0);

        if (move.contains("w")) {
            newMove = newMove.add(this.up);
        }
        if (move.contains("a")) {
            newMove = newMove.add(this.left);
        }
        if (move.contains("s")) {
            newMove = newMove.add(this.down);
        }
        if (move.contains("d")) {
            newMove = newMove.add(this.right);
        }

        this.setBeepoVacDir(move);

        // cycle threw values for vacs
        if (move.contains("e")) {
            if (this.vacType == 2) this.vacType = 0;
            else this.vacType++;
            this.setRadius();
        }

//        System.out.println(newMove);
        this.translate(newMove.scale(delta));

    }

    public void setBeepoVacDir(String dirs) {
        this.direction = switch (dirs) {
            case "d" -> 0;
            case "s" -> 2;
            case "a" -> 4;
            case "w" -> 6;
            case "ds" -> 1;
            case "as" -> 3;
            case "aw" -> 5;
            case "dw" -> 7;
            default -> this.direction;
        };
    }

    public int getVacType() { return this.vacType; }
    public int getVacDirection() { return this.direction; }

    public int getRadius() { return this.radius; }
    public void setRadius() {
        if (this.vacType == 0) this.radius = 25;
        else if (this.vacType == 1) this.radius = 30;
        else this.radius = 40;
    }

    public void update(final int delta) {}

    public void handleCollision() {
        move = new Vector(0,0);
        setPosition(lastPosition);
    }
}
