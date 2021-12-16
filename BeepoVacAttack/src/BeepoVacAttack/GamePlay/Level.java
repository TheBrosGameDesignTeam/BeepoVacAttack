package BeepoVacAttack.GamePlay;

import BeepoVacAttack.BeepoVacClient.ClientBeepoVac;
import BeepoVacAttack.BeepoVacClient.MainGame;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

/***
 * Using tutorial from https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm
 * for parsing XML documents.
 */

public class Level {
    private LinkedList<LevelImage> images = new LinkedList<>();
    private LinkedList<LevelWall> walls = new LinkedList<>();
    private LinkedList<LevelSurface> surfaces = new LinkedList<>();
    private LinkedList<LevelDustArea> dustAreas = new LinkedList<>();
    private LinkedList<LevelFurnitureInstance> furnitureInstances = new LinkedList<>();
    private HashMap<String, LevelFurnitureRecipe> furnitureRecipes = new HashMap<>();

    private DustMap dustMap = null;
    public Image staticDustMapImage = null;

    public LinkedList<LevelImage> getImages() { return images; }
    public LinkedList<LevelWall> getWalls() { return walls; }
    public LinkedList<LevelSurface> getSurfaces() { return surfaces; }
    public LinkedList<LevelDustArea> getDustAreas() { return dustAreas; }
    public HashMap<String, LevelFurnitureRecipe> getFurnitureRecipes() { return furnitureRecipes; }
    public LinkedList<LevelFurnitureInstance> getFurnitureInstances() { return furnitureInstances; }

    public static final String RES_NOISE_IMG = "BeepoVacAttack/resources/noise4.png";

    public static void loadResources()
    {
        ResourceManager.loadImage(RES_NOISE_IMG);
    }

    public static Level fromXML(String pathToXML) throws Exception {
        Level newLevel = new Level();

        File xmlFile = new File("BeepoVacAttack/BeepoVacAttack/src/BeepoVacAttack/resources/level/" + pathToXML);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDoc = builder.parse(xmlFile);

        System.out.println(xmlDoc.getDocumentElement().getNodeName());

        NodeList topLevelNodes = xmlDoc.getDocumentElement().getChildNodes();

        for (int i = 0; i < topLevelNodes.getLength(); i++) {
            if (topLevelNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element)topLevelNodes.item(i);

            String name = e.getNodeName();

            if (name.equals("FurnitureRecipes")) {
                processFurnitureRecipes(e, newLevel);
            }
            if (name.equals("World")) {
                processWorld(e, newLevel);
            }
        }

        // Now done parsing XML; let's build that furniture!
        newLevel.buildFurniture();

        return newLevel;
    }

    private static void processWorld(Element worldNode, Level newLevel) {
        for (int i = 0; i < worldNode.getChildNodes().getLength(); i++) {
            if (worldNode.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element)worldNode.getChildNodes().item(i);
            LevelObject object = processLevelObject(e);

            newLevel.addLevelObject(object);
        }
    }

    private static void processFurnitureRecipes(Element furnitureNode, Level newLevel) {
        for (int i = 0; i < furnitureNode.getChildNodes().getLength(); i++) {
            if (furnitureNode.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element)furnitureNode.getChildNodes().item(i);
            LevelFurnitureRecipe recipe = processSingleFurnitureRecipe(e);

            newLevel.addFurnitureRecipe(recipe);
        }
    }

    private static LevelFurnitureRecipe processSingleFurnitureRecipe(Element root) {
        String recipeName = root.getAttribute("name");
        LinkedList<LevelObject> subobjects = new LinkedList<>();

        // Get all of the subitems of this furniture recipe
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            if (root.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element)root.getChildNodes().item(i);
            LevelObject object = processLevelObject(e);
            subobjects.add(object);
        }

