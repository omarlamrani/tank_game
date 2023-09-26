package objects.turret;

import engine.handler.Handler;
import engine.SoundFX;
import engine.Vec;
import engine.components.controllers.TurretControls;
import objects.GameObject;
import objects.RenderComponents.TurretRenderComponent;
import objects.projectiles.Projectile;
import objects.tank.Tank;

import static engine.components.controllers.TurretMouseController.getTurnFromTarget;

/**
 * a Turret object is created by a Tank object when the Tank object is instantiated. The Turret sits
 * on top of the Tank and moves with the Tank around the map. However, it rotates independently of the
 * Tank object.<br><br>
 * MAIN METHODS:<br>
 * Turret - constructor, creates Turret and sets its position to match the Tank<br>
 * tick - updates state of turret each tick and animates it<br>
 * dropMine - [UNFINISHED METHOD] will drop a landmine when finished<br><br>
 * ATTRIBUTES<br>
 * ROTATION_SPEED - how fast the turret rotates<br>
 * ANIMATION_DELAY - ?<br>
 * parent - the Tank object that the Turret is attached to<br>
 * renderer, controls - components that handle animation and movement
 */
public class Turret extends GameObject implements TurretControls {

    //static Image turret = new Image("file:src/main/resources/turret.png");
    //static Image blast = new Image("file:src/main/resources/blast.png");
    //public transient Image sprite;

    //static ShadowWrapper effect = new ShadowWrapper(10.F, Color.BLACK);

    //private int frame = 0;
    public static final float ROTATION_SPEED = 2F;
    protected final int ANIMATION_DELAY = 5;
    public boolean animating = false;
    protected int NUM_FRAMES = 7;
    protected int delay = 0; //Frame delay
    public Tank parent;

    protected TurretRenderComponent renderer;
    //public TurretMovementComponent controls;

    //PositionComponent pos;

    /**
     * Constructor for the Turret class, which is called by the Tank constructor. it creates a Turret
     * object, and sets its position to the position of the parent Tank, causing it to stay in the same
     * location relative to the parent Tank object, and giving the illuion that the Tank and Turret are
     * one object.
     *
     * @param handler handler is passed to handle components etc
     * @param tank    the parent Tank object that the Turret is connected to
     */
    public Turret(Handler handler, Tank tank) {
        super(handler, tank.pos.get());

        this.pos.x = tank.pos.x;
        this.pos.y = tank.pos.y;
        this.parent = tank;

        this.renderer = this.addComponent(new TurretRenderComponent(pos, tank.hue));

        this.initComponents();
    }

    /**
     * updates the state of the turret object every in-game tick, and handles animation.
     */
    public void tick() {
        //HACKY ANIMATION CODE AHEAD
        //THIS SHOULD BE MADE IT'S OWN COMPONENT OR SOMETHING

        if (delay != 0) delay--;
        if (renderer.frame == NUM_FRAMES) {
            animating = false;
            renderer.frame = 0;
        }
        if (animating && delay == 0) {
            renderer.frame++;
            delay = ANIMATION_DELAY;
        }
    }

    private void turn(float r) {
        pos.setR(pos.getR() + r);
    }


    public float turnTo(Vec t) {
        float turn = getTurnFromTarget(Vec.subtract(t, pos.get()).getAngle(), pos.getR(), Turret.ROTATION_SPEED);
        turn(turn);
        return (turn);
    }

    public void fire() {
        if (!animating) {

            //Little bit of magic to make it look like it's appearing in the right place while moving
            new Projectile(parent.handler, Vec.add(Vec.add(Vec.scalarMult(pos.getRotation(), 45), parent.pos.get()), Vec.scalarMult(parent.vel, 2)), Vec.scalarMult(pos.getRotation(), 5));

            //Bullet fire Sound Effects
            String path = "src/main/resources/sounds/gunshot.mp3"; //sound location
            SoundFX bulletFire = new SoundFX(path);
            bulletFire.play(); //plays sound

            animating = true;
        }
    }


}
