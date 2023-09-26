package engine.components.controllers;

import engine.components.Component;
import engine.components.ComponentType;

public abstract class TankControls implements Component {

    /**
     * Turns the tank clockwise by its turn speed
     */
    public abstract void turnClockwise();

    /**
     * Turns the tank anticlockwise by its turn speed
     */
    public abstract void turnAntiClockwise();

    /**
     * Moves the tank forward by its move speed
     */
    public abstract boolean moveForward();

    /**
     * Moves the tank backward by its move speed
     */
    public abstract boolean moveBackward();

    @Override
    public ComponentType getType() {
        return null;
    }
}
