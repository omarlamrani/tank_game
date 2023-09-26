package engine.components;

import engine.wrappers.EventHandlerWrapper;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;

import java.io.Serializable;

public class EventComponent<S extends Event> implements Component, Serializable {
    EventType<S> eventType;
    EventHandlerWrapper<? super S> eventHandler;

    public EventComponent(EventType<S> eventType, EventHandlerWrapper<? super S> eventHandler) {
        this.eventType = eventType;
        this.eventHandler = eventHandler;

    }

    public EventComponent(EventType<S> eventType) {
        this.eventType = eventType;
    }

    /**
     * Adds this event to a canvas
     *
     * @param canvas The canvas to register this event to
     */
    public void registerEvent(Canvas canvas) {
        if (eventType != null && eventHandler != null) {
            canvas.addEventHandler(eventType, eventHandler);
        }
    }

    /**
     * Removes this event from a canvas
     *
     * @param canvas The canvas to remove this event from
     */
    public void unregister(Canvas canvas) {
        if (eventType != null && eventHandler != null) {
            canvas.removeEventHandler(eventType, eventHandler);
        }
    }

    /**
     * Sets the event handler for this event
     *
     * @param eventHandler the event handler to add to this EventComponent
     */
    public void setEventHandler(EventHandlerWrapper<? super S> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public ComponentType getType() {
        return ComponentType.EVENT;
    }
}
