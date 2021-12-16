package BeepoVacAttack.BeepoVacClient;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

import jig.ResourceManager;
import org.newdawn.slick.SpriteSheet;

import javax.swing.*;
import java.awt.*;

public class ClientEnemy {

    private float x;
    private float y;

    public static String ENEMY_SRC = "BeepoVacAttack/resources/bunny.png";

    public void setPos(float x, float y) {
        this.x = x; this.y = y;

    }

    public void render(Graphics g) {

        Image img = ResourceManager.getImage(ENEMY_SRC);
        g.drawImage( img, this.x, this.y );

    }


}
