package engine.components.controllers;

import engine.Entity;
import engine.Ray;
import engine.Vec;
import engine.components.ComponentType;
import engine.components.TickingInterface;
import engine.world.AI.PathFinder;
import engine.world.GridWorld;
import engine.world.Node;
import objects.tank.Tank;
import objects.tank.TankMovementComponent;
import objects.turret.Turret;

import java.util.ArrayList;
import java.util.Random;

public class TankAIController implements TickingInterface {

    private static final Random rand = new Random();
    private final PathFinder pather;
    private final TankMovementComponent moveComp;
    private final Tank tank;
    private final Turret turret;
    private final ArrayList<Entity>[][] grid;
    private final GridWorld gridWorld;
    boolean playerSight = false;
    private MovementState movementState = MovementState.MOVING;
    private int[] goal = {14, 5};
    private Node previousTarget = null;
    private Tank tankInSight = null;
    private Ray ray;
    private Vec rayStart;
    //private boolean stuck = false;

    public TankAIController(Tank tank) {
        this.tank = tank;
        gridWorld = tank.handler.getGridWorld();
        grid = gridWorld.getGrid();
        pather = new PathFinder();
        this.turret = tank.turret;
        tank.gridPosComp.recentCellChange = true;
        moveComp = tank.moveComp;

        path();

        //new PathVis(tank.handler, pather);
    }

    @Override
    public void tick() {

        move();

        rayStart = Vec.add(Vec.scalarMult(turret.pos.getRotation(), 50), turret.pos.get());
        ray = new Ray(rayStart, Vec.scalarMult(turret.pos.getRotation(), 5), turret.handler.getGridWorld());
        /* this should work if we ignore obstacles, but we should still find
        a way to detect walls in the way to avoid the enemy killing himself
        */
        Object player = ray.getCollision().getEntity();

        if (Tank.getTankList().size() > 1) {
            tankInSight = Tank.getPathSortedTankList(tank).get(0);
//            System.out.println(tankInSight);
//            System.out.println(Tank.getPathSortedTankList(tank).size());
            double distanceToTank = Math.sqrt(Math.pow(tankInSight.pos.getIntX() - tank.pos.getIntX(), 2)
                    + Math.pow(tankInSight.pos.getIntY() - tank.pos.getIntY(), 2));

            //starting distance between enemy and player is about 150
            if (distanceToTank < 250.0) {
                playerSight = true;
                //Made it so it only fires if the gun is within 10 degree of target
                if (Math.abs(turret.turnTo(tankInSight.pos.get())) < 10) {
                    if (player == tankInSight) tank.turret.fire();
                }
            } else {
                Vec target;
                if (pather.currentRoute.size() > 1) target = pather.currentRoute.get(pather.currentRoute.size() - 2).pos.get();
                else {
                    target = Vec.add(tank.pos.get(), tank.pos.getRVec());
                }
                turret.turnTo(target);
            }
            //System.out.println("IF WRONG");
            //System.out.println(distanceToTank);
        }

//        if(player == tankInSight){
//            System.out.println("SHOOT");
//        }
    }

    private void move() {
        boolean stuck = false;

        if (tank.gridPosComp.recentCellChange) {
            path();
        }

        Node target = null;
        if (pather.currentRoute.size() > 1) target = pather.currentRoute.get(pather.currentRoute.size() - 2);
        if (target != null) {

            Vec oPos = tank.pos.get();
            if (previousTarget != null && previousTarget != target) {
                float distFromPrev = Vec.getFastMag(Vec.subtract(previousTarget.pos.get(), tank.pos.get()));

                if (distFromPrev < 100) {
                    previousTarget = target;
                } else {
                    target = previousTarget;
                }
            }

            float preAngle = tank.pos.getR();
            Vec prePos = tank.pos.get();
            boolean turnComplete = moveComp.turnToCoord(target.pos.get());

            //System.out.println("Turn complete: " + turnComplete);
            //System.out.println("Current pos: " + tank.pos.getX() + ", " + tank.pos.getY());
            //System.out.println("Target pos: " + target.pos.getX() + ", " + target.pos.getY());


            if (turnComplete) {
                moveComp.moveForward();
                if (Vec.equals(tank.pos.get(), prePos)) {
                    stuck = true;
                    path();

                    moveComp.moveBackward();
                    moveComp.moveBackward();
                    moveComp.moveBackward();
                }

            } else if (Math.abs(tank.pos.getR() - preAngle) < 0.00001) {

                if (Vec.subtract(tank.pos.get(), oPos).getMagnitude() < 0.1) {
                    stuck = true;
                    path();
                }

                if (moveComp.moveForward()) {
                    moveComp.moveBackward();
                    moveComp.moveBackward();
                    moveComp.moveBackward();
                }
            }

        } else {
            movementState = MovementState.READY;
        }

        previousTarget = stuck ? null : target;

        if (movementState == MovementState.READY) {
            do {
                goal = new int[]{rand.nextInt(grid[0].length - 2) + 1, rand.nextInt(grid.length - 2) + 1};
            } while (grid[goal[1]][goal[0]] == null);
            path();
            movementState = MovementState.MOVING;
        }
    }


    /**
     * Recalculates the path to the current target.
     */
    private void path() {
        //todo clean this up by removing all the gridposcomp repetition.
        pather.tankRouteFind(tank, gridWorld.nodeGraph[goal[1]][goal[0]]);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TICKING;
    }

    @Override
    public void initialize() {
    }
}

