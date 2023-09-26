package engine.components;

import engine.wrappers.ImageWrapper;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import objects.GameObject;

import java.util.Hashtable;

public abstract class ImageRenderComponent extends RenderComponent {
    private static transient final Hashtable<Integer, ImageWrapper> imageCache = new Hashtable<>();
    public static int IIDCounter = 0;

    public ImageRenderComponent(PositionComponent pos) {
        super(pos);
    }

    public ImageWrapper fetchImage() {
        ImageWrapper fetch = imageCache.get(getIID());
        if (fetch == null) {
            ImageWrapper rendered;
            rendered = GameObject.prerender(getImage(), getEffect());
            imageCache.put(getIID(), rendered);
            return rendered;
        } else {
            return fetch;
        }
    }

    public abstract int getIID();

    public abstract Effect getEffect();

    public abstract Image getImage();

}
