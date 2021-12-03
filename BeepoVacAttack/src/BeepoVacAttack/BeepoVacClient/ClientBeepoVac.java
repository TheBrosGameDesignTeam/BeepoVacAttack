package BeepoVacAttack.BeepoVacClient;

import BeepoVacLevelEditor.Level;

public class ClientBeepoVac {

    // default starting position
    private float x = 100;
    private float y = 100;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y, Level l) {
        this.x = x; this.y = y;

        l.eraseCircle(Math.round(x) + 35, Math.round(y) + 35, 30);
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

}