        LevelFurnitureRecipe recipe = new LevelFurnitureRecipe(recipeName, subobjects);
        return recipe;
    }

    private static LevelObject processLevelObject(Element e) {
        String name = e.getNodeName();

        String x = e.getAttribute("x");
        String y = e.getAttribute("y");
        String w = e.getAttribute("w");
        String h = e.getAttribute("h");

        String r = e.getAttribute("r");

        boolean onlyForTall = e.getAttribute("onlyForTall").equalsIgnoreCase("true");

        Vector pos = null;
        if (!x.equals("") && !y.equals("")) {
            pos = new Vector(Float.parseFloat(x), Float.parseFloat(y));
        }

        Vector dim = null;
        if (!w.equals("") && !h.equals("")) {
            dim = new Vector(Float.parseFloat(w), Float.parseFloat(h));
        }

        Float radius = null;
        if (!r.equals("")) {
            radius = Float.parseFloat(r);
        }


        LevelObject object = null;

        if (name == "Image") {
            String src = e.getAttribute("src");
            object = new LevelImage(src, pos, dim);
        }

        else if (name == "Wall") {
            object = new LevelWall(pos, dim, radius, onlyForTall);
        }

        else if (name == "Surface") {
            String surfaceTypeString = e.getAttribute("type");

            LevelSurfaceType surfaceType = LevelSurfaceType.NORMAL;
            if (surfaceTypeString.equals("carpet")) surfaceType = LevelSurfaceType.CARPET;
            else if (surfaceTypeString.equals("tile")) surfaceType = LevelSurfaceType.TILE;

            object = new LevelSurface(pos, dim, surfaceType);
        }

        else if (name == "DustArea") {
            object = new LevelDustArea(pos, dim);
        }

        else if (name == "Furniture") {
            object = new LevelFurnitureInstance(e.getAttribute("name"), pos);
        }
        return object;
    }

    public void addLevelObject(LevelObject object) {
        if (LevelImage.class.isAssignableFrom(object.getClass())) {
            addImage((LevelImage) object);
        }

        else if (LevelWall.class.isAssignableFrom(object.getClass())) {
            addWall((LevelWall) object);
        }

        else if (LevelSurface.class.isAssignableFrom(object.getClass())) {
            addSurface((LevelSurface) object);
        }

        else if (LevelDustArea.class.isAssignableFrom(object.getClass())) {
            addDustArea((LevelDustArea) object);
        }

        else if (LevelFurnitureInstance.class.isAssignableFrom(object.getClass())) {
            addFurnitureInstance((LevelFurnitureInstance) object);
        }
    }
    public void addImage(LevelImage image) { images.add(image); }
    public void addWall(LevelWall wall) { walls.add(wall); }
    public void addSurface(LevelSurface surface) { surfaces.add(surface); }
    public void addDustArea(LevelDustArea dustArea) { dustAreas.add(dustArea); }
    public void addFurnitureRecipe(LevelFurnitureRecipe recipe) {
        System.out.println("Added furniture recipe with name " + recipe.getName());
        furnitureRecipes.put(recipe.getName(), recipe);
    }
    public void addFurnitureInstance(LevelFurnitureInstance instance) { furnitureInstances.add(instance); }

    public void buildFurniture() {
        for (LevelFurnitureInstance instance : furnitureInstances) {
            instance.build(this);
        }
    }

    public void initializeDustMap() {
        dustMap = new DustMap(2671, 1917, 0.15f);
        dustMap.resetClear();

        for (LevelDustArea dustArea : dustAreas) {
            dustMap.paintDustInRectangle(dustArea.getPosition(), dustArea.getSize());
        }

        for (LevelWall wall : walls) {
            dustMap.clearDustInRectangle(wall.getPosition(), wall.getSize());
            // Map.setWallsUnreachable(wall.getPosition(), wall.getSize());
        }

        dustMap.updateInitialFillAmount();
        staticDustMapImage = dustMap.getSlickImage();
    }

    public float getPercentClear() { return dustMap.getPercentRemaining(); }

    public Image getSlickDustMapImage()
    {
        return dustMap.getSlickImage();
    }

    public int eraseCircle(int x, int y, int radius) {
        return dustMap.removeDust(new Vector(x, y), radius);
    }

    public float getDustMapRatio() {
        return dustMap.getRatio();
    }

    public DustMap getDustMap() {
        return dustMap;
    }


    public void renderBackground(Graphics g) {
        for (LevelImage image : getImages()) {
            renderLevelImage(image, g, 1.0f);
        }

        Image dustMapImage = getSlickDustMapImage();
        if (dustMapImage != null) {


            ClientBeepoVac b = MainGame.instance.players.get(MainGame.instance.whichPlayer - 1);
            float x = Math.min(Math.max(0, b.getX() - MainGame.getWidth() / 2), 2671 - MainGame.getWidth());
            float y = Math.min(Math.max(0, b.getY() - MainGame.getHeight() / 2), 1917 - MainGame.getHeight());

            g.setDrawMode(Graphics.MODE_ALPHA_MAP);
            dustMapImage.draw(
                    x, y,
                    MainGame.getWidth(),
                    MainGame.getHeight(),
                    new Color(225, 190, 100, 80)
            );

            g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
            ResourceManager.getImage(RES_NOISE_IMG).draw(
                    0, 0,
                    2671,
                    1917,
                    new Color(225, 190, 100, 80)
            );

            g.setDrawMode(Graphics.MODE_NORMAL);
        }

//        for (LevelSurface surface : getSurfaces()) {
//            renderLevelSurface(surface, g);
//        }
//
//        for (LevelWall wall : getWalls()) {
//            renderLevelWall(wall, g);
//        }
    }

    public void renderOverlay(Graphics g, boolean loweredOpacity) {        // Render the furniture!
        for (LevelFurnitureInstance item : getFurnitureInstances()) {
            for (LevelObject object : item.getSubobjects()) {
                if (LevelImage.class.isAssignableFrom(object.getClass())) {
                    renderLevelImage((LevelImage) object, g, loweredOpacity ? 0.3f : 1.0f);
                }
//                else if (LevelWall.class.isAssignableFrom(object.getClass())) {
//                    renderLevelWall((LevelWall) object, g);
//                }
//                else if (LevelSurface.class.isAssignableFrom(object.getClass())) {
//                    renderLevelSurface((LevelSurface) object, g);
//                }
            }
        }
    }

    private void renderLevelImage(LevelImage image, Graphics g, float opacity) {
        if (image.getImage() == null) return;

        float x = image.getPosition().getX();
        float y = image.getPosition().getY();
        float w = image.getSize().getX();
        float h = image.getSize().getY();

        Image img = image.getImage();
        g.setColor(new Color(1.0f, 1.0f, 1.0f, opacity));

        image.getImage().draw(x, y, w, h, new Color(1.0f, 1.0f, 1.0f, opacity));
    }

    private void renderLevelWall(LevelWall wall, Graphics g) {
        Vector pos = wall.getPosition();
        Vector size = wall.getSize();

        Float radius = wall.getRadius();

        g.setColor(new Color(1.0f, 0f, 0f, 0.5f));

        if (size != null) {
            g.fillRect(pos.getX(), pos.getY(), size.getX(), size.getY());
        } else {
            g.fillOval(pos.getX() - radius, pos.getY() - radius, radius * 2, radius * 2);
        }

        g.setColor(Color.blue);

        if (size != null) {
            g.drawRect(pos.getX(), pos.getY(), size.getX(), size.getY());
        } else {
            g.drawOval(pos.getX() - radius, pos.getY() - radius, radius * 2, radius * 2);
        }

    }

    private void renderLevelSurface(LevelSurface surface, Graphics g) {
        Vector pos = surface.getPosition();
        Vector size = surface.getSize();

        g.setColor(new Color(0.0f, 1f, 0f, 0.5f));
        g.fillRect(pos.getX(), pos.getY(), size.getX(), size.getY());

        g.setColor(Color.blue);
        g.drawRect(pos.getX(), pos.getY(), size.getX(), size.getY());

        g.setColor(Color.magenta);
        g.drawString(String.valueOf(surface.getType()), pos.getX(), pos.getY());
    }
}
