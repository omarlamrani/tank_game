package objects.RenderComponents;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import engine.wrappers.FloatWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthBarRenderComponent extends RenderComponent {

    public float healthPoints;

    public HealthBarRenderComponent(PositionComponent pos, float healthPoints) {
        super(new PositionComponent(pos.getWrappedX(), pos.getWrappedY(), new FloatWrapper(0F)), 1);
        this.healthPoints = healthPoints;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x.get() - 45, y.get() + 60, 110, 20);
        gc.setFill(Color.GREEN);
        gc.fillRect(x.get() - 40, y.get() + 65, healthPoints, 10);
        //V2 _> how much hp is displayed

    }
}
