package objects.RenderComponents;

import engine.components.ImageRenderComponent;
import engine.components.PositionComponent;
import engine.wrappers.ShadowWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class TurretRenderComponent extends ImageRenderComponent {

    public static Image turret = new Image("file:src/main/resources/turrets/turret.png");

    public static Image blast = new Image("file:src/main/resources/blast.png");
    static ShadowWrapper effect = new ShadowWrapper(10.F, Color.BLACK);
    private final float hue;
    private final transient boolean fastRender = true;
    private final int IID;
    public int frame = 0;

    public TurretRenderComponent(PositionComponent pos, float hue) {
        super(pos);

        IID = IIDCounter;
        IIDCounter++;

        this.hue = hue;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        gc.drawImage(blast, frame * 128, 0, 128, 128, (int) this.x.get() - 64, (int) this.y.get() - 64, 128, 128);
        gc.drawImage(fetchImage().image, frame * 128, 0, 128, 128, (int) this.x.get() - 64, (int) this.y.get() - 64, 128, 128);
    }

    @Override
    public int getIID() {
        return IID;
    }

    @Override
    public Effect getEffect() {
        effect.setInput(new ColorAdjust(hue, 0, 0, 0));
        return effect;
    }

    @Override
    public Image getImage() {
        return turret;
    }
}
