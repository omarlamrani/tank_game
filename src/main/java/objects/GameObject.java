package objects;

import engine.Entity;
import engine.handler.Handler;
import engine.Vec;
import engine.components.PositionComponent;
import engine.components.TickingComponent;
import engine.wrappers.ImageWrapper;
import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class GameObject extends Entity {
    public PositionComponent pos;
    //public VelocityComponent vel;

    public GameObject(Handler handler, Vec pos) {
        super(handler);
        init(pos.getX(), pos.getY(), 0);
    }

    public GameObject(Handler handler, Vec pos, float r) {
        super(handler);
        init(pos.getX(), pos.getY(), r);
    }

    /**
     * A method to pre-apply effects to images.
     *
     * @param in     The image to process
     * @param effect The effect to apply
     * @return The input image with effect applied
     */
    public static ImageWrapper prerender(Image in, Effect effect) {
        final ImageWrapper img = new ImageWrapper(in);
        //final boolean[] waiting = {true};

        Canvas renderCanvas = new Canvas(in.getWidth(), in.getHeight());
        GraphicsContext gc = renderCanvas.getGraphicsContext2D();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        gc.setEffect(effect);
        gc.drawImage(in, 0, 0);

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                img.image = renderCanvas.snapshot(parameters, new WritableImage((int) in.getWidth(), (int) in.getHeight()));
                System.out.println("Done");
                //waiting[0] = false;
            }
        });

        //img = renderCanvas.snapshot(parameters, new WritableImage((int) in.getWidth(), (int) in.getHeight()));

        return img;
    }

    /**
     * #
     *
     * @param x The X-Coordinate of the GameObject
     * @param y The Y-Coordinate of the GameObject
     * @param r The Rotation of the GameObject
     */
    private void init(float x, float y, float r) {
        pos = this.addComponent(new PositionComponent(x, y, r));                               //This one is named because it is useful to be able to reference by obj.pos.getX

        this.addComponent(new TickingComponent(this::tick));

        handler.gameObjects.add(this);
    }

    /**
     * A method called every tick, gameObject logic is run here.
     */
    public abstract void tick();

}
