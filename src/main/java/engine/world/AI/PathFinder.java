package engine.world.AI;

import engine.components.GridPositionComponent;
import engine.world.GridWorld;
import engine.world.Node;
import objects.tank.Tank;

import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {
    private final HashMap<Integer, PathNode> explored;
    private final HashMap<Integer, PathNode> searchSpace;
    public ArrayList<Node> currentRoute;


    public PathFinder() {
        explored = new HashMap<>();
        searchSpace = new HashMap<>();
        currentRoute = new ArrayList<>();
    }


    public ArrayList<Node> tankRouteFind(Tank tank, Node destination) {
        GridPositionComponent gpc = tank.gridPosComp;
        GridWorld gridWorld = gpc.getGrid();

        gridWorld.addNode(gpc.gridPosX, gpc.gridPosY);
        gridWorld.nodeGraph[gpc.gridPosY][gpc.gridPosX].pos = tank.pos;

        fullRouteFind(gridWorld.nodeGraph[gpc.gridPosY][gpc.gridPosX], destination);

        gridWorld.removeNode(gpc.gridPosX, gpc.gridPosY);

        return currentRoute;
    }

    public ArrayList<Node> tankToTank(Tank tank, Tank dest) {
        GridPositionComponent gpc = dest.gridPosComp;
        GridWorld gridWorld = gpc.getGrid();

        gridWorld.addNode(gpc.gridPosX, gpc.gridPosY);
        gridWorld.nodeGraph[gpc.gridPosY][gpc.gridPosX].pos = dest.pos;

        tankRouteFind(tank, gridWorld.nodeGraph[gpc.gridPosY][gpc.gridPosX]);

        gridWorld.removeNode(gpc.gridPosX, gpc.gridPosY);

        return currentRoute;
    }

    public ArrayList<Node> fullRouteFind(Node origin, Node destination) {
        cleanup();
        currentRoute.clear();

        if (origin == null || destination == null) return null;

        searchSpace.put(origin.ID, new PathNode(origin, destination));
        findPath(destination);

        if (!explored.containsKey(destination.ID)) return null;

        currentRoute = getPathFromNodes(origin, destination);

        return currentRoute;
    }


    private void findPath(Node goal) {
        PathNode first = searchSpace.values().stream().sorted().findFirst().orElse(null);
        if (first == null || goal == null || explore(first, goal)) return;
        findPath(goal);
    }

    private ArrayList<Node> getPathFromNodes(Node origin, Node destination) {
        if (destination == origin) {
            ArrayList<Node> output = new ArrayList<>();
            output.add(destination);
            return output;
        }

        PathNode best = null;
        PathNode destPN = explored.get(destination.ID);
        float lookingForG = destPN.g - destPN.edgeDist;
        for (Node n : destination.neighbors) {
            PathNode testing = explored.get(n.ID);
            if (testing != null) {
                if (Math.abs(testing.g - lookingForG) < 0.01) {
                    best = testing;
                    break;
                }
            }
        }
        ArrayList<Node> output = new ArrayList<>();

        //System.out.println("Adding node to path: " + destination.x + ", " + destination.y);
        output.add(destination);

        if (best != null) {
            output.addAll(getPathFromNodes(origin, best.getNode()));
        }

        //System.out.println("Ending");
        return output;
    }

    /**
     * Explores a node.
     *
     * @param node The node to explore
     * @return If the node it explored is the target node.
     */
    private boolean explore(PathNode node, Node goal) {
        //System.out.println("Exploring: " + node.x + ", " + node.y + " " + searchSpace.size());
        if (node.ID == goal.ID) {
            explored.put(node.ID, node);
            searchSpace.remove(node.ID);
            return true;
        } else {
            //System.out.println(node.getNode().neighbors.size());
            for (Node n : node.getNode().neighbors) {
                //System.out.println(n.x + ", " + n.y);
                if (explored.get(n.ID) == null && searchSpace.get(n.ID) == null) {
                    searchSpace.put(n.ID, new PathNode(n, node, goal));
                }
            }
            explored.put(node.ID, node);
            searchSpace.remove(node.ID);
        }
        return false;
    }

    public void cleanup() {
        explored.forEach((k, v) -> v.remove());
        explored.clear();
        searchSpace.forEach((k, v) -> v.remove());
        searchSpace.clear();
    }
}
