package engine.wrappers;

import javafx.event.Event;
import javafx.event.EventHandler;

import java.io.Serializable;
import java.util.EventListener;

@FunctionalInterface
public interface EventHandlerWrapper<T extends Event> extends EventListener, Serializable, EventHandler<T> {
    void handle(T var1);
}
