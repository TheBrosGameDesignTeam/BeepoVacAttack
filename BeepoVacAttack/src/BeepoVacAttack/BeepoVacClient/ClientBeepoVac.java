package BeepoVacAttack.BeepoVacClient;

import BeepoVacAttack.GamePlay.Level;
import BeepoVacAttack.GamePlay.MapNode;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ClientBeepoVac extends MapNode {

    public static String RES_PLAYER_IMG_SRC = "BeepoVacAttack/resources/beepovacs_v3.png";
    private static Image playerImage = null;

    private boolean underSomething = false;
    public boolean getUnderSomething() { return this.underSomething; }
    public void setUnderSomething(boolean value) { this.underSomething = value; }


    public ClientBeepoVac(float x, float y) {
        super(x, y);
    }

    public static void loadResources() {
        ResourceManager.loadImage(RES_PLAYER_IMG_SRC);
    }

    // default starting position
    private float x = 100;
    private float y = 100;

    private int direction = 0;
    private int radius = 30;
    private int vacType = 1;

    public int timer = -100;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y, Level l) {
        this.x = x; this.y = y;

        l.eraseCircle(Math.round(x) + radius / 2, Math.round(y) + radius / 2, Math.round(radius));
    }

    public void setBeepoVacDir(int dirs) {
        this.direction = dirs;
    }

    public void setBeepoVacType(int vacType) {
        // cycle threw values for vacs
        this.vacType = vacType;
        if (vacType == 0) this.radius = 25;
        else if (vacType == 1) this.radius = 30;
        else this.radius = 40;
    }

    public int getBeepoVacType() {
        return this.vacType;
    }

    public void render(Graphics g) {
        // Image img = ResourceManager.getImage(RES_PLAYER_IMG_SRC);
        SpriteSheet sprite = new SpriteSheet(ResourceManager.getImage(RES_PLAYER_IMG_SRC), 205, 205);
        int rows = 3;
        int cols = 8;

        int srcX = 0;
        int srcY = 0;
        int srcX2 = sprite.getWidth() / cols;
        int srcY2 = sprite.getHeight() / rows;

        g.drawImage(
                sprite.getSprite(direction, this.vacType),
                getX() - radius, getY() - radius,
                getX() + radius * 2, getY() + radius * 2,
                srcX, srcY,     // what sprite
                srcX2, srcY2
        );
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

    public Vector getPos() { return new Vector(this.x, this.y); }

}
