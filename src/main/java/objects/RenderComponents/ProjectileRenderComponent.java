package objects.RenderComponents;

import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ProjectileRenderComponent extends RenderComponent {

    static Image bullet = new Image("file:src/main/resources/playerBullet.png");
    static int[] falseRand = {8, 3, 7, 1, 5, 4, 9, 6, 2, 0};
    private int life = 0;

    public ProjectileRenderComponent(PositionComponent pos) {
        super(pos);
    }

    @Override
    public void toRender(GraphicsContext gc) {
        gc.save();
        Color col = new Color(1.0, 0.670, 0.2, 0.5);

        gc.setFill(col);

        if (life > 12) {
            for (int i = 0; i < 10; i++) {
                int localtime = (life + falseRand[i] * 9) % 60;
                gc.fillRect(x.get() - 8 + i, y.get() + 5 + localtime, 10 - (localtime / 6), 10 - (localtime / 6));
            }
        }
        gc.restore();
        gc.drawImage(bullet, (int) x.get() - 8.5, (int) y.get() - 5.5);
    }

    public void lifeTick() {
        life += 2;
    }
}
