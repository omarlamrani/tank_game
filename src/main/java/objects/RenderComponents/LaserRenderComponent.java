package objects.RenderComponents;

import engine.Vec;
import engine.components.PositionComponent;
import engine.components.RenderComponent;
import engine.wrappers.ColourWrapper;
import javafx.scene.canvas.GraphicsContext;

public class LaserRenderComponent extends RenderComponent {
    private final ColourWrapper beamColour;
    public int life = 0;
    Vec start;
    Vec end;
    Vec dir;
    boolean flag = true;

    public LaserRenderComponent(Vec start, Vec end, ColourWrapper beamColour) {
        super(new PositionComponent(new Vec(0, 0)), 4);
        this.start = start;
        this.end = end;
        this.beamColour = beamColour;

        this.dir = Vec.subtract(end, start);

        //System.out.println("Start: " + start + "\nEnd: " + end + "\nDir: " + dir);

        dir.setMagnitude(1);
        //System.out.println("Dir adj: " + dir);

    }

    public void lifeTick() {
        if (life < 60) life++;
    }

    @Override
    public void toRender(GraphicsContext gc) {
        //gc.setFill(new Color(0.2,1 ,0.9, 0.5));
        gc.setFill(beamColour.get());
        Vec current = new Vec(start);
        Vec perp = new Vec(-dir.getY(), dir.getX());
        Float ang = dir.getAngle();
        //perp.setRotation(ang + 90);

        Vec drawLoc;
        int other = 1;

        while (Math.abs(Vec.subtract(end, current).getAngle() - ang) < 50) {
            drawLoc = Vec.add(current, Vec.scalarMult(perp, (float) life / 5 * other));
            gc.fillRect(drawLoc.getX() - 5, drawLoc.getY() - 5, 11 - life / 6, 11 - life / 6);
            current = Vec.add(current, dir);
            other = other * -1;
        }

        flag = false;

    }
}
