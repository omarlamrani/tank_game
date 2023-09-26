package engine.wrappers;

import java.io.Serializable;

public class FloatWrapper implements Serializable {
    private float value;

    public FloatWrapper(Float n) {
        this.value = n;
    }

    public float get() {
        return value;
    }

    public void set(float n) {
        value = n;
    }


}
