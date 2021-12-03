package BeepoVacAttack.GamePlay;

import jig.Vector;

public abstract class LevelObject {
    private Vector position;
    private Vector size;

    public Vector getPosition() { return position.copy(); }
    public Vector getSize() { return (size == null ? null : size.copy()); }

    public void setPosition(Vector position) { this.position = position.copy(); }
    public void setSize(Vector size) { this.size = (size == null ? null : size.copy()); }

    public abstract LevelObject copy();
}
