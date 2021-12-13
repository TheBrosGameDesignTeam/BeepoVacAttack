package BeepoVacAttack.GamePlay;

import BeepoVacAttack.BeepoVacClient.ClientBeepoVac;
import BeepoVacAttack.BeepoVacClient.MainGame;
import jig.Vector;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;

public class DustMap {
    // The original width, height, and scaling ratio
    private int externalWidth;
    private int externalHeight;
    private float ratio;
    public float getRatio() { return ratio; }
    // The image we perform drawing operations on
    private BufferedImage image;

    // The original map to compare against
    private int initialFillAmount = 0;

    // The graphics object we use to draw on the dust map
    private Graphics2D imageGraphics;

    private org.newdawn.slick.Image slickImage = null;
    public org.newdawn.slick.Image getSlickImage() { return slickImage; }

    public DustMap(int width, int height, float ratio)
    {
        this.externalWidth = width;
        this.externalHeight = height;
        this.ratio = ratio;
        image = new BufferedImage(
                Math.round(width * ratio),
                Math.round(height * ratio),
                BufferedImage.TYPE_INT_ARGB
        );

        imageGraphics = (Graphics2D) image.getGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        try {
            slickImage = new org.newdawn.slick.Image(Math.round(MainGame.getWidth() * ratio), Math.round(MainGame.getHeight() * ratio));
        }  catch (SlickException e) {
            e.printStackTrace();
        }
        resetClear();
    }

    public void reset()
    {
        imageGraphics.setColor(Color.red);
        imageGraphics.setComposite(AlphaComposite.Src);
        imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        updateSlickImage();
    }

    public void resetClear()
    {
        imageGraphics.setComposite(AlphaComposite.Clear);
        imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        updateSlickImage();
    }

    public void paintDustInRectangle(Vector pos, Vector size) {
        imageGraphics.setColor(Color.red);
        imageGraphics.setComposite(AlphaComposite.Src);
        imageGraphics.fillRect(
                Math.round(pos.getX() * ratio),
                Math.round(pos.getY() * ratio),
                Math.round(size.getX() * ratio),
                Math.round(size.getY() * ratio)
        );
        updateSlickImage();
    }

    public void clearDustInRectangle(Vector pos, Vector size) {
        imageGraphics.setComposite(AlphaComposite.Clear);
        imageGraphics.fillRect(
                Math.round(pos.getX() * ratio),
                Math.round(pos.getY() * ratio),
                Math.round(size.getX() * ratio),
                Math.round(size.getY() * ratio)
        );
        updateSlickImage();
    }

    public void updateInitialFillAmount() {
        initialFillAmount = getFilledPixels();
    }

    public int removeDust(Vector pos, int radius)
    {
        /*
        Removes the dust within a given radius of a given position, and returns the number of amount of dust removed
        (i.e. the number of pixels that were made transparent). This is done by counting the number of transparent
        pixels before and after the painting operation.

        Q: Isn't that an inefficient use of resources?
        A: Your face is inefficient use of resources. (Okay, yes, since most of the map isn't changing, the majority
            of the time is spent counting pixels we know won't change. I'll probably optimize this at some point so that
            it only counts the pixels in a smaller area around the position, but for now I am tired so this will have
            to do.)
         */
        int totalDustBefore = getClearPixels();
        imageGraphics.setComposite(AlphaComposite.Clear);
        imageGraphics.fillOval(
                Math.round(pos.scale(ratio).getX() - radius * ratio),
                Math.round(pos.scale(ratio).getY() - radius * ratio),
                Math.round(radius * 2 * ratio),
                Math.round(radius * 2 * ratio)
        );
        int totalDustAfter = getClearPixels();
        int dustPickedUp = totalDustAfter - totalDustBefore;


        updateSlickImage();
        return dustPickedUp;
    }

    public int getTotalPixels() { return image.getWidth() * image.getHeight(); }

    public int getClearPixels() { return getPixelsWithAlpha(0x00); }
    public int getFilledPixels() { return getPixelsWithAlpha(0xFF); }
    public int getPixelsWithAlpha(int alpha) {
        int clearedPixels = 0;
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
            {
                int a = (image.getRGB(x,y) & 0xFF000000) >>> 4*6;
                if (a == alpha) clearedPixels++;
            }
        return clearedPixels;
    }

    public float getPercentRemaining()
    {
        return (getFilledPixels() * 100f) / initialFillAmount;
    }

    public void updateSlickImage()
    {
        // Every frame, it looks like we have to create a new Texture from the BufferedImage.
        // If we could find a more performant way to do this, that would be great.
        Texture i = null;
        try
        {
            ClientBeepoVac b = MainGame.instance.players.get(MainGame.instance.whichPlayer - 1);

            float x = Math.min(Math.max(0, b.getX() - MainGame.getWidth() / 2), 2671 - MainGame.getWidth());
            float y = Math.min(Math.max(0, b.getY() - MainGame.getHeight() / 2), 1917 - MainGame.getHeight());

            int subimageX = Math.round(x * ratio);
            int subimageY = Math.round(y * ratio);


            BufferedImage subimage = image.getSubimage(
                    subimageX, subimageY,
                    Math.round(MainGame.getWidth() * ratio), Math.round(MainGame.getHeight() * ratio)
            );
            i = BufferedImageUtil.getTexture("image", subimage);
            slickImage = new org.newdawn.slick.Image(i);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RasterFormatException e) {
            e.printStackTrace();
        }


    }


}
