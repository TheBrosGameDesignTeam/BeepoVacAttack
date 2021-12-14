package BeepoVacAttack.BeepoVacClient;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Bang extends Entity {

    private final Animation explosion;

    public Bang(final float x, final float y) {
        super(x, y);
        SpriteSheet sprite = new SpriteSheet(ResourceManager.getImage(MainGame.BANG_SPRITE), 566, 493);

        explosion = new Animation(sprite, 40);
        addAnimation(explosion);
        explosion.setLooping(false);

    }

    public boolean isActive() {
        return !explosion.isStopped();
    }

}
