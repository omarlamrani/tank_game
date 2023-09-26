package engine.components.controllers;

import engine.components.EventComponent;
import engine.components.TickingInterface;
import engine.trackers.MouseTracker;
import engine.wrappers.EventHandlerWrapper;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;

public class TurretMouseController extends EventComponent implements TickingInterface {

    MouseTracker mouse;
    TurretControls controls;

    public TurretMouseController(TurretControls controls, MouseTracker mouse) {
        super(MouseEvent.MOUSE_PRESSED);

        this.controls = controls;
        this.mouse = mouse;


        this.setEventHandler((EventHandlerWrapper) event -> controls.fire());
    }

    /**
     * @param target  Target angle
     * @param current Current angle
     * @param speed   Much to turn by
     * @return What turn to take to get closer to the target from the current angle
     */
    public static float getTurnFromTarget(float target, float current, float speed) {
        float move = target - current;
        move = move % 360;
        if (move < 0) move += 360;

        if (move == 0) {
            return 0;
        } else if (Math.abs(move) < speed) {
            return move;
        } else if (move >= 180) {
            return -speed;
        } else {
            return speed;
        }
    }

    @Override
    public void tick() {
        controls.turnTo(mouse.get());
    }

    @Override
    public void initialize() {

    }
}
