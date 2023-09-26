package objects.grid;

import engine.Entity;
import engine.handler.Handler;
import engine.components.FreeCollider;
import engine.components.PositionComponent;
import engine.world.GridWorld;

public class WorldBorder extends Entity {

    public WorldBorder(Handler handler, int x, int y, GridWorld grid) {
        super(handler);
        grid.addObject(this, x, y);

        PositionComponent pos = new PositionComponent(grid.getCentre(x, y));
        this.addComponent(new FreeCollider(pos, 80, 80, handler.getGridWorld()));
    }
}
