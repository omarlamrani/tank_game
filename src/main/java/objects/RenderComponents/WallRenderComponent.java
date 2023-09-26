package objects.RenderComponents;

import engine.handler.Handler;
import engine.components.ImageRenderComponent;
import engine.components.PositionComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class WallRenderComponent extends ImageRenderComponent {
    private static final Image texture = new Image("file:src/main/resources/towers_walls_blank_full.png");
    static int IID = -1;
    //private static ImageWrapper prerender = null;
    int xSize;
    int ySize;
    int type;

    public WallRenderComponent(PositionComponent pos, int xSize, int ySize, int type) {
        super(pos);
        this.xSize = xSize;
        this.ySize = ySize;
        this.type = type;

        if (IID == -1) {
            IID = IIDCounter;
            IIDCounter++;
        }
    }

    public void toRender(GraphicsContext gc) {
        gc.drawImage(fetchImage().image, 128 * type, (type == 2 ? 512 : 0), 128, 128, x.get() - Handler.TILE_SIZE / 2, y.get() - Handler.TILE_SIZE / 2, Handler.TILE_SIZE, Handler.TILE_SIZE);
    }

    @Override
    public int getIID() {
        return IID;
    }

    @Override
    public Effect getEffect() {
        return new DropShadow(10.0F, Color.BLACK);
    }

    @Override
    public Image getImage() {
        return texture;
    }
}
