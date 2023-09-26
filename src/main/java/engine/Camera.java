package engine;

import javafx.geometry.Point2D;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Camera {
    private static Transform screenTransform = new Translate();

    public static Transform getTransform() {
        return screenTransform;
    }

    public static void setTransform(Transform transform) {
        screenTransform = transform;
    }

    public static Vec screenToGame(Vec screenPos) {
        Point2D p = new Point2D(screenPos.getX(), screenPos.getY());
        try {
            return new Vec(screenTransform.inverseTransform(p));
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vec gameToScreen(Vec gamePos) {
        Point2D p = new Point2D(gamePos.getX(), gamePos.getY());
        return new Vec(screenTransform.transform(p));
    }

}
