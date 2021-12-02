package BeepoVacAttack.BeepoVacClient;

public class ClientBeepoVac {

    // default starting position
    private float x = 100;
    private float y = 100;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y) {
        this.x = x; this.y = y;
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

}
