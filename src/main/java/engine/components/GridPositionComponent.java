package engine.components;

import engine.Entity;
import engine.world.GridWorld;

public class GridPositionComponent implements TickingInterface {
    private final GridWorld grid;
    private final PositionComponent pos;
    private final Entity target;
    public boolean updatePos;
    public boolean recentCellChange;
    public int gridPosX = -1, gridPosY = -1;

    public GridPositionComponent(PositionComponent pos, GridWorld grid, Entity target) {
        this.pos = pos;
        this.grid = grid;
        this.target = target;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TICKING;
    }

    @Override
    public void tick() {

        recentCellChange = false;

        if (updatePos) {
            updateGridPos();
            updatePos = false;

            //TODO add final collision detection here. It should check each tick if in collision and attempt to restore non collision.
            //if (this.getComponent(TankAIController.class) == null) System.out.println(gridPosX + " " + gridPosY + " " +  handler.gridWorld.collider.getNearbyCollidables(gridPosX, gridPosY).size());

        }
    }

    public boolean updateGridPos() {
        int oldX = gridPosX;
        int oldY = gridPosY;

        this.gridPosX = grid.getGridPosX(pos.getX());
        this.gridPosY = grid.getGridPosY(pos.getY());

        if (oldX != gridPosX || oldY != gridPosY) {
            grid.updatePos(target, oldX, oldY, gridPosX, gridPosY);
            recentCellChange = true;
            return true;
        }
        return false;
    }

    public void remove() {
        grid.removeObject(target, gridPosX, gridPosY);
    }

    public GridWorld getGrid() {
        return grid;
    }

    @Override
    public void initialize() {

    }
}
