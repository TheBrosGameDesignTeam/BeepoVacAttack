package BeepoVacAttack.BeepoVacClient;
import BeepoVacAttack.GamePlay.Level;
import BeepoVacAttack.GamePlay.Map;
import BeepoVacAttack.Networking.*;
import jig.Entity;

import jig.ResourceManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MainGame extends StateBasedGame {
    public static MainGame instance;

    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE = 1;
    public static int deltaDup; 

    public static final String DOCK_IMG = "BeepoVacAttack/resources/Dock.png";
    public static final String SWITCH_IMG = "BeepoVacAttack/resources/switch.png";

    public static final String RES_FONT = "BeepoVacAttack/resources/font/FredokaOne-Regular.ttf";
    public static String BANG_SPRITE = "BeepoVacAttack/resources/dustexplosion.png";

    private UnicodeFont normalFont;
    public static UnicodeFont getNormalFont() { return instance.normalFont; }

    private UnicodeFont largeFont;
    public static UnicodeFont getLargeFont() { return instance.largeFont; }

    private UnicodeFont titleFont;
    public static UnicodeFont getTitleFont() { return instance.titleFont; }

    // networking
    public static ConcurrentLinkedQueue<Object> queue;
    Caller caller;
    Listener listener;

    // gameplay
    public int whichPlayer = 0;
    public LinkedList<ClientBeepoVac> players;
    public LinkedList<ClientDustBunny> bunnies;

    // animations
    public ArrayList<Bang> explosions;

    public LinkedList<Dock> docks;
    // public Map map;

    // store screen width and height
    public final int ScreenWidth;
    public final int ScreenHeight;

    public MainGame(String title, int width, int height) {
        super(title);
        ScreenWidth = width;
        ScreenHeight = height;

        queue = new ConcurrentLinkedQueue<Object>();
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        players = new LinkedList<ClientBeepoVac>();
        bunnies = new LinkedList<ClientDustBunny>();
        docks = new LinkedList<Dock>();
        // map = new Map();

        // add an array of explosions
        explosions = new ArrayList<Bang>(3);

        instance = this;

        ClientBeepoVac.loadResources();
        ClientDustBunny.loadResources();
        Level.loadResources();
    }

    public static int getWidth() { return instance.ScreenWidth; }
    public static int getHeight() { return instance.ScreenHeight; }

    public static int xPosForStringCenteredAt(int x, String text, org.newdawn.slick.Font font)
    {
        return x - font.getWidth(text) / 2;
    }


    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());
        ResourceManager.loadImage(DOCK_IMG);
        ResourceManager.loadImage(SWITCH_IMG);
        ResourceManager.loadImage(BANG_SPRITE);

        // Load font
        normalFont = new UnicodeFont(RES_FONT, 25, false, false);
        normalFont.getEffects().add(new OutlineEffect(3, Color.black));
        normalFont.getEffects().add(new ColorEffect(Color.white));
        normalFont.addAsciiGlyphs();
        normalFont.loadGlyphs();

        largeFont = new UnicodeFont(RES_FONT, 50, false, false);
        largeFont.getEffects().add(new OutlineEffect(3, Color.black));
        largeFont.getEffects().add(new ColorEffect(Color.white));
        largeFont.addAsciiGlyphs();
        largeFont.loadGlyphs();

        titleFont = new UnicodeFont(RES_FONT, 70, false, false);
        titleFont.getEffects().add(new OutlineEffect(3, Color.black));
        titleFont.getEffects().add(new ColorEffect(Color.white));
        titleFont.addAsciiGlyphs();
        titleFont.loadGlyphs();

        container.setDefaultFont(getNormalFont());
    }

    public static void main(String[] args) {

        AppGameContainer app;
        try {
            Thread.sleep(1000);
            System.out.println("ok sleep is done");
            app = new AppGameContainer(new MainGame("Client", 800, 600));
            app.setDisplayMode(800, 600, false);
            app.setVSync(true);
            app.setAlwaysRender(true);
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


