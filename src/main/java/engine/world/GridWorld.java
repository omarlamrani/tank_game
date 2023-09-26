package engine.world;

import engine.Entity;
import engine.handler.Handler;
import engine.Vec;
import engine.components.FreeCollider;
import objects.grid.Wall;
import objects.grid.WorldBorder;
import objects.tank.Tank;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class GridWorld {

    public static final Set<Class> COLLIDABLE = Set.of(Tank.class, Wall.class, WorldBorder.class);
    public
    final int originX, originY, size, dimX, dimY;
    /**
     * Grid will be a simplified version of the game world that can be used for ai and others
     */
    private final ArrayList<Entity>[][] grid;
    public Node[][] nodeGraph;
    public Handler handler;

    /**
     * @param dimX    How many grid squares on the X axis
     * @param dimY    How many grid squares on the Y axis
     * @param size    How many pixels should each square be
     * @param originX Top left pixel of the grid
     * @param originY Top left pixel of the grid
     */
    public GridWorld(Handler handler, int dimX, int dimY, int size, int originX, int originY) {
        this.handler = handler;
        grid = new ArrayList[dimY][dimX];

        this.dimX = dimX;
        this.dimY = dimY;

        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        this.size = size;
        this.originX = originX;
        this.originY = originY;

        nodeGraph = new Node[dimY][dimX];

        buildNodeGraph(dimX, dimY);
    }

    public void updatePos(Entity target, int oldX, int oldY, int newX, int newY) {
        if (oldX >= 0 && oldX < dimX && oldY >= 0 && oldY < dimY) {
            removeObject(target, oldX, oldY);
        }


        if (newX >= 0 && newX < dimX && newY >= 0 && newY < dimY) {
            addObject(target, newX, newY);
        }
    }

    public void addObject(Entity object, int x, int y) {
        getObjectList(x, y).add(object);
        if (COLLIDABLE.contains(object.getClass()) && nodeGraph[y][x] != null) {
            removeNode(x, y);
        }
    }

    public void removeObject(Entity object, int x, int y) {
        getObjectList(x, y).remove(object);
        if (COLLIDABLE.contains(object.getClass()) && nodeGraph[y][x] == null) {
            addNode(x, y);
        }
    }

    public int getGridPosX(float x) {
        return ((int) x - originX) / size;
    }

    public int getGridPosY(float y) {
        return ((int) y - originY) / size;
    }

    public Vec getSpawn() {
        Random rand = new Random(System.currentTimeMillis());
        do {
            int x = rand.nextInt(grid[0].length - 2) + 1;
            int y = rand.nextInt(grid.length - 2) + 1;

            if (grid[y][x].isEmpty()) return getCentre(x, y);
        }
        while (true);
    }

    public ArrayList<Entity>[][] getGrid() {
        return grid;
    }

    public Node getNode(int x, int y) {
        return nodeGraph[y][x];
    }

    public ArrayList<Entity> getObjectList(int x, int y) {
        if (x >= 0 && x < dimX && y >= 0 && y < dimY) return grid[y][x];
        else {
            System.out.println("Invalid grid position requested: " + x + " " + y);
            return new ArrayList<>();
        }
    }

    public ArrayList<Entity> getNearbyCollidables(int x, int y) {
        ArrayList<Entity> output = new ArrayList<>();
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if (i >= 0 && i < dimY && j >= 0 && j < dimX) {
                    output.addAll(getCollidables(j, i));
                }
            }
        }
        return output;
    }

    private ArrayList<Entity> getCollidables(int x, int y) {
        ArrayList<Entity> output = new ArrayList<>();
        for (Entity e : getObjectList(x, y)) {
            if (e.getComponent(FreeCollider.class) != null) {
                output.add(e);
            }
        }
        return output;
    }

    public void updateNode(Node node) {
        node.resetNeighbors();
        node.addNeighbors(findNeighbors(node.x, node.y));
        for (Node n : node.neighbors) {
            n.resetNeighbors();
            n.addNeighbors(findNeighbors(n.x, n.y));
        }
    }


    public Node addNode(int x, int y) {
        if (nodeGraph[y][x] == null) {
            nodeGraph[y][x] = new Node(this, getCentre(x, y));
            updateNode(nodeGraph[y][x]);
        } else {
            System.out.println("Tried to set a non null node in the graph.");
        }
        return null;
    }

    /**
     * Removes a node from the nodegraph and updates any references to that node
     *
     * @param x x coord
     * @param y y coord
     */
    public void removeNode(int x, int y) {
        if (nodeGraph[y][x] == null) {
            System.out.println("Tried to set a null node to null");
        } else {
            ArrayList<Node> temp = nodeGraph[y][x].neighbors;
            nodeGraph[y][x].dereference();
            nodeGraph[y][x] = null;
            for (Node n : temp) {
                n.resetNeighbors();
                n.addNeighbors(findNeighbors(n.x, n.y));
            }
        }
    }

    /**
     * Outputs a text representation of this grid
     */
    public void printGrid() {
        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                char c = ' ';
                if (!grid[i][j].isEmpty()) c = 'x';

                System.out.print("[" + c + "]");
            }
            System.out.println();
        }
    }


    /**
     * Returns the upper left pixel position of the specified grid coordinate
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The pixel position of the upper left corner as a Vec
     */
    public Vec getCorner(int x, int y) {
        return new Vec(originX + (size * x), originY + (size * y));
    }


    /**
     * Returns the centre pixel position of the specified grid coordinate
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The pixel position of the centre as a Vec
     */
    public Vec getCentre(int x, int y) {
        return new Vec(originX + (size * x) + size / 2, originY + (size * y) + size / 2);
    }

    /**
     * Contructs a graph of connected nodes that represent paths that can be taken by the AI.
     *
     * @param x x dimension of the graph
     * @param y y dimension of the graph
     * @return a 2d array of nodes in the same order as the grid
     */
    private Node[][] buildNodeGraph(int x, int y) {
        Node[][] out = new Node[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                nodeGraph[i][j] = new Node(this, getCentre(j, i));
            }
        }

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                for (Node n : findNeighbors(j, i)) {
                    nodeGraph[i][j].addNeighbor(n);
                }
            }
        }

        return out;
    }

    /**
     * Finds neighbor nodes based on nodes that can be moved to directly by a tank.
     *
     * @param x x grid coordinate
     * @param y y grid coordinate
     * @return a list of nodes that the node x y connects to
     */
    private ArrayList<Node> findNeighbors(int x, int y) {
        ArrayList<Node> output = new ArrayList<>();

        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                int pY = y + k;
                int pX = x + l;

                if (pX >= 0 && pX < dimX && pY >= 0 && pY < dimY && !(k == 0 && l == 0)) {
                    if (nodeGraph[pY][pX] != null) {
                        //if axis aligned just add.
                        if (pX == x || pY == y) {
                            output.add(nodeGraph[pY][pX]);
                        } else if (nodeGraph[y][pX] != null && nodeGraph[pY][x] != null) {
                            output.add(nodeGraph[pY][pX]);
                        }
                    }
                }
            }
        }
        return output;
    }

}

