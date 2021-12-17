package BeepoVacAttack.GamePlay;

import jig.Vector;

public class Map {
    private static final int rows = 26;
    private static final int cols = 19;
    private static final int ratio = 100;           // one node for every 100 pixels
    public static MapNode[][] map = new MapNode[rows][cols];

    public Map() {
        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                map[x][y] = new MapNode(x * ratio, y * ratio);
            }
        }
    }

    public static int getRows() { return rows; }
    public static int getCols() { return cols; }
    public static MapNode[][] getMap() { return map; }




}