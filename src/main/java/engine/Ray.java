package engine;

import engine.components.FreeCollider;
import engine.components.PositionComponent;
import engine.world.GridWorld;
import objects.grid.WallConnector;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Ray {
    static final float MOVE_DISTANCE = 5F;
    static final float MAX_RANGE = 10000F;
    GridWorld grid;
    Vec startPos;
    Vec rotation;

    public Ray(Vec pos, Vec rotate, GridWorld grid) {
        this.startPos = pos;
        this.rotation = rotate;
        this.grid = grid;
        rotation.setMagnitude(MOVE_DISTANCE);
    }

    public Collision getCollision() {
        PositionComponent pos = new PositionComponent(startPos);
        int moves = 0;

        while ((float) moves * MOVE_DISTANCE < MAX_RANGE) {
            int cX = grid.getGridPosX(pos.get().getX());
            int cY = grid.getGridPosY(pos.get().getY());

            if (cX < 0 || cX > grid.dimX || cY < 0 || cY > grid.dimY) {
                //System.out.println("Ray OOB @:" + cX + ", " + cY);
                return new Collision(pos.get(), null);
            }

            Collision gTest = evalGrid(pos.get());

            if (gTest.getEntity() != null) return gTest;
            else pos.setPos(gTest.getPos());

            moves++;
        }

        //System.out.println("Ray OOR");

        return new Collision(pos.get(), null);
    }

    private Collision evalGrid(Vec startPos) {
        PositionComponent pos = new PositionComponent(startPos);
        FreeCollider tester = new FreeCollider(pos, new Ellipse2D.Float(0, 0, 1, 1), grid);
        int gX = grid.getGridPosX(startPos.getX());
        int gY = grid.getGridPosY(startPos.getY());

        //System.out.println("Eval cell:" + gX + ", " + gY);

        ArrayList<Entity> collidables = grid.getNearbyCollidables(gX, gY);

        while (grid.getGridPosX(pos.getX()) == gX && grid.getGridPosY(pos.getY()) == gY) {
            for (Entity c : collidables) {
                FreeCollider target = c.getComponent(FreeCollider.class);
                if (target.testForCollisionWithObject(tester, target.getPos())) {
                    return new Collision(pos.get(), c);
                }
            }

            pos.setPos(Vec.add(pos.get(), rotation));
        }

        return new Collision(pos.get(), null);
    }
}
