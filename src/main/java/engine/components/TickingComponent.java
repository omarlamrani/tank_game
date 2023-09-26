package engine.components;

public class TickingComponent implements TickingInterface {

    private final Runnable tick;
    private boolean init = false;

    public TickingComponent(Runnable tick) {
        this.tick = tick;
    }

    public void tick() {
        if (init) {
            if (tick != null) tick.run();
            else toTick();
        }
    }

    public void toTick() {

    }

    public ComponentType getType() {
        return ComponentType.TICKING;
    }

    public void initialize() {
        init = true;
    }
}