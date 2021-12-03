package BeepoVacAttack.GamePlay;

import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.File;

public class LevelImage extends LevelObject {
    private String src = "";
    public String getSrc() { return src; }
    public void setSrc(String src) {
        this.src = src;

        try {
            image = new Image("BeepoVacAttack/resources/level/" + this.src);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private Image image = null;
    public Image getImage() { return image; }

    public LevelImage(String src, Vector position, Vector size) {
        this.setSrc(src);
        this.setPosition(position);
        this.setSize(size);
    }

    public LevelObject copy() {
        return new LevelImage(src, getPosition(), getSize());
    }
}
