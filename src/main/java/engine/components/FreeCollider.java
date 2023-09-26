package engine.components;

import engine.Vec;
import engine.world.GridWorld;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This Class will handle collisions for non axis aligned objects.
 */

public class FreeCollider extends RenderComponent implements Component {

    public static final boolean RENDER_DEBUG_BOXES = false;
    private final GridWorld grid;
    private final Area area;
    public int width, height;
    public PositionComponent pos;
    private Class[] collides = {};
    public Color debugColor = new Color(1, 0, 0, 0.5F);

    public FreeCollider(PositionComponent pos, int width, int height, GridWorld grid) {
        super(pos);
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.grid = grid;
        this.area = new Area(new Rectangle(0, 0, width, height));
        this.z = 99;
    }

    public FreeCollider(PositionComponent pos, Shape shape, GridWorld grid) {
        super(pos);
        this.pos = pos;
        this.width = shape.getBounds().width;
        this.height = shape.getBounds().height;
        this.grid = grid;
        this.area = new Area(shape);
    }

    private static boolean getCollision(Area a1, Area a2) {
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    public void setCollides(Class[] collides) {
        this.collides = collides;
    }

    public Class[] getCollides() {
        return collides;
    }

    /**
     * Gets the shape of this collider
     *
     * @return The shape of this Collider
     */
    public Area getShape() {
        AffineTransform rotation = new AffineTransform();
        rotation.setToRotation(Math.toRadians((int) pos.getR()), (int) pos.getX(), (int) pos.getY());
        rotation.translate(pos.getIntX() - (width / 2F), pos.getIntY() - (height / 2F));
        return this.area.createTransformedArea(rotation);
    }

    /**
     * Tests for a collision between this collider at a given position
     *
     * @param collidables A list of colliders to check collisions against
     * @param position    The position this collider should be in
     * @return if a collision is found
     */
    public boolean testForCollision(ArrayList<FreeCollider> collidables, Vec position) {
        collidables.remove(this);
        PositionComponent pos = new PositionComponent(position);
        pos.setR(this.pos.getR());
        FreeCollider tester = new FreeCollider(pos, this.width, this.height, grid);

        for (FreeCollider f : collidables) {
            if (FreeCollider.getCollision(tester.getShape(), f.getShape())) {
                return true;
            }
        }

        return false;
    }

    public boolean testForCollisionWithObject(FreeCollider collidable, Vec position) {
        PositionComponent pos = new PositionComponent(position);
        pos.setR(this.pos.getR());
        FreeCollider tester = new FreeCollider(pos, this.width, this.height, grid);
        return FreeCollider.getCollision(tester.getShape(), collidable.getShape());
    }

    public boolean localCollisionTest(Vec move, Class[] toFind) {
        return testForCollision(getNearby(move, toFind), move);
    }

    public ArrayList<FreeCollider> getNearby(Vec position, Class[] toFind) {
        int gpX = grid.getGridPosX(position.getX());
        int gpY = grid.getGridPosX(position.getY());

        //Class[] test  = {Tank.class, Wall.class};
        //Arrays.stream(test).anyMatch(f -> f.isInstance(e))
        return (ArrayList<FreeCollider>) grid.getNearbyCollidables(gpX, gpY)
                .stream()
                .filter(e -> (Arrays.stream(toFind).anyMatch(f -> f.isInstance(e))))
                .map(e -> e.getComponent(FreeCollider.class))
                .collect(Collectors.toList());
    }

    //For debugging only.
    public void toRender(GraphicsContext gc) {
        gc.setFill(debugColor);
        gc.fillRect(pos.getX() - (width / 2F), pos.getY() - (height / 2F), width, height);
    }

    public Vec getPos() {
        return pos.get();
    }

    @Override
    public ComponentType getType() {
        if (RENDER_DEBUG_BOXES) return ComponentType.RENDERING;
        else return ComponentType.SIMPLE;
    }
}
