package objects.minimap;

import engine.Entity;
import engine.handler.Handler;
import engine.components.PositionComponent;
import engine.components.TickingComponent;
import engine.world.GridWorld;
import objects.grid.Wall;
import objects.tank.Tank;

import java.util.ArrayList;

public class Minimap extends Entity {
    GridWorld grid;
    MinimapRenderComponent renderer;

    public Minimap(Handler handler) {
        super(handler);

        this.grid = handler.getGridWorld();
        this.renderer = this.addComponent(new MinimapRenderComponent(new PositionComponent(50, 500, 0), 99));

        this.addComponent(new TickingComponent(this::tick));

        this.initComponents();
    }

    public void tick() {
        int[][] map = new int[grid.dimY][grid.dimX];
        for (int y = 0; y < grid.dimY; y++) {
            for (int x = 0; x < grid.dimX; x++) {
                ArrayList<Entity> cell = grid.getObjectList(x, y);
                map[y][x] = 0;
                for (Entity e : cell) {
                    if (e instanceof Tank) {
                        map[y][x] = (int) (((Tank) e).hue * 128000) + 2;
                        //System.out.println(((Tank) e).hue + " " + map[y][x]);
                        break;
                    } else if (e instanceof Wall) map[y][x] = 1;
                }
            }
        }

        renderer.setMap(map);
    }

}
