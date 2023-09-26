package objects.projectiles;

import engine.SoundFX;
import engine.Vec;
import engine.components.FreeCollider;
import engine.components.VelocityComponent;
import engine.handler.Handler;
import engine.wrappers.FloatWrapper;
import objects.GameObject;
import objects.RenderComponents.CannonBallRenderComponent;
import objects.RenderComponents.ProjectileRenderComponent;
import objects.grid.Wall;
import objects.grid.WallConnector;
import objects.grid.WorldBorder;
import objects.tank.Tank;

public class CannonBall extends GameObject {

    private final VelocityComponent vel;
    private final CannonBallRenderComponent render;
    private final FreeCollider collider;
    //the classes that can be bounced off (walls, the edge of the map etc.)
    private final Class[] bounceObjects = {Wall.class, WorldBorder.class, WallConnector.class};
    //the classes that can be 'hit' (i.e. damaged) by the projectile
    private final Class[] targetObjects = {Tank.class};
    private int bounceCounter = 0; //how many times the bullet has hit a wall

    public CannonBall(Handler handler, Vec pos, Vec vel) {
        super(handler, pos);
        this.pos.setPos(pos);
        this.pos.setR(vel.getRotation());
        this.collider = this.addComponent(new FreeCollider(this.pos, 1, 1/*new Ellipse2D.Float(0, 0, 15, 15)*/, handler.getGridWorld()));
        collider.r = new FloatWrapper(0F);

        render = this.addComponent(new CannonBallRenderComponent(this.pos));
        this.vel = this.addComponent(new VelocityComponent(this.pos));

        this.vel.set(vel);

        this.initComponents();
    }

    public boolean move(Vec move, int depth) {
        if (depth == 0) return false;

        Vec dest = Vec.add(move, pos.get());
        if (collider.localCollisionTest(dest, bounceObjects)) {

            dest = pos.get();

            int colAcc = 8;

            float xCreep = move.getX() / colAcc;
            float yCreep = move.getY() / colAcc;
            int xCounter = colAcc;
            int yCounter = colAcc;
            for (int i = 0; i < colAcc; i++) {
                dest.setX(dest.getX() + xCreep);
                xCounter--;
                if (collider.localCollisionTest(dest, bounceObjects)) {
                    dest.setX(dest.getX() - xCreep);
                    break;
                }
                dest.setY(dest.getY() + yCreep);
                yCounter--;
                if (collider.localCollisionTest(dest, bounceObjects)) {
                    dest.setY(dest.getY() - yCreep);
                    break;
                }
            }

            pos.setPos(dest);

            float fracMoveLeft;

            if (xCounter < yCounter) {
                vel.setX(vel.getX() * -1);
            } else {
                vel.setY(vel.getY() * -1);
            }

            fracMoveLeft = Math.abs(((float) (xCounter + yCounter + 1)) / (colAcc * 2));

            move(Vec.scalarMult(move, fracMoveLeft).setRotation(vel.getRotation()), depth - 1);

            pos.setR(move.getAngle());
            return true;
        } else {
            if (collider.localCollisionTest(dest, targetObjects)) {
                hit(dest, 35);
                return true;
            } else {
                pos.setPos(Vec.add(pos.get(), move));
                return false;
            }
        }
    }

    public void hit(Vec dest, float hpReduction) {
        for (Tank t : Tank.getTankList()) {
            if (collider.testForCollisionWithObject(t.collider, dest)) {
                SoundFX collision = new SoundFX("src/main/resources/sounds/collision.mp3");
                collision.play();
                t.reduceHealthPoints(hpReduction);
                /*
                if (t.getHealthPoints() > hpReduction) {
                    t.reduceHealthPoints(hpReduction);
                } else {
                    t.remove();
                    //t.healthBar.remove();
                }

                 */
            }
        }
        this.remove();
    }

    @Override
    public void tick() {
        render.lifeTick();

        //test for collision at dest
        Vec prePos = pos.get();

        if (move(vel, 5)) bounceCounter++;

        if (Vec.subtract(pos.get(), prePos).getMagnitude() > vel.getMagnitude() + 0.1) {
            System.out.println("Warp detected!");
        }

        if (this.bounceCounter >= 6) {
            this.remove();
        }
    }
}
