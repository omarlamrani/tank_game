package objects.tank;

import engine.Vec;
import engine.components.ComponentType;
import engine.components.FreeCollider;
import engine.components.GridPositionComponent;
import engine.components.PositionComponent;
import engine.components.controllers.TankControls;
import engine.components.controllers.TurretMouseController;


public class TankMovementComponent extends TankControls {


    public static float SPEED;
    public static float TURN_SPEED = 2;

    private final PositionComponent pos;
    private final FreeCollider collider;
    private final GridPositionComponent gridPosComp;

    public TankMovementComponent(PositionComponent pos, FreeCollider collider, GridPositionComponent gridPosComp) {
        this.pos = pos;
        this.collider = collider;
        this.gridPosComp = gridPosComp;
    }

    /**
     * attempts to move in the direction of the movement vector provided, taking into account collisions
     *
     * @param move The direction and magnitude to move.
     * @return if a collision occurred.
     */
    public boolean attemptMove(Vec move) {
        Vec targetPos = Vec.add(pos.get(), move);
        boolean collisionFlag = false;
        //boolean collision = getCollision(targetPos);
        boolean collision = collider.localCollisionTest(targetPos, collider.getCollides());
        collisionFlag = collision;

        if (collision) {
            targetPos = Vec.add(pos.get(), new Vec(0, move.getY()));
            collision = collider.localCollisionTest(targetPos, collider.getCollides());
            if (collision) {
                targetPos = Vec.add(pos.get(), new Vec(move.getX(), 0));
                collision = collider.localCollisionTest(targetPos, collider.getCollides());
            }
        }

        if (!collision) pos.setPos(targetPos);
        gridPosComp.updatePos = true;
        return collisionFlag;
    }

    public void turnClockwise() {
        attemptTurn(TURN_SPEED);
    }

    public void turnAntiClockwise() {
        attemptTurn(-TURN_SPEED);
    }


    public boolean turnToCoord(Vec target) {
        return turnToAngle(Vec.subtract(target, pos.get()).getAngle());
    }

    public boolean turnToAngle(float target) {
        float turn = TurretMouseController.getTurnFromTarget(target, pos.getR(), TURN_SPEED);
        //System.out.println("turn: " + turn + ", target: " + target + ", current: " + pos.getR());

        //This solves AI jitter but adds wobble
        //todo fix this.
        if (Math.abs(turn) < 2) return true;
        attemptTurn(turn);
        return false;
    }

    public boolean moveForward() {
        return attemptMove(Vec.scalarMult(pos.getRotation(), SPEED));
    }

    public boolean moveBackward() {
        return attemptMove(Vec.scalarMult(pos.getRotation(), -SPEED / 2));
    }

    private boolean getCollision(Vec position) {
        return collider.testForCollision(collider.getNearby(position, collider.getCollides()), position);
    }


    public void attemptTurn(float turn) {
        pos.setR(pos.getR() + turn);
        if (getCollision(pos.get())) {
            pos.setR(pos.getR() - turn);

            //while (!getCollision(pos.get())) {
            //    pos.setR(pos.getR() + turn / 10);
            //}
            //pos.setR(pos.getR() - turn / 10);
        }
        //TODO make this search for a lesser turn if one is needed.
    }


    @Override
    public ComponentType getType() {
        return null;
    }
}
