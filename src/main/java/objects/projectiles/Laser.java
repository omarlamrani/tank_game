package objects.projectiles;

import engine.Entity;
import engine.handler.Handler;
import engine.Vec;
import engine.components.TickingComponent;
import engine.wrappers.ColourWrapper;
import javafx.scene.paint.Color;
import objects.RenderComponents.LaserRenderComponent;

public class Laser extends Entity {

    LaserRenderComponent beam;

    public Laser(Handler handler, Vec startPos, Vec endPos, Color beamColour) {
        super(handler);
        ColourWrapper col = new ColourWrapper(beamColour);
        col.setA(0.5F);
        this.beam = this.addComponent(new LaserRenderComponent(startPos, endPos, col));
        this.addComponent(new TickingComponent(this::tick));

        this.initComponents();
    }

    public void tick() {
        beam.lifeTick();

        if (beam.life >= 60) {
            this.remove();
        }
    }
}
