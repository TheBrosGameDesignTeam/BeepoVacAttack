package BeepoVacAttack.BeepoVacClient;

import BeepoVacAttack.GamePlay.Level;
import BeepoVacAttack.GamePlay.MapNode;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ClientBeepoVac {

    public static String RES_PLAYER_IMG_SRC = "BeepoVacAttack/resources/beepovacs_v3.png";
    private static Image playerImage = null;

    private boolean underSomething = false;
    private boolean isMoving = false;

    // default starting position
    private float x = 100;
    private float y = 100;

    private float volume = 1f;
    private float pitch = 1f;

    private int direction = 0;
    private int radius = 30;
    private int vacType = 1;

    public int timer = -100;

    public static void loadResources() {
        ResourceManager.loadImage(RES_PLAYER_IMG_SRC);
    }

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y, Level l) {
        this.x = x; this.y = y;

        l.eraseCircle(Math.round(x) + radius / 2, Math.round(y) + radius / 2, Math.round(radius));
    }

    public void setBeepoVacDir(int dirs) {
        this.direction = dirs;
    }

    public void setBeepoVacType(int vacType) {
        // cycle through values for vacs
        this.vacType = vacType;
        setRadiusAndSound(vacType);
    }

    public int getBeepoVacType() {
        return this.vacType;
    }

    public void setRadiusAndSound(int vacType) {
        switch (vacType) {
            case 0 -> {
                this.radius = 25;
                this.volume = .75f;
                this.pitch = .7f;
            }
            case 2 -> {
                this.radius = 40;
                this.volume = 1.5f;
                this.pitch = 1.5f;
            }
            default -> {
                this.radius = 30;
                this.volume = 1f;
                this.pitch = 1f;
            }
        }
    }

    public void render(Graphics g) {
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

    public void onVacSound() {
        ResourceManager.getSound(MainGame.VAC_OFF_SOUND).stop();
        ResourceManager.getSound(MainGame.VAC_ON_SOUND).play(pitch, volume);
    }

    public void contVacSound() {
        ResourceManager.getSound(MainGame.VAC_GO_SOUND).loop(pitch, volume);
    }

    public void stopVacSound() {
        ResourceManager.getSound(MainGame.VAC_ON_SOUND).stop();
        ResourceManager.getSound(MainGame.VAC_GO_SOUND).stop();
        ResourceManager.getSound(MainGame.VAC_OFF_SOUND).play(pitch, volume);
    }

    public boolean getIsMoving() { return this.isMoving; }

    public void setIsMoving(boolean value) {
        if (!isMoving && value) onVacSound();
        else if (isMoving && !value) stopVacSound();
        this.isMoving = value;
    }

    public boolean getUnderSomething() { return this.underSomething; }
    public void setUnderSomething(boolean value) { this.underSomething = value; }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

    public Vector getPos() { return new Vector(this.x, this.y); }

}
