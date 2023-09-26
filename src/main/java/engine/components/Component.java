package engine.components;

import java.io.Serializable;

public interface Component extends Serializable {

    /**
     * If the component needs special behaviour types it returns the type.
     *
     * @return Component type
     */
    ComponentType getType();

}
