package BeepoVacAttack.GamePlay;

import java.util.LinkedList;

public class LevelFurnitureRecipe {
    private String name;
    private LinkedList<LevelObject> subobjects;

    public String getName() { return name; }
    public LinkedList<LevelObject> getSubobjects() { return subobjects; }

    public LevelFurnitureRecipe(String name, LinkedList<LevelObject> subobjects) {
        this.name = name;
        this.subobjects = subobjects;
    }

}
