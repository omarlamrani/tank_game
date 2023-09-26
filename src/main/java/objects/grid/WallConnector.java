package objects.grid;

import engine.Entity;
import engine.handler.Handler;
import engine.components.FreeCollider;
import engine.components.PositionComponent;
import engine.components.RenderComponent;
import engine.world.GridWorld;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WallConnector extends Entity {

    public WallConnector(Handler handler, int x, int y, boolean horizontal, GridWorld grid) {
        super(handler);
        PositionComponent pos = new PositionComponent(grid.getCentre(x, y));

        int width = 34;
        int height = 10;

        grid.addObject(this, x, y);
        //grid.removeNode(x, y);
        if (horizontal) {
            pos.setR(90F);
            pos.setY(pos.getY() + 40);
            //this.addComponent(new WallConnectorRenderComponent(this.addComponent(pos), 10, 34, horizontal));
            //this.addComponent(new FreeCollider(pos, 10, 34, handler.gridWorld));
        } else {
            pos.setR(0F);
            pos.setX(pos.getX() + 40);
            //this.addComponent(new WallConnectorRenderComponent(this.addComponent(pos), 34, 10, horizontal));
            //this.addComponent(new FreeCollider(pos, 34, 10, handler.gridWorld));
        }

        //grid.addObject(this, grid.getGridPosX(pos.getIntX()), grid.getGridPosY(pos.getIntY()));


        FreeCollider col = this.addComponent(new FreeCollider(pos, width, height, handler.getGridWorld()));
        //col.debugColor = Color.MEDIUMPURPLE;

        if (Handler.DEBUG) {
            this.addComponent(new RenderComponent(pos, 100) {
                public void toRender(GraphicsContext gc) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(pos.getIntX() - width / 2, pos.getIntY() - height / 2, width, height);
                }
            });
        }
    }


}
