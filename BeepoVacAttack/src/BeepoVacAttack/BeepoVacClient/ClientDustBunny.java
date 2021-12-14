package BeepoVacAttack.BeepoVacClient;

import BeepoVacAttack.GamePlay.Level;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ClientDustBunny {

    public static String RES_PLAYER_IMG_SRC = "BeepoVacAttack/resources/beepovacs_v3.png";
    public static String RES_BUNNY_IMG_SRC = "BeepoVacAttack/resources/bunny.png";

    public boolean caught = false;

    int aniPoint1 = 0;
    int aniPoint2 = 0;

    // testing
    public static String RES_BUNNY_ANI = "BeepoVacAttack/resources/dustbunny.png";

    private static Image playerImage = null;

    public static void loadResources() {
        ResourceManager.loadImage(RES_PLAYER_IMG_SRC);
        ResourceManager.loadImage(RES_BUNNY_IMG_SRC);

        ResourceManager.loadImage(RES_BUNNY_ANI);
    }

    // default starting position
    private float x = 900;
    private float y = 1000;

    private int direction = 0;
    private int radius = 30;

    // Store where to draw the image
    public void setDustBunnyPos(float x, float y) {
        this.x = x; this.y = y;
    }

    public void setDustBunnyDir(String dirs) {
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

    public void render(Graphics g) {
        // Image img = ResourceManager.getImage(RES_PLAYER_IMG_SRC);
        SpriteSheet sprite = new SpriteSheet(ResourceManager.getImage(RES_BUNNY_ANI), 485, 387);
//        Image img = ResourceManager.getImage(RES_BUNNY_IMG_SRC);
        int rows = 2;
        int cols = 4;

        int srcX = 0;
        int srcY = 0;
//        int srcX2 = img.getWidth() / cols;
//        int srcY2 = img.getHeight() / rows;

        int srcX2 = sprite.getWidth() / cols;
        int srcY2 = sprite.getHeight() / rows;

        g.drawImage(
                sprite.getSprite(aniPoint1, aniPoint2),
                getX() - radius, getY() - radius, // position
                getX() + radius * 2, getY() + radius * 2,
                srcX, srcY,     // what sprite
                srcX2, srcY2
        );

    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

    public void setCaught() { this.caught = true;}

}
