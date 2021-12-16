package BeepoVacAttack.BeepoVacClient;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class Bang extends Entity {

    private final Animation explosion;

    public Bang(final float x, final float y) {
        super(x, y);
        SpriteSheet sprite = new SpriteSheet(ResourceManager.getImage(MainGame.BANG_SPRITE), 294, 256);

        explosion = new Animation(sprite, 50);
        addAnimation(explosion);
        explosion.setLooping(false);
        ResourceManager.getSound(MainGame.POOF_SOUND).play(1f, .5f);
    }

    public boolean isActive() {
        return !explosion.isStopped();
    }

}
