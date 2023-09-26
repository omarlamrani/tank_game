package objects.RenderComponents;

import engine.components.PositionComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CannonTurretRenderComponent extends TurretRenderComponent {

    public static Image cannonTurret = new Image("file:src/main/resources/turrets/cannonturret.png");


    public CannonTurretRenderComponent(PositionComponent pos, float hue) {
        super(pos, hue);
    }

    @Override
    public void toRender(GraphicsContext gc) {
        gc.drawImage(fetchImage().image, frame * 128, 0, 128, 128, (int) this.x.get() - 64, (int) this.y.get() - 64, 128, 128);
    }

    @Override
    public Image getImage() {
        return cannonTurret;
    }
}

