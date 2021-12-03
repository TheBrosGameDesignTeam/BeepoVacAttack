package BeepoVacAttack.BeepoVacClient;

import BeepoVacAttack.GamePlay.Level;
import jig.ResourceManager;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ClientBeepoVac {

    public static String RES_PLAYER_IMG_SRC = "BeepoVacAttack/resources/beepovacs_v2.png";
    private static Image playerImage = null;

    public static void loadResources() {
        ResourceManager.loadImage(RES_PLAYER_IMG_SRC);
    }

    // default starting position
    private float x = 100;
    private float y = 100;

    private int radius = 30;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y, Level l) {
        this.x = x; this.y = y;

        l.eraseCircle(Math.round(x) + radius, Math.round(y) + radius, radius);
    }

    public void render(Graphics g) {
        Image img = ResourceManager.getImage(RES_PLAYER_IMG_SRC);
        int rows = 3;
        int cols = 4;

        int srcX = 0;
        int srcY = 0;
        int srcX2 = img.getWidth() / cols;
        int srcY2 = img.getHeight() / rows;

        g.drawImage(
                img,
                getX() - radius, getY() - radius,
                getX() + radius * 2, getY() + radius * 2,
                srcX, srcY,
                srcX2, srcY2
        );
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

}
