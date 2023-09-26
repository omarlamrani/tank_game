package engine.world;

import engine.Entity;
import engine.Vec;
import engine.components.PositionComponent;
import engine.components.RenderComponent;

import java.util.ArrayList;

public class Node extends Entity {
    public static int counter = 0;
    public int x;
    public int y;
    public int ID;
    public ArrayList<Node> neighbors;
    public ArrayList<Node> axisNeighbors;
    public PositionComponent pos;
    public GridWorld grid;

    public Node(GridWorld grid, Vec pos) {
        super(grid.handler);
        this.grid = grid;
        this.ID = counter;
        counter++;
        this.x = grid.getGridPosX(pos.getX());
        this.y = grid.getGridPosY(pos.getY());

        //grid.nodeGraph[y][x] = this;
        this.pos = new PositionComponent(pos);
        neighbors = new ArrayList<>();
        axisNeighbors = new ArrayList<>();
        //this.addComponent(new NodeRenderComponent(this.pos, neighbors));
    }

    public void resetNeighbors() {
        neighbors.clear();
        axisNeighbors.clear();
    }


    public void addNeighbor(Node node) {
        neighbors.add(node);
        if (node.x == this.x || node.y == this.y) {
            axisNeighbors.add(node);
        }
    }

    public void addNeighbors(ArrayList<Node> toAdd) {
        for (Node n : toAdd) {
            addNeighbor(n);
        }
    }

    public void removeNode(Node node) {
        neighbors.remove(node);
        axisNeighbors.remove(node);
    }


    public void dereference() {
        ArrayList<Node> test = new ArrayList<>(axisNeighbors);
        ArrayList<Node> all = new ArrayList<>(neighbors);

        for (Node n : all) {
            n.removeNode(this);
        }

        for (Node n : test) {
            for (Node m : test) {
                n.removeNode(m);
            }
        }
        resetNeighbors();

        this.removeComponent(getComponent(RenderComponent.class));

        this.remove();
    }
}

