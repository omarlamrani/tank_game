package engine.components.controllers;

import engine.components.ComponentType;
import engine.components.TickingInterface;
import engine.trackers.KeyboardTracker;

public class TankKeyboardController implements TickingInterface {

    TankControls moveComp;
    KeyboardTracker keyboard;

    public TankKeyboardController(TankControls moveComp, KeyboardTracker keyboard) {
        this.moveComp = moveComp;
        this.keyboard = keyboard;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TICKING;
    }

    @Override
    public void tick() {
        if (keyboard.W) moveComp.moveForward();
        if (keyboard.A) moveComp.turnAntiClockwise();
        if (keyboard.S) moveComp.moveBackward();
        if (keyboard.D) moveComp.turnClockwise();
    }

    @Override
    public void initialize() {

    }
}
