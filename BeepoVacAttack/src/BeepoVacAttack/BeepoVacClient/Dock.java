package BeepoVacAttack.BeepoVacClient;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Dock extends Entity {

    public Dock(final float x, final float y) {
        super(x,y);
        addImageWithBoundingBox(ResourceManager
                .getImage("BeepoVacAttack/resources/Dock.png"));
    }

    public void update(final int delta) {}

}


