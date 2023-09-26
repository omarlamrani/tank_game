package engine.wrappers;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class ColourWrapper implements Serializable {

    private float r, g, b;
    private float a = 1;

    public ColourWrapper(Color col) {
        set(col);
    }

    public Color get() {
        return new Color(r, g, b, a);
    }

    public void set(Color col) {
        r = (float) col.getRed();
        g = (float) col.getGreen();
        b = (float) col.getBlue();
        a = (float) col.getOpacity();
    }

    public void set(float r, float g, float b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = (float) a;
    }

    public void setA(float a) {
        this.a = a;
    }
}
