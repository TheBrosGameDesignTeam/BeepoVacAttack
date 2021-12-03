package BeepoVacAttack.GamePlay;

import jig.Vector;

import java.util.LinkedList;

public class LevelFurnitureInstance extends LevelObject {
    private String name;
    private LinkedList<LevelObject> subobjects = null;

    public String getName() { return name; }
    public LinkedList<LevelObject> getSubobjects() { return subobjects; }

    public LevelFurnitureInstance(String name, Vector pos) {
        this.name = name;
        this.setPosition(pos);
    }

    @Override
    public LevelObject copy() {
        System.out.println("don't copy a furniture instance you doofus");
        return null;
    }

    public void build(Level level) {
        LevelFurnitureRecipe recipe = level.getFurnitureRecipes().get(getName());
        LinkedList<LevelObject> originalSubobjects = recipe.getSubobjects();

        subobjects = new LinkedList<>();

        for (LevelObject o : originalSubobjects) {
            LevelObject copy = o.copy();
            Vector origPos = copy.getPosition();
            Vector newPos = new Vector(origPos.getX() + getPosition().getX(), origPos.getY() + getPosition().getY());
            copy.setPosition(newPos);
            subobjects.add(copy);
        }
    }
}
