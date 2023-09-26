package engine.world.AI;

import engine.Entity;
import engine.handler.Handler;
import engine.components.PositionComponent;
import engine.components.TickingComponent;
import engine.world.Node;

import java.util.ArrayList;

public class PathVis extends Entity {

    PathFinder finder;
    ArrayList<Float> coords;

    public PathVis(Handler handler, PathFinder finder) {
        super(handler);

        this.finder = finder;
        coords = new ArrayList<>();


        this.addComponent(new TickingComponent(this::tick));
        this.addComponent(new PathVisRenderComponent(new PositionComponent(0, 0, 0), coords));

        initComponents();
    }

    private void tick() {
        coords.clear();
        //System.out.println(path.size());
        for (Node n : finder.currentRoute) {
            //System.out.println(path.size());
            coords.add(n.pos.getX());
            coords.add(n.pos.getY());
        }
    }
}


