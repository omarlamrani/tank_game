package engine.handler;

import engine.Camera;
import engine.Maze;
import engine.components.EventComponent;
import engine.components.RenderComponent;
import engine.components.TickingInterface;
import engine.trackers.KeyboardTracker;
import engine.trackers.MouseTracker;
import engine.world.GridWorld;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import objects.GameObject;
import objects.RenderComponents.BackgroundRenderComponent;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

//This class handles the component system

public class Handler {

    public static final boolean DEBUG = false;
    public static final int TILE_SIZE = 80;
    private final ArrayList<TickingInterface> tickingComponents = new ArrayList<>();
    private final ArrayList<TickingInterface> tickingRemoval = new ArrayList<>();
    private final ArrayList<RenderComponent> renderRemoval = new ArrayList<>();
    private final CopyOnWriteArrayList<TickingInterface> tickingAdd = new CopyOnWriteArrayList<>();
    private final ArrayList<RenderComponent> renderAdd = new ArrayList<>();
    private GraphicsContext gc;
    public ArrayList<RenderComponent> renderComponents = new ArrayList<>();
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    private int trackingTankID = -1;
    private int offX = 0, offY = 0;
    private MouseTracker mouseTracker;
    private KeyboardTracker keyboardTracker;
    protected GridWorld gridWorld;

    public Handler(GraphicsContext gc, Scene scene) {
        this.gc = gc;
        this.setMouseTracker(new MouseTracker(gc.getCanvas()));
        this.setKeyboardTracker(new KeyboardTracker(scene));
        this.addRenderComponent(new BackgroundRenderComponent());
    }

    public Handler(GraphicsContext gc, Scene scene, int[][] grid) {
        this.gc = gc;
        this.setMouseTracker(new MouseTracker(gc.getCanvas()));
        this.setKeyboardTracker(new KeyboardTracker(scene));
        int size = TILE_SIZE;
        this.gridWorld = new GridWorld(this, grid[0].length, grid.length, size, 0, 0);
        this.addRenderComponent(new BackgroundRenderComponent());
    }

    //debug only
    public Handler(int dimX, int dimY) {
        this.gridWorld = new GridWorld(this, dimX, dimY, TILE_SIZE, 0, 0);
    }

    public void setTackingID(int ID) {
        trackingTankID = ID;
    }

    public void tickList() {
        tickingComponents.addAll(tickingAdd);

        //This operation is very expensive, but I couldn't get a buffer system to work how I wanted it to.
        tickingAdd.removeAll(tickingComponents);

        for (TickingInterface t : tickingComponents) {
            t.tick();
        }

        tickingComponents.removeAll(tickingRemoval);
        tickingRemoval.clear();
    }

    public void renderList() {
        for (RenderComponent r : renderAdd) {
            addSorted(r);
        }

        if (trackingTankID != -1) {
            for (RenderComponent r : renderComponents) {
                if (r.UID == trackingTankID) {
                    offX = (int) (gc.getCanvas().getWidth() / 2 - r.x.get());
                    offY = (int) (gc.getCanvas().getHeight() / 2 - r.y.get());
                }
            }
        }

        renderAdd.clear();
        //gc.clearRect(-1000, -1000, 3000, 3000);


        //Translation calculation
        Transform scale = new Scale((float) MouseTracker.scroll, (float) MouseTracker.scroll, gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2);

        Point2D screenOrigin = scale.transform(new Point2D(0, 0));
        Point2D offset = scale.transform(new Point2D(offX, offY));

        Transform translate = new Translate(offset.getX() - screenOrigin.getX(), offset.getY() - screenOrigin.getY());

        Transform preCorrection = translate.createConcatenation(scale);

        Point2D correctionMarkerTL = new Point2D(0, 0);
        Point2D correctionMarkerBR = new Point2D(getGridWorld().dimX * getGridWorld().size, getGridWorld().dimY * getGridWorld().size);

        correctionMarkerTL = preCorrection.transform(correctionMarkerTL);
        correctionMarkerBR = preCorrection.transform(correctionMarkerBR);

        double screenX = gc.getCanvas().getWidth();
        double screenY = gc.getCanvas().getHeight();

        double xCorrect = 0;
        if (correctionMarkerTL.getX() > 0) {
            xCorrect = -correctionMarkerTL.getX();
        } else if (correctionMarkerBR.getX() < screenX) {
            xCorrect = Math.min((screenX - correctionMarkerBR.getX()), -correctionMarkerTL.getX());
        }

        double yCorrect = 0;
        if (correctionMarkerTL.getY() > 0) {
            yCorrect = -correctionMarkerTL.getY();
            //System.out.println("O-Y on screen by " + correctionMarkerTL.getY());
        } else if (correctionMarkerBR.getY() < screenY) {
            //System.out.println("BR-Y on screen by " + (screenY-correctionMarkerBR.getY()));
            yCorrect = Math.min((screenY - correctionMarkerBR.getY()), -correctionMarkerTL.getY());
        }

        Transform correction = new Translate(xCorrect, yCorrect);

        Transform finalTranslate = correction.createConcatenation(translate);

        Camera.setTransform(finalTranslate.createConcatenation(scale));

        for (RenderComponent r : renderComponents) {
            r.render(gc, scale, finalTranslate);
        }

        renderComponents.removeAll(renderRemoval);
        renderRemoval.clear();
    }

    public ArrayList<RenderComponent> getRenderComponents() {
        return (ArrayList<RenderComponent>) renderComponents.clone();
    }

    public void addRenderComponent(RenderComponent component) {
        renderAdd.add(component);
    }

    private void addSorted(RenderComponent comp) {
        if (renderComponents.size() == 0) {
            renderComponents.add(comp);
        } else {
            for (int i = 0; i < renderComponents.size(); i++) {
                if (comp.z < renderComponents.get(i).z) {
                    renderComponents.add(i, comp);
                    return;
                }
            }
            renderComponents.add(comp);
        }
    }

    public void addTickingComponent(TickingInterface component) {
        tickingAdd.add(component);
    }

    public void addEventComponent(EventComponent<?> component) {
        component.registerEvent(gc.getCanvas());
    }

    public void removeRenderComponent(RenderComponent component) {
        renderRemoval.add(component);
    }

    public void removeTickingComponent(TickingInterface component) {
        tickingRemoval.add(component);
    }

    public void removeEventComponent(EventComponent<?> component) {
        component.unregister(gc.getCanvas());
    }

    public MouseTracker getMouseTracker() {
        return mouseTracker;
    }

    public void setMouseTracker(MouseTracker mouseTracker) {
        this.mouseTracker = mouseTracker;
    }

    public KeyboardTracker getKeyboardTracker() {
        return keyboardTracker;
    }

    public void setKeyboardTracker(KeyboardTracker keyboardTracker) {
        this.keyboardTracker = keyboardTracker;
    }

    public GridWorld getGridWorld() {
        return gridWorld;
    }

    public void setGridWorld(GridWorld gridWorld) {
        this.gridWorld = gridWorld;
    }
}
