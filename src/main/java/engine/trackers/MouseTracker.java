package engine.trackers;

import engine.Camera;
import engine.Vec;
import javafx.scene.canvas.Canvas;

public class MouseTracker {

    public static final float MIN_SCROLL = 0.4F;
    public static float x = 0, y = 0;
    public static boolean pressed = false;
    public static double scroll = 1;

    public MouseTracker(Canvas canvas) {
        canvas.setOnMouseMoved(e -> {
            x = (float) e.getX();
            y = (float) e.getY();
        });

        canvas.setOnMousePressed(e -> pressed = true);
        canvas.setOnMouseReleased(e -> pressed = false);

        canvas.setOnScroll(e -> {
            scroll += e.getDeltaY() / 1000;
            //System.out.println(e.getDeltaY()/1000);
            if (scroll > 1) scroll = 1;
            else if (scroll < MIN_SCROLL) scroll = MIN_SCROLL;
        });
    }

    public Vec get() {
        //System.out.println("Mouse x, y: " + x + ", " + y + "  Offset: " + Handler.offX + ", " + Handler.offY);
        //return Vec.add(Vec.scalarMult(new Vec(x, y), (float) (1/scroll)), new Vec(Handler.offX, Handler.offY));
        //RenderComponent.screenTransform.
        return Camera.screenToGame(new Vec(x, y));
    }
}
