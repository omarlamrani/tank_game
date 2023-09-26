package objects.RenderComponents;

import engine.ImageSheet;
import engine.components.PositionComponent;
import engine.components.RenderComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.File;

public class BackgroundRenderComponent extends RenderComponent {
    private static final Image ground = ImageSheet.getSubImage(new File("src/main/resources/terrain.png"), 0, 384, 128, 128);


    public BackgroundRenderComponent() {
        super(new PositionComponent(0, 0, 0));

    }

    public void toRender(GraphicsContext gc) {
        gc.setFill(new ImagePattern(ground, 0, 0, 128, 128, false));
        gc.fillRect(0, 0, 6000, 6000);
    }
}
