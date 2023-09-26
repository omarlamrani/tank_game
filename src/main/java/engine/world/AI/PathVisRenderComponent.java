package engine.world.AI;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PathVisRenderComponent extends RenderComponent {

    ArrayList<Float> path;

    public PathVisRenderComponent(PositionComponent pos, ArrayList<Float> coords) {
        super(pos);

        this.path = coords;


    }

    @Override
    public void toRender(GraphicsContext gc) {
        ArrayList<Float> temp = new ArrayList<Float>(path);

        if (temp == null) return;

        gc.save();
        gc.setLineWidth(5.0);
        gc.setFill(Color.ORANGE);
        for (int i = 0; i < temp.size() - 3; i += 2) {
            gc.strokeLine(temp.get(i), temp.get(i + 1), temp.get(i + 2), temp.get(i + 3));
            //System.out.println(path.get(i) + ", " + path.get(i+2));
        }
        gc.restore();
    }
}
