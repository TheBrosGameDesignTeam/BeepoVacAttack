package BeepoVacAttack.BeepoVacClient;

import BeepoVacAttack.GamePlay.Level;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ClientBeepoVac {

    public static String RES_PLAYER_IMG_SRC = "BeepoVacAttack/resources/beepovacs_v3.png";
    private static Image playerImage = null;

    public static void loadResources() {
        ResourceManager.loadImage(RES_PLAYER_IMG_SRC);
    }

    // default starting position
    private float x = 100;
    private float y = 100;

    private int direction = 0;
    private int radius = 30;
    private int vacType = 1;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y, Level l) {
        this.x = x; this.y = y;

        l.eraseCircle(Math.round(x) + radius / 2, Math.round(y) + radius / 2, Math.round(radius));
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

    public void setBeepoVacType() {

        // cycle threw values for vacs
        if (this.vacType == 2) this.vacType = 0;
        else this.vacType++;

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

}
