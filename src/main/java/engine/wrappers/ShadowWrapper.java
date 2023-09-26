package engine.wrappers;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class ShadowWrapper extends DropShadow implements Serializable {
    public ShadowWrapper(Float f, Color c) {
        super(f, c);
    }
}
