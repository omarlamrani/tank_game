package engine.world.AI;

import engine.world.Node;

public class PathNode extends Node implements Comparable<PathNode> {

    final static float BIAS = 1.1F;
    private final Node under;
    public float g;
    public float f;
    public float edgeDist;
    float h;

    public PathNode(Node toCopy, Node target) {
        super(toCopy.grid, toCopy.pos.get());
        under = toCopy;
        if (target != null) init(toCopy, target, 0);
    }

    public PathNode(Node toCopy, PathNode current, Node target) {
        super(toCopy.grid, toCopy.pos.get());
        under = toCopy;
        if (target != null) init(current, target, current.g);
    }

    private void init(Node current, Node target, float pg) {
        //This probably means I should change the inheritance around.
        ID = getNode().ID;
        counter--;

        edgeDist = (float) (Math.pow(current.x - under.x, 2) + Math.pow(current.y - under.y, 2));
        g = edgeDist + pg;
        h = (float) (Math.pow(target.x - under.x, 2) + Math.pow(target.y - under.y, 2));
        f = g + BIAS * h;

        //this.getComponent(NodeRenderComponent.class).updateMode("searchSpace");
    }

    @Override
    public int compareTo(PathNode o) {
        return Float.compare(f, o.f);
    }

    public Node getNode() {
        return under;
    }
}
