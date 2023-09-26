package engine.components.controllers;

import engine.Vec;
import engine.components.Component;
import engine.components.ComponentType;

public interface TurretControls extends Component {

    /**
     * Turns the turret toward a target vector
     *
     * @param t Target direction for the turret
     * @return How far the turret turned.
     */
    float turnTo(Vec t);

    /**
     * Fires the gun of the Turret
     */
    void fire();

    @Override
    default ComponentType getType() {
        return null;
    }
}
