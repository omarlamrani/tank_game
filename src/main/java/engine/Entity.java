package engine;

import engine.components.*;
import engine.handler.Handler;

import java.io.Serializable;
import java.util.LinkedList;

public class Entity implements Serializable {

    private final transient LinkedList<Component> components = new LinkedList<>();
    public transient Handler handler;

    public Entity(Handler handler) {
        this.handler = handler;
    }

    public <T extends Component> T getComponent(Class<T> type) {
        for (Component c : components) {
            if (type.isInstance(c)) {
                return (T) c;
            }
        }
        return null;
    }

    public LinkedList<Component> listComponents() {
        return components;
    }

    /**
     * Adds a component to this entity
     *
     * @param component Component to add
     * @param <T>       Type of component
     * @return returns the component added.
     */
    public <T extends Component> T addComponent(T component) {

        if (component instanceof TickingInterface) handler.addTickingComponent((TickingInterface) component);
        if (component instanceof RenderComponent && component.getType() == ComponentType.RENDERING)
            handler.addRenderComponent((RenderComponent) component);
        if (component instanceof EventComponent) handler.addEventComponent((EventComponent<?>) component);

        /*
         switch (component.getType()) {
         case TICKING:
         handler.addTickingComponent((TickingInterface) component);
         break;

         case RENDERING:
         handler.addRenderComponent((RenderComponent) component);
         break;

         case EVENT:
         handler.addEventComponent((EventComponent<?>) component);
         }
         */

        components.add(component);
        return component;
    }

    public <T extends Component> void removeComponent(T component) {
        if (component instanceof TickingInterface) handler.removeTickingComponent((TickingInterface) component);
        if (component instanceof RenderComponent) handler.removeRenderComponent((RenderComponent) component);
        if (component instanceof EventComponent) handler.removeEventComponent((EventComponent<?>) component);
        components.remove(component);
    }

    public void remove() {
        LinkedList<Component> removalList;
        removalList = new LinkedList<>(components);


        for (Component c : removalList) {
            this.removeComponent(c);
        }
    }

    public void initComponents() {
        for (Component c : components) {
            if (c instanceof TickingInterface) {
                ((TickingInterface) c).initialize();
            }
            //this.addComponent(c);
        }
    }

    public Handler getHandler() {
        return handler;
    }

}
