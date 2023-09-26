package engine.wrappers;

import java.io.Serializable;


public class IntWrapper implements Serializable {
    public int value;

    public IntWrapper(int n) {
        this.value = n;
    }

    public float get() {
        return value;
    }

    public void set(int n) {
        value = n;
    }


}
