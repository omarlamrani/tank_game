package objects.grid;

import engine.Entity;
import engine.handler.Handler;
import engine.components.FreeCollider;
import engine.components.PositionComponent;
import engine.components.RenderComponent;
import engine.world.GridWorld;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.RenderComponents.WallRenderComponent;

public class Wall extends Entity {

    public Wall(Handler handler, int x, int y, int type, int r, GridWorld grid) {
        super(handler);
        grid.addObject(this, x, y);

        //grid.removeNode(x, y);

        int width = (type == 2 ? 80 : 52);
        int height = (type == 2 ? 10 : 52);

        PositionComponent pos = new PositionComponent(grid.getCentre(x, y));
        pos.setR((float) r + 90);

        this.addComponent(new WallRenderComponent(this.addComponent(pos), 46, 46, type));
        this.addComponent(new FreeCollider(pos, width, height, handler.getGridWorld()));

        if (Handler.DEBUG) {
            this.addComponent(new RenderComponent(pos) {
                public void toRender(GraphicsContext gc) {
                    gc.setFill(Color.RED);
                    gc.fillRect(pos.getIntX() - width / 2, pos.getIntY() - height / 2, width, height);
                }
            });
        }

    }
}
