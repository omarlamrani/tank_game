package objects.RenderComponents;

import engine.components.ImageRenderComponent;
import engine.components.PositionComponent;
import engine.wrappers.ShadowWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TankRenderComponent extends ImageRenderComponent {

    static Image SPRITE = new Image("file:src/main/resources/body_tracks.png");
    static ShadowWrapper effect = new ShadowWrapper(10.F, Color.BLACK);
    private final float hue;
    private final int IID;

    public TankRenderComponent(PositionComponent pos, float hue) {
        super(pos);

        this.IID = IIDCounter;
        IIDCounter++;

        this.hue = hue;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        //gc.setFill(new Color(1, 0, 0, 0.5));
        //int width = 70;
        //gc.fillRect((int) x.get() - width / 2, (int) y.get() - 40, width, 80);
        gc.drawImage(fetchImage().image, (int) x.get() - 64, (int) y.get() - 64, 128, 128);

        //if (this.IID == 3) System.out.println("Hello from " + getX() + ", " + y.get());
    }

    @Override
    public int getIID() {
        return IID;
    }

    public float getX() {
        return x.get();
    }

    @Override
    public Effect getEffect() {
        effect.setInput(new ColorAdjust(hue, 0, 0, 0));
        return effect;
    }

    @Override
    public Image getImage() {
        return SPRITE;
    }
}