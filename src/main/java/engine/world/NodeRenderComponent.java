package engine.world;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class NodeRenderComponent extends RenderComponent {

    transient final static int RADIUS = 10;
    transient ArrayList<Node> neighbors;
    transient String mode;

    public NodeRenderComponent(PositionComponent pos, ArrayList<Node> neighbors) {
        super(pos, 90);
        this.neighbors = neighbors;
        mode = "node";
    }

    public void updateMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        if (neighbors == null) return;
        switch (mode) {
            case "node":
                gc.setFill(Color.HOTPINK);

                gc.fillOval(x.get() - RADIUS / 2, y.get() - RADIUS / 2, RADIUS, RADIUS);

                for (Node n : (ArrayList<Node>) neighbors.clone()) {
                    int ix = (int) x.get(), iy = (int) y.get(), dx = n.pos.getIntX(), dy = n.pos.getIntY();

                    gc.strokeLine(ix, iy, dx, dy);

                    gc.setFill(Color.BLACK);
                    int px = (9 * dx + ix) / 10;
                    int py = (9 * dy + iy) / 10;

                    gc.fillOval(px - 2, py - 2, 4, 4);
                }
                break;
            case "searchSpace":
                gc.setFill(new Color(1, 0, 0, 0.5));
                gc.fillOval(x.get() - RADIUS, y.get() - RADIUS, RADIUS * 2, RADIUS * 2);
                break;

            case "inPath":
                break;
        }
    }
}
