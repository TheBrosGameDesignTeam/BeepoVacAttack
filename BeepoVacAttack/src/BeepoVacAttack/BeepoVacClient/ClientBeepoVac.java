package BeepoVacAttack.BeepoVacClient;

public class ClientBeepoVac {

    private float x = 0;
    private float y = 0;

    // Store where to draw the image
    public void setBeepoVacPos(float x, float y) {
        this.x = x; this.y = y;
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }

//    public void setPos(float x, float y) { this.x = x; this.y = y; }

}
