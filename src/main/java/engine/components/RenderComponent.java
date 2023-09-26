package engine.components;

import engine.wrappers.FloatWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class RenderComponent implements Component {

    public static transient int counter = 0;
    public final int UID;
    public FloatWrapper x, y, r;
    public int z;
    protected boolean transformable = true;

    public RenderComponent(PositionComponent pos) {
        UID = counter;
        counter++;

        this.x = pos.x;
        this.y = pos.y;
        this.r = pos.r;
        this.z = 0;
    }

    public RenderComponent(PositionComponent pos, int z) {
        UID = counter;
        counter++;

        this.x = pos.x;
        this.y = pos.y;
        this.r = pos.r;
        this.z = z;
    }

    public void toRender(GraphicsContext gc) {
        //if you don't override toRender you get an easily recognisable missing texture
        gc.setFill(Color.HOTPINK);
        gc.fillRect(x.get() - 10, y.get() - 10, 20, 20);
        gc.fillRect(x.get() + 10, y.get() + 10, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillRect(x.get() + 10, y.get() - 10, 20, 20);
        gc.fillRect(x.get() - 10, y.get() + 10, 20, 20);
    }

    public void render(GraphicsContext gc, Transform scale, Transform translate) {
        if (transformable) {
            gc.save();

            Transform rotate = new Rotate((int) r.get(), (int) x.get(), (int) y.get());

            Transform mid = scale.createConcatenation(rotate);

            gc.setTransform((Affine) translate.createConcatenation(mid));

            toRender(gc);
            gc.restore();
        } else {
            toRender(gc);
        }
    }

    public ComponentType getType() {
        return ComponentType.RENDERING;
    }

}
