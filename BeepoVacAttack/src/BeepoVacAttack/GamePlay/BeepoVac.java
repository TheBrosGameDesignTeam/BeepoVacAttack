package BeepoVacAttack.GamePlay;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class BeepoVac extends MapNode {

    private float speed = 0.2f;
    private Vector up = new Vector(0,-speed);
    private Vector left = new Vector(-speed,0);
    private Vector down = new Vector(0,speed);
    private Vector right = new Vector(speed,0);
    Vector move;
    Vector lastPosition;

    private boolean onCarpet = false;
    private boolean underSomething = false;

    private final ConvexPolygon smallShape = new ConvexPolygon(15f);
    private final ConvexPolygon mediumShape = new ConvexPolygon(30f);
    private final ConvexPolygon largeShape = new ConvexPolygon(45f);

    private final Vector smallOffset = new Vector(7.5f,7.5f);
    private final Vector mediumOffset = new Vector(15f,15f);
    private final Vector largeOffset = new Vector(22.5f,22.5f);

    private int vacType = 1;
    private int direction = 0;
    private int radius = 50;

    private Vector defaultPosition;

    public BeepoVac(final float x, final float y) {
        super(x, y);
        this.defaultPosition = new Vector(x, y);
        this.move = new Vector(0, 0);
        lastPosition = getPosition();
        addShape(mediumShape, mediumOffset);
    }

    public void resetPosition()
    {
        this.setPosition(defaultPosition);
        direction = 0;
        vacType = 1;
    }

    public void setMove(String move, int delta) {
        lastPosition = getPosition();
        float moveScale = 1f;

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
            switch (vacType) {
                case 0:
                    System.out.println("Switching to small vac");
                    removeShape(largeShape);
                    addShape(smallShape, smallOffset);
                    speed = 0.3f;
                    break;
                case 1:
                    System.out.println("Switching to medium vac");
                    removeShape(smallShape);
                    addShape(mediumShape, mediumOffset);
                    setPosition(getPosition().add(new Vector(0f,15f)));
                    speed = 0.2f;
                    break;
                case 2:
                    System.out.println("Switching to large vac");
                    removeShape(mediumShape);
                    addShape(largeShape, largeOffset);
                    setPosition(getPosition().add(new Vector(0f,30f)));
                    speed = 0.1f;
                    break;
            }
            up = new Vector(0,-speed);
            left = new Vector(-speed,0);
            down = new Vector(0,speed);
            right = new Vector(speed,0);
        }

//        System.out.println(newMove);
        if (onCarpet) {
            moveScale = getVacType() == 0 ? 0.3f :
                    getVacType() == 1 ? 0.5f :
                            1f;
        }

        moveScale *= 20f;
        this.translate(newMove.scale(delta).scale(moveScale));

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

    public void setOnCarpet(boolean onCarpet) {
        this.onCarpet = onCarpet;
    }

    public boolean getUnderSomething() { return this.underSomething; }
    public void setUnderSomething(boolean value) { this.underSomething = value; }
}
