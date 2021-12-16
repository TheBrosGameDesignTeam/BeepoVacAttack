package BeepoVacAttack.GamePlay;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class BeepoVac extends MapNode {

    private final Vector up = new Vector(0,-3);
    private final Vector left = new Vector(-3,0);
    private final Vector down = new Vector(0,3);
    private final Vector right = new Vector(3,0);
    Vector move;

    private int vacType = 1;
    private int direction = 0;
    private int radius = 50;

    private Vector defaultPosition;

    public BeepoVac(final float x, final float y) {
        super(x,y);
        this.defaultPosition = new Vector(x, y);
        this.move = new Vector(0,0);
    }

    public void resetPosition()
    {
        this.setPosition(defaultPosition);
        direction = 0;
        vacType = 1;
    }

    public void setMove(String move) {

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
        }

//        System.out.println(newMove);
        this.translate(newMove);

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

    public void update(final int delta) {}

}
