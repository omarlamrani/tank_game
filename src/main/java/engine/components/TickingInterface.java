package engine.components;

public interface TickingInterface extends Component {

    /**
     * Functionality of the component to be called 60 times every second.
     */
    void tick();

    /**
     * Abstract method that is called during component initialization.
     * Stops the component running when first created unless intended.
     */
    void initialize();
}
